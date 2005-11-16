/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.rsf.renderer.RenderSystemStatic;
import uk.org.ponder.rsf.state.FossilizedConverter;
import uk.org.ponder.rsf.state.RequestSubmittedValueCache;
import uk.org.ponder.rsf.state.SVESorter;
import uk.org.ponder.rsf.state.SubmittedValueEntry;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;

/**
 * A request-scope bean whose job is to determine the (topologically sorted) set
 * of submitted request submitted values for this POST request. If this is a
 * RENDER request, returns an empty listt.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class PostDecoder {
  private FossilizedConverter fossilizedconverter;
  private RenderSystemStatic rendersystemstatic;
  private Map requestparams;
  private String requesttype;
  private Map normalizedrequest;
  

  public void setFossilizedConverter(FossilizedConverter fossilizedconverter) {
    this.fossilizedconverter = fossilizedconverter;
  }

  public void setRenderSystemStatic(RenderSystemStatic rendersystemstatic) {
    this.rendersystemstatic = rendersystemstatic;
  }

  public void setRequestMap(Map requestparams) {
    this.requestparams = requestparams;
  }

  public void setRequestType(String requesttype) {
    this.requesttype = requesttype;
  }
  
  public void init() {
    normalizedrequest = new HashMap();
    normalizedrequest.putAll(requestparams);
    rendersystemstatic.normalizeRequestMap(requestparams);
  }

  
  // This method is expected to be called by accreteRSVC
  public void parseRequest(RequestSubmittedValueCache rsvc) {
    // NB checks for value size are needed for default JSF parameter
    // implementation
    // which synthesises hidden fields for every key/value set within a form.
    for (Iterator keyit = normalizedrequest.keySet().iterator(); keyit.hasNext();) {
      String key = (String) keyit.next();
      String[] values = (String[]) normalizedrequest.get(key);
      Logger.log.info("PostInit: key " + key + " value " + values[0]);
      String elpath = BeanUtil.stripEL(key);
      // First parse pure EL bindings
      // TODO: use first character of value as a type indicator
      if (elpath != null && values.length > 0) {
        Logger.log.info("Setting EL parameter " + key + " to value "
            + values[0]);
        SubmittedValueEntry sve = new SubmittedValueEntry();
        sve.valuebinding = elpath;
        sve.newvalue = values;
        rsvc.addEntry(sve);

      }
      // Secondly assess whether this was a component fossilised binding.
      else if (fossilizedconverter.isFossilisedBinding(key)
          && values.length > 0) {
        SubmittedValueEntry sve = fossilizedconverter.parseFossil(key,
            values[0]);

        String[] newvalue = (String[]) normalizedrequest.get(sve.componentid);
        sve.newvalue = newvalue;
        fossilizedconverter.fixupNewValue(sve, rendersystemstatic, key,
            values[0]);
        Logger.log.info("Discovered fossilised binding for " + sve.valuebinding
            + " for component " + sve.componentid + " with old value "
            + sve.oldvalue);
        rsvc.addEntry(sve);
      }
    }
  }

  public RequestSubmittedValueCache getRequestRSVC() {
    RequestSubmittedValueCache newvalues = new RequestSubmittedValueCache();
    if (requesttype.equals(ViewParameters.ACTION_REQUEST)) {
      parseRequest(newvalues);
      // Topologically sort the fresh values (which may be arbitrarily
      // disordered through passing the request) in order of intrinsic
      // dependency.
      SVESorter sorter = new SVESorter(newvalues);
      List sorted = sorter.getSortedRSVC();
      for (int i = 0; i < sorted.size(); ++i) {
        newvalues.addEntry((SubmittedValueEntry) sorted.get(i));
      }
    }
    return newvalues;
  }

  public Map getNormalizedRequest() {
    return normalizedrequest;
  }
  
  public static String decodeAction(Map normalizedrequest) {
    String[] actionmethods = (String[]) normalizedrequest
        .get(SubmittedValueEntry.FAST_TRACK_ACTION);
    if (actionmethods != null) {
      String actionmethod = actionmethods[0];
      actionmethod = BeanUtil.stripEL(actionmethod);
      return actionmethod;
    }
    return null;
  }

  public static String decodeSubmittingControl(Map normalized) {
    // This MUST be set or the post is erroneous.
    String[] array = (String[]) normalized
        .get(SubmittedValueEntry.SUBMITTING_CONTROL);
    return array[0];
  }

}
