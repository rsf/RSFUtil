/*
 * Created on Nov 23, 2004
 */
package uk.org.ponder.rsf.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.arrayutil.ArrayUtil;
import uk.org.ponder.beanutil.BeanContainerWrapper;
import uk.org.ponder.beanutil.PathUtil;
import uk.org.ponder.errorutil.CoreMessages;
import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.mapping.DARApplier;
import uk.org.ponder.mapping.DARReceiver;
import uk.org.ponder.mapping.DataAlterationRequest;
import uk.org.ponder.rsf.renderer.RenderUtil;
import uk.org.ponder.rsf.state.RequestStateEntry;
import uk.org.ponder.rsf.state.RequestSubmittedValueCache;
import uk.org.ponder.rsf.state.SubmittedValueEntry;
import uk.org.ponder.rsf.util.RSFUtil;
import uk.org.ponder.util.AssertionException;
import uk.org.ponder.util.Logger;
import uk.org.ponder.webapputil.ViewParameters;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * PostHandler is a request scope bean responsible for handling an HTTP
 * POST request. 
 */
public class PostHandler {

  public static final String RSVC_VAR = "uk.org.ponder.rsvc";
  private DARApplier darapplier;
  private BeanContainerWrapper beanwrapper;
  private ActionResultInterpreter ari;
  private RequestStateEntry requeststateentry;

  // this will be used to locate request-scope beans.
  public void setBeanContainerWrapper(BeanContainerWrapper beanwrapper) {
    this.beanwrapper = beanwrapper;
  }

  public void setDARApplier(DARApplier darapplier) {
    this.darapplier = darapplier;
  }

  public void setRequestState(RequestStateEntry requeststateentry) {
    this.requeststateentry = requeststateentry;
  }
  
  public void setActionResultInterpreter(ActionResultInterpreter ari) {
    this.ari = ari;
  }

  public static boolean valueUnchanged(Object oldvalue, Object newvalue) {
    if (oldvalue instanceof String) {
      // special hack for dealing with checkboxes, and a bit further for
      // suppressed components - we should probably make some attempt to be
      // component-aware here. 
      if (newvalue == null) {
        newvalue = oldvalue.equals("")? "" : "false";
      }
      return oldvalue.equals(newvalue);
    }
    else if (oldvalue instanceof String[]) {
      String[] olds = (String[]) oldvalue;
      String[] news = (String[]) newvalue;
      return ArrayUtil.lexicalCompare(olds, olds.length, news, news.length) != 0;
    }
    else
      throw new AssertionException("Unknown value type " + oldvalue);
  }

  public ViewParameters handle(ViewParameters origrequest, Map origrequestparams) {
    HashMap requestparams = new HashMap();
    requestparams.putAll(origrequestparams);
    String key = RenderUtil.findCommandParams(requestparams);
    if (key != null) {
      String params = key.substring(SubmittedValueEntry.COMMAND_LINK_PARAMETERS.length());
      RenderUtil.unpackCommandLink(params, requestparams);
      requestparams.remove(key);
    }
    
    String actionmethod = ((String[]) requestparams.get(ViewParameters.FAST_TRACK_ACTION))[0];
    
    actionmethod = RSFUtil.stripEL(actionmethod);
    
    String result = null;
    RequestSubmittedValueCache rsvc = null;
    try {
      rsvc = applyValues(requestparams);
      result = (String) beanwrapper.invokeBeanMethod(actionmethod);
    }
    catch (Exception e) {
      Logger.log.error(e);
      ThreadErrorState.addError(new TargettedMessage(
          CoreMessages.GENERAL_ACTION_ERROR));
    }
    
    String linkid = TargettedMessage.TARGET_NONE;
   
    ARIResult arires = ari.interpretActionResult(origrequest, result);
    requeststateentry.requestComplete(rsvc);
    
    arires.resultingview.viewtoken = requeststateentry.outgoingtokenID;
    return origrequest;
  }

  

  // This is assumed to be a standard servlet map of Strings to String[].
  // one would assume this was easy enough to assure with any other technology
  public RequestSubmittedValueCache applyValues(Map requestparams) {
    RequestSubmittedValueCache rsvc = new RequestSubmittedValueCache();

    // Directly sent EL expressions are assumed to be from hidden fields and
    // not the user - to supply context for the call, e.g. loading metadata.

    // NB checks for value size are needed for default JSF parameter
    // implementation
    // which synthesises hidden fields for every key/value set within a form.
    for (Iterator keyit = requestparams.keySet().iterator(); keyit.hasNext();) {
      String key = (String) keyit.next();
      String[] values = (String[]) requestparams.get(key);
      String value = values[0];
      Logger.log.info("PostInit: key " + key + " value " + value);
      String elpath = RSFUtil.stripEL(key);
      if (elpath != null && value.length() > 0) {
        Logger.log.info("Setting EL parameter " + key + " to value "
            + requestparams.get(key));
        for (int i = 0; i < values.length; ++i) {
          darapplier.setBeanValue(elpath, beanwrapper, values[i]);
        }

      }
      if (SubmittedValueEntry.isFossilisedBinding(key) && value.length() > 0) {
        SubmittedValueEntry sve = new SubmittedValueEntry(key, value);
        Logger.log.info("Discovered fossilised binding for " + sve.valuebinding
            + " for component " + sve.componentid + " with old value "
            + sve.oldvalue);
        if (sve.isdeletion) {
          // process deletions separately, they have no state and so evade the
          // rsvc
          String rootpath = PathUtil.getHeadPath(sve.valuebinding);
          DARReceiver rootbean = (DARReceiver) beanwrapper
              .locateRootBean(rootpath);
          String strippedpath = PathUtil.getFromHeadPath(sve.valuebinding);
          rootbean.addDataAlterationRequest(new DataAlterationRequest(
              strippedpath, sve.oldvalue, DataAlterationRequest.DELETE));
        }
        else {
          rsvc.addEntry(sve);
        }
      }
    }
    // Main "apply request values" processing - for each fossilised binding now
    // stored in the rsvc where value has changed, queue up the value change
    // with
    //    the DARApplier.
    for (int i = 0; i < rsvc.entries.size(); ++i) {
      SubmittedValueEntry sve = (SubmittedValueEntry) rsvc.entries.get(i);
      String[] values = (String[]) requestparams.get(sve.componentid);
      String newvalue = values == null? null : values[0];
      // a null new value might be an unchecked check box.... or else a component
      // that was suppressed entirely during rendering.
      Logger.log.info("Fossilised binding for " + sve.valuebinding
          + " old value " + sve.oldvalue + " new value " + newvalue);
      // this is only a "UI level" value changed indication. It cannot
      // detect whether the user has made a value change that would cause
      // no semantic change to the data value. - WHERE AM I? Impossible to locate new value here, JSF used to do it by magic! Should we just fork through the bean reference? Probably not.
      if (valueUnchanged(sve.oldvalue, newvalue)) {
        continue;
      }
      String rootpath = PathUtil.getHeadPath(sve.valuebinding);
      Object rootbean = beanwrapper.locateRootBean(rootpath);
      // if it is a receiver, apply the request for deferred handling
      if (rootbean instanceof DARReceiver) {
        String strippedpath = PathUtil.getFromHeadPath(sve.valuebinding);
        DataAlterationRequest dar = new DataAlterationRequest(strippedpath,
            newvalue);
        // CONVERSIONS should really occur here, while we still know the
        // component IDs and can generate errors.
        ((DARReceiver) rootbean).addDataAlterationRequest(dar);
      }
      else {
        // else apply the change immediately
        darapplier.setBeanValue(sve.valuebinding, beanwrapper, newvalue);
      }
    }
    return rsvc;
  }

}