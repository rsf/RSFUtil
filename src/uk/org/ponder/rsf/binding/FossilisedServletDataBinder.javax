/*
 * Created on Aug 1, 2005
 */
package uk.org.ponder.rsf.binding;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import uk.org.ponder.beanutil.RootBeanLocator;
import uk.org.ponder.errorutil.RequestSubmittedValueCache;
import uk.org.ponder.errorutil.SubmittedValueEntry;
import uk.org.ponder.mapping.DARReceiver;
import uk.org.ponder.mapping.DataAlterationRequest;
import uk.org.ponder.rsf.util.RSFUtil;
import uk.org.ponder.util.Logger;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * Currently disused, incomplete rendering of PostHandler to use official
 * Spring architecture.
 */
public class FossilisedServletDataBinder extends DataBinder {
  RootBeanLocator rootbeanlocator;
  /**
   * @param target
   * @param objectName
   */
  public FossilisedServletDataBinder(Object target, String objectName) {
    super(target, objectName);
    // TODO Auto-generated constructor stub
  }

  public void bind(ServletRequest request) {
    RequestSubmittedValueCache rsvc = new RequestSubmittedValueCache();
    //requestmap.put(RSVC_VAR, rsvc);
    Map requestparams = request.getParameterMap();

    
    MutablePropertyValues fastpvs = new MutablePropertyValues();
    
    MutablePropertyValues oldpvs = new MutablePropertyValues();
    MutablePropertyValues newpvs = new MutablePropertyValues();
// Directly sent EL expressions are assumed to be from hidden fields and
// not the user - to supply context for the call, e.g. loading metadata.

    // NB checks for value size are needed for default JSF parameter
    // implementation
    // which synthesises hidden fields for every key/value set within a form.
    for (Iterator keyit = requestparams.keySet().iterator(); keyit.hasNext();) {
      String key = (String) keyit.next();
      String value = (String) requestparams.get(key);
      Logger.log.info("PostInit: key " + key + " value " + value);
      if (key.startsWith("#{") && value.length() > 0) {
        
        Logger.log.info("Setting EL parameter " + key + " to value "
            + value);
        String el = key.substring(2, key.indexOf('}'));
        fastpvs.addPropertyValue(el, value);
      }
      bind(fastpvs);
      
      if (SubmittedValueEntry.isFossilisedBinding(key) && value.length() > 0) {
        SubmittedValueEntry sve = new SubmittedValueEntry(key, value);
        Logger.log.info("Discovered fossilised binding for " + sve.valuebinding
            + " for component " + sve.componentid + " with old value "
            + sve.oldvalue);
        if (sve.isdeletion) {
          // process deletions separately, they have no state and so evade the
          // rsvc
          DARReceiver rootbean = (DARReceiver) rootbeanlocator
              .locateRootBean(sve.valuebinding);
          String strippedpath = null;
          rootbean.addDataAlterationRequest(new DataAlterationRequest(
              strippedpath, sve.oldvalue, DataAlterationRequest.DELETE));
        }
        else {
          rsvc.addEntry(sve);      
        }
      }
    }

    for (int i = 0; i < rsvc.entries.size(); ++i) {
      SubmittedValueEntry sve = (SubmittedValueEntry) rsvc.entries.get(i);
      String newvalue = (String) requestparams.get(sve.componentid);
      Logger.log.info("Fossilised binding for " + sve.valuebinding
          + " old value " + sve.oldvalue + " new value " + newvalue);
      // skip it if there is no new value, or it has not changed
      if (newvalue == null || newvalue.equals(sve.oldvalue)) {
        continue;
      }
      Object rootbean = rootbeanlocator.locateRootBean(sve.valuebinding);
      // if it is a receiver, apply the request for deferred handling
      if (rootbean instanceof DARReceiver) {
        String strippedpath = null;
        DataAlterationRequest dar = new DataAlterationRequest(strippedpath,
            newvalue);
        // CONVERSIONS should really occur here, while we still know the
        // component IDs and can generate errors.
        ((DARReceiver) rootbean).addDataAlterationRequest(dar);
      }
      else {
        // else apply the change immediately
      }
    }
  }
  
}
