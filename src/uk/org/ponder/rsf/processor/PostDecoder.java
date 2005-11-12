/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.rsf.state.FossilizedConverter;
import uk.org.ponder.rsf.state.RequestSubmittedValueCache;
import uk.org.ponder.rsf.state.SVESorter;
import uk.org.ponder.rsf.state.SubmittedValueEntry;
import uk.org.ponder.util.Logger;

// currently only request-scope via RenderSystem, which should be split.
public class PostDecoder {
  private FossilizedConverter fossilizedconverter;

  public void setFossilizedConverter(FossilizedConverter fossilizedconverter) {
    this.fossilizedconverter = fossilizedconverter;
  }

  // This method is expected to be called by accreteRSVC
  public void parseRequest(Map requestparams, RequestSubmittedValueCache rsvc) {
    // NB checks for value size are needed for default JSF parameter
    // implementation
    // which synthesises hidden fields for every key/value set within a form.
    for (Iterator keyit = requestparams.keySet().iterator(); keyit.hasNext();) {
      String key = (String) keyit.next();
      String[] values = (String[]) requestparams.get(key);
      Logger.log.info("PostInit: key " + key + " value " + values[0]);
      String elpath = BeanUtil.stripEL(key);
      // First parse pure EL bindings
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

        String[] newvalue = (String[]) requestparams.get(sve.componentid);
        sve.newvalue = newvalue;
        fossilizedconverter.fixupNewValue(sve, key, values[0]);
        Logger.log.info("Discovered fossilised binding for " + sve.valuebinding
            + " for component " + sve.componentid + " with old value "
            + sve.oldvalue);
        rsvc.addEntry(sve);
      }
    }

  }

  /**
   * Gather any new SubmittedValueEntries from the supplied request map, and
   * append them (sorted topologically) to the end of the supplied rsvc.
   */
  public void accreteRSVC(Map requestparams, RequestSubmittedValueCache rsvc) {
    RequestSubmittedValueCache newvalues = new RequestSubmittedValueCache();
    parseRequest(requestparams, newvalues);

    // Topologically sort the fresh values (which may be arbitrarily disordered
    // through passing the request) in order of intrinsic dependency.
    SVESorter sorter = new SVESorter(newvalues);
    List sorted = sorter.getSortedRSVC();
    for (int i = 0; i < sorted.size(); ++i) {
      rsvc.addEntry((SubmittedValueEntry) sorted.get(i));
    }
  }

  public static String decodeAction(HashMap requestparams) {
    String[] actionmethods = (String[]) requestparams
        .get(SubmittedValueEntry.FAST_TRACK_ACTION);
    if (actionmethods != null) {
      String actionmethod = actionmethods[0];
      actionmethod = BeanUtil.stripEL(actionmethod);
      return actionmethod;
    }
    return null;
  }

}
