/*
 * Created on Nov 16, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.HashMap;
import java.util.Iterator;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.Logger;

/** If the target beans are serializable, then so will be the entry sent 
 * to the tokenstateholder. If so this state may be passed to a 
 * ClientFormTokenStateHolder or other similar - for Hibernate-style
 * non-serializable beans, they must be sent to an InMemoryTSH. But on the
 * other hand, these beans will need a Session.lock() called on them in any 
 * case, so need to derive from the BCPS for a HibernateBCPS.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class BeanCopyPreservationStrategy implements StatePreservationStrategy {
  private StringList beannames;
  private TokenStateHolder holder;

  private static class BeanCopyTokenState extends TokenState {
    HashMap beans = new HashMap();
  }

  public void setPreservingBeans(StringList beannames) {
    this.beannames = beannames;
  }
  
  public void setTokenStateHolder(TokenStateHolder holder) {
    this.holder = holder;
  }
  
  public void preserve(BeanLocator source, String tokenid) {
    BeanCopyTokenState bcts = new BeanCopyTokenState();
    bcts.tokenID = tokenid;
    
    for (int i = 0; i < beannames.size(); ++ i) {
      String beanname = beannames.stringAt(i);
      bcts.beans.put(beanname, source.locateBean(beanname));
    }
    holder.putTokenState(bcts);
  }

  public void restore(WriteableBeanLocator target, String tokenid) {
    BeanCopyTokenState bcts = (BeanCopyTokenState) holder.getTokenState(tokenid);
    if (bcts == null) {
      Logger.log.warn("Client requested restoration of expired flow state with ID " + tokenid);
    }
    else {
      for (Iterator keyit = bcts.beans.keySet().iterator(); keyit.hasNext();) {
        String beanname = (String) keyit.next();
        target.set(beanname, bcts.beans.get(beanname));
      }
    }    
  }

  public void clear(String tokenid) {
    holder.clearTokenState(tokenid);
  }

}
