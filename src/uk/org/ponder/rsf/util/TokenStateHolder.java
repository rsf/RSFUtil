/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.util;

import java.util.HashMap;

import uk.org.ponder.errorutil.RequestStateEntry;
import uk.org.ponder.errorutil.RequestSubmittedValueCache;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.errorutil.TokenRequestState;
import uk.org.ponder.hashutil.EighteenIDGenerator;
import uk.org.ponder.webapputil.ViewParameters;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class TokenStateHolder {
 
  // a map of String error token IDs to ErrorStateEntries.
// the idea is that this be a timeout cache which is cleared out every
// hour or so... all this functionality to be moved to another class.
  private HashMap errorcache = new HashMap();
  
  public TokenRequestState getTokenState(String tokenID) {
    return (TokenRequestState) errorcache.get(tokenID);
  }
  
  /**
   * Signal that all errors for this request cycle have been accumulated into
   * the current ese. It will now be cached in the errorcache for reference by
   * further requests, under the OUTGOING token ID, 
   * and cleared from the current thread.
   */
  public void errorAccumulationComplete(RequestSubmittedValueCache rsvc) {
    RequestStateEntry ese = ThreadErrorState.getErrorState();
    
    TokenRequestState trs = new TokenRequestState();
    trs.rsvc = rsvc;
    trs.ese = ese;
    errorcache.put(ese.outgoingtokenID, ese);
    ThreadErrorState.getErrorState().errors.clear();
  }
}
