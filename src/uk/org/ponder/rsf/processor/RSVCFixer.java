/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.errorutil.RequestSubmittedValueCache;
import uk.org.ponder.errorutil.SubmittedValueEntry;
import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.errorutil.TokenRequestState;
import uk.org.ponder.rsf.components.BasicComponentSetter;
import uk.org.ponder.rsf.components.ComponentProcessor;
import uk.org.ponder.rsf.components.ComponentSetter;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.util.CoreRSFMessages;
import uk.org.ponder.rsf.util.TokenStateHolder;
import uk.org.ponder.util.Copiable;
import uk.org.ponder.util.Logger;
import uk.org.ponder.webapputil.BlankViewParameters;
import uk.org.ponder.webapputil.ViewParameters;

// Returns a ComponentProcessor which will do the work of setting the
// value of a component to the value obtained from the RSVC stored at the
// CURRENT view token.

public class RSVCFixer implements ComponentProcessor, Copiable {
  TokenRequestState cachedtrs;
  private RequestSubmittedValueCache rsvc;
  private ViewParameters viewparams;

  public Object copy() {
    RSVCFixer togo = new RSVCFixer();
    togo.tsholder = tsholder;
    return togo;
  }

  private TokenStateHolder tsholder;

  public void setTSHolder(TokenStateHolder errorhandler) {
    this.tsholder = errorhandler;
  }

  public void setViewParameters(ViewParameters viewparams) {
// NB 2 hacks - first, avoidance of being dead bean, second, reliance that
// viewparams is the final dependency - tsholder is static. If we can think
// of some earthly way to make Spring work, or else discover how to read
// init-methods, this will go away.
    this.viewparams = viewparams;
    if (!(viewparams instanceof BlankViewParameters)) { 
      cachedtrs = tsholder.getTokenState(viewparams.viewtoken);
      if (cachedtrs == null) {
        Logger.log
            .info("INTERESTING EVENT!! User requested error state which has expired from the cache");

        ThreadErrorState.getErrorState().errors
            .addMessage(new TargettedMessage(TargettedMessage.TARGET_NONE,
                CoreRSFMessages.EXPIRED_TOKEN));
      }
      else {
        rsvc = cachedtrs.rsvc;
      }
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
