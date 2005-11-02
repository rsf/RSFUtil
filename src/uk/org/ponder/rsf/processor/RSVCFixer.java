/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.rsf.componentprocessor.BasicComponentSetter;
import uk.org.ponder.rsf.componentprocessor.ComponentProcessor;
import uk.org.ponder.rsf.componentprocessor.ComponentSetter;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.state.RequestSubmittedValueCache;
import uk.org.ponder.rsf.state.SubmittedValueEntry;
import uk.org.ponder.rsf.state.TokenRequestState;
import uk.org.ponder.rsf.state.TokenStateHolder;
import uk.org.ponder.rsf.util.CoreRSFMessages;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;

/** 
 * A ComponentProcessor which will do the work of setting the
 * value of a component to the value obtained from the RSVC stored at the
 * CURRENT view token. This is used to return the values of an erroneous submission
 * to the user for correction.
 * <br>
 * This is a request scope bean with init-method init().
 * 
 */

public class RSVCFixer implements ComponentProcessor {
  TokenRequestState cachedtrs;
  private RequestSubmittedValueCache rsvc;
  private ViewParameters viewparams;

  private TokenStateHolder tsholder;

  public void setTSHolder(TokenStateHolder errorhandler) {
    this.tsholder = errorhandler;
  }

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }

  public void init() {
    cachedtrs = tsholder.getTokenState(viewparams.viewtoken);
    if (cachedtrs == null) {
      Logger.log
          .info("INTERESTING EVENT!! User requested error state which has expired from the cache");

      ThreadErrorState.addError(new TargettedMessage(
          TargettedMessage.TARGET_NONE, CoreRSFMessages.EXPIRED_TOKEN));
    }
 
  }
  
  private ComponentSetter setter = new BasicComponentSetter();

  public void processComponent(UIComponent toprocess) {
    if (rsvc != null) {
      SubmittedValueEntry sve = rsvc.byID(toprocess.getFullID());
      if (sve != null) {
        setter.setValue(toprocess, sve.oldvalue);
      }
    }
  }

}
