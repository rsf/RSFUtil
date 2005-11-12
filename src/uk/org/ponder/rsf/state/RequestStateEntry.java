/*
 * Created on Nov 23, 2004
 */
package uk.org.ponder.rsf.state;

import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.hashutil.EighteenIDGenerator;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * Represents the token-related state which is stored DURING a request.
 * After the request is completed, some of this information may be
 * copied to a TokenRequestState entry for the token, stored in the 
 * TokenStateHolder.
 * 
 * 
 * Keyed by a token which identifies a view that has been presented to the user.
 * The token is allocated afresh on each POST, and redirected to a GET view
 * that shares the same token. If the POST is non-erroneous, the rsvc can
 * be blasted, however if it is in error, it will be kept so that the values
 * can be resubmitted back to the user. We should CHANGE the old behaviour
 * that carried along the old token - we simply want to make a new
 * token and erase the entry for the old one.
 * 
 * When a further POST is made again from that view,  
 * it should mark that token as USED, possibly by simply removing the values
 * from the cache.
 * 
 * When the user tries to make a submission from an erased token, there
 * should be some (ideally) application-defined behaviour. But in general
 * we don't have much option but to erase their data, since it will probably
 * conflict somehow. 
 * 
 * NB RequestStateEntry is now a request-scope bean.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RequestStateEntry {
  public String globaltargetid = null;
  
  public String incomingtokenID;
  public String outgoingtokenID;


  private TokenStateHolder tsholder;

  public void setTSHolder(TokenStateHolder errorhandler) {
    this.tsholder = errorhandler;
  }
  
  public TokenStateHolder getTSHolder() {
    return tsholder;
  }
  
  private static EighteenIDGenerator idgenerator = new EighteenIDGenerator();
   
  public void setViewParameters(ViewParameters viewparams) {
    incomingtokenID = viewparams == null? null : viewparams.viewtoken;
    outgoingtokenID = idgenerator.generateID();
  }

  // rewrites any queued errors with no target to refer to the
  // specified target.
  public void setGlobalTarget(String globalid) {
    globaltargetid = globalid;
  }
  
//Convert errors which are currently referring to bean paths back onto
  // their fields as specified in RSVC.
  private void fixupErrors(TargettedMessageList tml, 
      RequestSubmittedValueCache rsvc) {
    for (int i = 0; i < tml.size(); ++i) {
      TargettedMessage tm = tml.messageAt(i);
      if (!tm.targetid.equals(TargettedMessage.TARGET_NONE)) {
        String id = rsvc.byPath(tm.targetid).componentid;
        tm.targetid = id;
      }
    }
  }
  
  /**
   * Signal that all errors for this request cycle have been accumulated into
   * the current ese. It will now be cached in the tokenrequeststate for reference by
   * further requests, under the OUTGOING token ID, 
   * and cleared from the current thread.
   * @return The converted TokenRequestState object ready to be stored in the
   * global cache.
   */
  public void requestComplete(RequestSubmittedValueCache rsvc) {
    TargettedMessageList errors = ThreadErrorState.getErrorState().errors;
    TokenRequestState trs = new TokenRequestState();
    if (errors.size() > 0) {
      fixupErrors(errors, rsvc);
    }
    else {
      // unless there were errors, the rsvc does not need to be stored.
      rsvc = null;
    }
    trs.rsvc = rsvc;
    trs.errors = errors;
   
    ThreadErrorState.clearState();
    tsholder.putTokenState(trs);
  }
  
}
