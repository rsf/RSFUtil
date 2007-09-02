/*
 * Created on 2 Sep 2007
 */
package uk.org.ponder.rsf.flow.support;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.mapping.DARList;
import uk.org.ponder.mapping.DARReceiver;
import uk.org.ponder.mapping.DataAlterationRequest;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterceptor;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;

/** An interesting THING1 which does the work of allowing UIELBindings to be
 * applied directly to outgoing URL state from an action cycle. It uses the
 * intreresting strategy of being a DARReceiver, mapped to the path of the 
 * ARIResult, which intercepts requests to write to it and redirects them onto
 * itself, to be replayed later during ARI2 execution time. With this bean,
 * essentially all uses of ARI2 can actually be handled directly with bindings.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class ARIResultPenningReshaper implements DARReceiver, ActionResultInterceptor {

  private DARList pent = new DARList();
  private BeanModelAlterer darapplier;
  private TargettedMessageList targettedMessageList;

  public void setBeanModelAlterer(BeanModelAlterer darapplier) {
    this.darapplier = darapplier;
  }

  public boolean addDataAlterationRequest(DataAlterationRequest toadd) {
    pent.add(toadd);
    return true;
  }

  public void setTargettedMessageList(TargettedMessageList targettedMessageList) {
    this.targettedMessageList = targettedMessageList;
  }

  public void interceptActionResult(ARIResult result, ViewParameters incoming,
      Object actionReturn) {
    if (!targettedMessageList.isError()) {
      for (int i = 0; i < pent.size(); ++i) {
        DataAlterationRequest dar = pent.DARAt(i);
        try {
          darapplier.applyAlteration(result, dar, null);
        }
        catch (Exception e) {
          Logger.log.info("Error applying binding to outgoing URL state", e);
        }
      }
    }

  }

}
