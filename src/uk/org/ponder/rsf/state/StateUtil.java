/*
 * Created on Nov 8, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.beanutil.PathUtil;
import uk.org.ponder.graph.ConcreteGraph;
import uk.org.ponder.mapping.DARReceiver;
import uk.org.ponder.mapping.DataAlterationRequest;
import uk.org.ponder.util.Logger;

public class StateUtil {
  
  public static void parseValue(String key, String value, RequestSubmittedValueCache rsvc) {
    Logger.log.info("PostInit: key " + key + " value " + value);
    String elpath = BeanUtil.stripEL(key);
    if (elpath != null && value.length() > 0) {
      Logger.log.info("Setting EL parameter " + key + " to value "
          + value);
      SubmittedValueEntry sve = new SubmittedValueEntry();
      sve.valuebinding = elpath;
      sve.newvalue = value;
      rsvc.addEntry(sve);
     
    }
    if (SubmittedValueEntry.isFossilisedBinding(key) && value.length() > 0) {
      SubmittedValueEntry sve = SubmittedValueEntry.parseFossil(key, value);
      Logger.log.info("Discovered fossilised binding for " + sve.valuebinding
          + " for component " + sve.componentid + " with old value "
          + sve.oldvalue);
      rsvc.addEntry(sve);
    }
  }
  
  public static void accreteRSVC(Map requestparams, RequestSubmittedValueCache rsvc) {
    RequestSubmittedValueCache newvalues = new RequestSubmittedValueCache();
    // Directly sent EL expressions are assumed to be from hidden fields and
    // not the user - to supply context for the call, e.g. loading metadata.

    // NB checks for value size are needed for default JSF parameter
    // implementation
    // which synthesises hidden fields for every key/value set within a form.
    for (Iterator keyit = requestparams.keySet().iterator(); keyit.hasNext();) {
      String key = (String) keyit.next();
      String[] values = (String[]) requestparams.get(key);
      for (int i = 0; i < values.length; ++ i) {
        parseValue(key, values[i], newvalues);
      }
    }
    
    topologicalSort(newvalues);
    
      String value = values[0];
      Logger.log.info("PostInit: key " + key + " value " + value);
      String elpath = BeanUtil.stripEL(key);
      if (elpath != null && value.length() > 0) {
        Logger.log.info("Setting EL parameter " + key + " to value "
            + requestparams.get(key));
        SubmittedValueEntry sve = new SubmittedValueEntry()
        for (int i = 0; i < values.length; ++i) {
          // the value may be a leaf, an El r-value or an XML-encoded object. 
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
          // TODO: They MUST NOT! Otherwise it is not replayable!
          String rootpath = PathUtil.getHeadPath(sve.valuebinding);
          DARReceiver rootbean = (DARReceiver) beanwrapper
              .locateBean(rootpath);
          String strippedpath = PathUtil.getFromHeadPath(sve.valuebinding);
          rootbean.addDataAlterationRequest(new DataAlterationRequest(
              strippedpath, sve.oldvalue, DataAlterationRequest.DELETE));
        }
        else {
          rsvc.addEntry(sve);
        }
      }
    }
  }
  

  private void topologicalSort(RequestSubmittedValueCache rsvc) {
    ELDependencyMap elmap = new ELDependencyMap();
    ConcreteGraph graph = new ConcreteGraph();
    HashMap numbermap = new HashMap();
    
    for (int i = 0; i < rsvc.entries.size(); ++ i) {
      SubmittedValueEntry entry = rsvc.entryAt(i);
      elmap.recordWrite(entry.valuebinding, entry);
      int vnno = graph.createVertex();
      numbermap.put(entry, new Integer(vnno));
    }
    
    for (int i = 0; i < rsvc.entries.size(); ++ i) {
      SubmittedValueEntry entry = rsvc.entryAt(i);
      List invalidate = elmap.getInvalidatingEntries(entry.valuebinding);
      if (entry.newvalue instanceof String) {
        String readel = BeanUtil.stripEL((String) entry.newvalue);
        if (readel != null) {
          List invalidateread = elmap.getInvalidatingEntries(readel);
          invalidate.addAll(invalidateread);
        }
      }
      for (int j = 0; i < invalidate.size(); ++ i) {
        SubmittedValueEntry sve = (SubmittedValueEntry) invalidate.get(j);
        int number = ((Integer)numbermap.get(sve)).intValue();
        graph.createEdge(number, i);
      }
    }
    // Each entry is a WRITE to the EL path "valuebinding".
    // In addition, a "fast EL" may represent a "read" from path newvalue,
    // if newvalue represents an EL.
    // Component binding: will WRITE to valuebinding ( when real value appears )
    // EL: will WRITE to valuebinding, READ from newvalue. 
    // these READS must be shifted until AFTER writes of values related to newvalue.
    
    // i) a WRITE to a nested path DEPENDS ON a WRITE to a STRICTLY higher path.
    // ii) a READ from a nested path DEPENDS ON a WRITE to a STRICTLY OR EQUAL higher path.
    // iii) a WRITE to the SAME PATH should technically just replace the dependent write.
    
    // OK then. edge created FROM bean TO dependency, i.e. that which operates on higher path.
    //                   WRITE                             READ
    // e.g. valuebinding {permissionbean.name}       value [field]             NODE 1
    // fast EL:          {resource.a8394.permission} value {permissionbean}    NODE 2
    // edge FROM 2 -> 1
    // hah. fast EL is actually SLOW!
    
    
    // toposort is only intended to operate on fast EL values - these must be
    // i) submitted before all component bindings,
  }
}
