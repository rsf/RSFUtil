/*
 * Created on Nov 23, 2004
 */
package uk.org.ponder.rsf.state;

import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.hashutil.EighteenIDGenerator;
import uk.org.ponder.rsf.request.RequestSubmittedValueCache;
import uk.org.ponder.rsf.request.SubmittedValueEntry;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;

/**
 * Holds the token-related state which is stored DURING a request. After
 * the request is completed, some of this information may be copied to a
 * TokenRequestState entry for the token, stored in the TokenStateHolder.
 * 
 * Keyed by a token which identifies a view that has been presented to the user.
 * The token is allocated afresh on each POST, and redirected to a GET view that
 * shares the same token. If the POST is non-erroneous, the rsvc can be blasted,
 * however if it is in error, it will be kept so that the values can be
 * resubmitted back to the user. We should CHANGE the old behaviour that carried
 * along the old token - we simply want to make a new token and erase the entry
 * for the old one.
 * 
 * When a further POST is made again from that view, it should mark that token
 * as USED, possibly by simply removing the values from the cache.
 * 
 * When the user tries to make a submission from an erased token, there should
 * be some (ideally) application-defined behaviour. But in general we don't have
 * much option but to erase their data, since it will probably conflict somehow.
 * 
 * NB RequestStateEntry is now a request-scope bean.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ErrorStateManager {
  /** The id of the submitting control responsible for this POST cycle, if there
  * is one. This will be used to target the error messages delivered to the
  * following RENDER cycle. */
  public String globaltargetid = null;
  // this field will be set if there is an incoming error state.
  public ErrorTokenState errorstate;

  private TokenStateHolder errortsholder;
  // for a GET request this will be set, but empty.
  private RequestSubmittedValueCache requestrsvc;
  private ViewParameters viewparams;

  public void setTSHolder(TokenStateHolder errortsholder) {
    this.errortsholder = errortsholder;
  }

  public void setViewParameters(ViewParameters viewparams) {
   this.viewparams = viewparams;
  }
  
  public void init() {
    if (viewparams.errortoken != null) {
      errorstate = (ErrorTokenState) errortsholder.getTokenState(viewparams.errortoken);
      if (errorstate == null) {
        Logger.log.warn("Client requested error state " + viewparams.errortoken
            + " which has expired from the cache");
      }
    } 
  }
  
  public void setRequestRSVC(RequestSubmittedValueCache requestrsvc) {
    this.requestrsvc = requestrsvc;
  }

  private static EighteenIDGenerator idgenerator = new EighteenIDGenerator();

  public String allocateToken() {
    return idgenerator.generateID();
  }
  

  public String allocateOutgoingToken() {
    if (errorstate == null) {
      errorstate = new ErrorTokenState();
      errorstate.tokenID = viewparams.errortoken == null? allocateToken() : viewparams.errortoken;
    }
    return errorstate.tokenID;
  }

  // Convert errors which are currently referring to bean paths back onto
  // their fields as specified in RSVC. Called at the END of a POST cycle.
  private void fixupErrors(TargettedMessageList tml,
      RequestSubmittedValueCache rsvc) {
    for (int i = 0; i < tml.size(); ++i) {
      TargettedMessage tm = tml.messageAt(i);
      if (!tm.targetid.equals(TargettedMessage.TARGET_NONE)) {
        // Target ID refers to bean path. We need somewhat more flexible
        // rules for locating a component ID, which is defined as something
        // which follows "message-for". These IDs may actually be "synthetic",
        // at a particular level of containment, in that they refer to a specially
        // instantiated genuine component which has the same ID.
        SubmittedValueEntry sve = rsvc.byPath(tm.targetid);
        if (sve == null) {
          Logger.log.warn("Message queued for non-component path " + tm.targetid);
        }
        else {
          String id = tm.targetid = sve.componentid;
        }
      }
      // We desire TMs stored between cycles are "trivially" serializable, any
      // use of the actual exception object should be finished by action end.
      tm.exception = null;
    }
  }

  /**
   * Signal that all errors for this request cycle have been accumulated into
   * the current ese. It will now be cached in the tokenrequeststate for
   * reference by further requests, under the OUTGOING token ID, and cleared
   * from the current thread.
   * 
   * @return The error token ID to be used for the outgoing request, or null
   * if there is no error.
   */
  public String requestComplete() {
    try {
      if (ThreadErrorState.isError()) {
        TargettedMessageList errors = ThreadErrorState.getErrorState().errors;
        // the errors arose from this cycle, and hence must be referred to
        // by SVEs from this cycle. If it is a GET cycle, rsvc will be empty,
        // but then all errors will have global target.
        fixupErrors(errors, requestrsvc);

        allocateOutgoingToken();
        errorstate.globaltargetid = globaltargetid;
        errorstate.rsvc = requestrsvc;
        errorstate.errors = errors;
        errortsholder.putTokenState(errorstate.tokenID, errorstate);
        return errorstate.tokenID;
      }
      else {
        if (errorstate != null) {
          errortsholder.clearTokenState(errorstate.tokenID);
        }
        return null;
      }
    }
    finally {
      ThreadErrorState.endRequest();
    }

  }

}
