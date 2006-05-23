/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.org.ponder.rsf.renderer.RenderSystemDecoder;
import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.rsf.request.FossilizedConverter;
import uk.org.ponder.rsf.request.RequestSubmittedValueCache;
import uk.org.ponder.rsf.request.SVESorter;
import uk.org.ponder.rsf.request.SubmittedValueEntry;
import uk.org.ponder.util.Logger;

/**
 * A request-scope bean whose job is to determine the (topologically sorted) set
 * of submitted request submitted values for this POST request. If this is a
 * RENDER request, returns an empty list.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class PostDecoder {
  private FossilizedConverter fossilizedconverter;
  private RenderSystemDecoder rendersystemdecoder;
  private Map requestparams;
  private String requesttype;
  private Map normalizedrequest;
  private RequestSubmittedValueCache requestrsvc;

  public void setFossilizedConverter(FossilizedConverter fossilizedconverter) {
    this.fossilizedconverter = fossilizedconverter;
  }

  public void setRenderSystemDecoder(RenderSystemDecoder rendersystemdecoder) {
    this.rendersystemdecoder = rendersystemdecoder;
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
    rendersystemdecoder.normalizeRequestMap(normalizedrequest);
  }

  // This method is expected to be called by accreteRSVC
  public void parseRequest(RequestSubmittedValueCache rsvc) {
   // Firstly acquire all non-component ("pure") bindings
    for (Iterator keyit = normalizedrequest.keySet().iterator(); keyit
        .hasNext();) {
      String key = (String) keyit.next();
      String[] values = (String[]) normalizedrequest.get(key);
      Logger.log.info("PostInit: key " + key + " value " + values[0]);
      if (fossilizedconverter.isNonComponentBinding(key)) {
        for (int i = 0; i < values.length; ++ i) {
          // EL binding key is fixed, there may be many values
          SubmittedValueEntry sve = fossilizedconverter.parseBinding(key,
            values[i]);
          rsvc.addEntry(sve);
          Logger.log.info("Discovered noncomponent binding for "
              + sve.valuebinding + " rvalue " + sve.newvalue);
        }
      }
      // Secondly assess whether this was a component fossilised binding.
      else if (fossilizedconverter.isFossilisedBinding(key)
          && values.length > 0) {
        SubmittedValueEntry sve = fossilizedconverter.parseFossil(key,
            values[0]);

        // Grab dependent values which we can now deduce may be in the request
        String[] newvalue = (String[]) normalizedrequest.get(sve.componentid);
        sve.newvalue = newvalue;
        fossilizedconverter.fixupNewValue(sve, rendersystemdecoder, key,
            values[0]);
        
        String[] reshaper = 
          (String[]) normalizedrequest.get(fossilizedconverter.getReshaperKey(sve.componentid));
        if (reshaper != null) {
          sve.reshaperbinding = reshaper[0];
        }
        
        Logger.log.info("Discovered fossilised binding for " + sve.valuebinding
            + " for component " + sve.componentid + " with old value "
            + sve.oldvalue);
        rsvc.addEntry(sve);
      }
    }
  }

  public RequestSubmittedValueCache getRequestRSVC() {
    if (requestrsvc == null) {
      requestrsvc = new RequestSubmittedValueCache();
      if (requesttype.equals(EarlyRequestParser.ACTION_REQUEST)) {
        RequestSubmittedValueCache newvalues = new RequestSubmittedValueCache();
        parseRequest(newvalues);
        // Topologically sort the fresh values (which may be arbitrarily
        // disordered through passing the request) in order of intrinsic
        // dependency.
        SVESorter sorter = new SVESorter(newvalues);
        List sorted = sorter.getSortedRSVC();
     
        for (int i = 0; i < sorted.size(); ++i) {
          requestrsvc.addEntry((SubmittedValueEntry) sorted.get(i));
        }
      }
    }
    return requestrsvc;
  }

  public Map getNormalizedRequest() {
    return normalizedrequest;
  }

  public static String decodeAction(Map normalizedrequest) {
    String[] actionmethods = (String[]) normalizedrequest
        .get(SubmittedValueEntry.FAST_TRACK_ACTION);
    if (actionmethods != null) {
      String actionmethod = actionmethods[0];
      //actionmethod = BeanUtil.stripEL(actionmethod);
      return actionmethod;
    }
    return null;
  }

  public static String decodeSubmittingControl(Map normalized) {
    // This SHOULD be set if an RSF submitting response is required
    String[] array = (String[]) normalized
        .get(SubmittedValueEntry.SUBMITTING_CONTROL);
    return array == null? null : array[0];
  }

}
