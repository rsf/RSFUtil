/*
 * Created on Nov 16, 2005
 */
package uk.org.ponder.rsf.preservation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.BeanNameAware;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.beanutil.IterableBeanLocator;
import uk.org.ponder.beanutil.PathUtil;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.rsf.state.ExpiredFlowException;
import uk.org.ponder.rsf.state.TokenStateHolder;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * If the target beans are serializable, then so will be the entry sent to the
 * tokenstateholder. If so this state may be passed to a
 * ClientFormTokenStateHolder or other similar - for Hibernate-style
 * non-serializable beans, they must be sent to an InMemoryTSH. But on the other
 * hand, these beans will need a Session.lock() called on them in any case, so
 * need to derive from the BCPS for a HibernateBCPS.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class BeanCopyPreservationStrategy implements StatePreservationStrategy,
    BeanNameAware {
  private StringList beannames;
  private TokenStateHolder holder;
  private String ourbeanname;
  private BeanModelAlterer alterer;

  public void setPreservingBeans(StringList beannames) {
    this.beannames = beannames;
  }

  public void setTokenStateHolder(TokenStateHolder holder) {
    this.holder = holder;
  }

  public void setBeanModelAlterer(BeanModelAlterer alterer) {
    this.alterer = alterer;
  }
  
  public void preserve(BeanLocator source, String tokenid) {
    HashMap beans = new HashMap();
    for (int i = 0; i < beannames.size(); ++i) {
      String beanname = beannames.stringAt(i);
      Object bean = alterer.getBeanValue(beanname, source);
      // This branch generally useful for entity bean locators
      if (bean instanceof IterableBeanLocator) {
        IterableBeanLocator iterablebean = (IterableBeanLocator) bean;
        for (Iterator beanit = iterablebean.iterator(); beanit.hasNext(); ) {
          String subbeanname = (String) beanit.next();
          Object subbean = iterablebean.locateBean(subbeanname);
          String fullpath = PathUtil.composePath(beanname, subbeanname);
          beans.put(fullpath, subbean);
        }
      }
      if (bean != null) {
        beans.put(beanname, bean);
        Logger.log.info("BeanCopy preserved to path " + beanname + ": " + bean);   
      }
    }
    String token = ourbeanname + tokenid;
    holder.putTokenState(token, beans);
    Logger.log.info("BeanCopy saved " + beans.size() + " beans to token " + token);
  }

  public void restore(WriteableBeanLocator target, String tokenid) {
    String token = ourbeanname + tokenid;
    Logger.log.info("BeanCopy looking for state token " + token);
    Map beans = (Map) holder.getTokenState(token);
    if (beans == null) {
      throw UniversalRuntimeException.accumulate(new ExpiredFlowException(),
          "Client requested restoration of expired flow state with ID "
              + tokenid);
    }
    else {
      for (Iterator keyit = beans.keySet().iterator(); keyit.hasNext();) {
        String beanname = (String) keyit.next();
        Object bean = beans.get(beanname);
        alterer.setBeanValue(beanname, target, bean);
      }
    }
  }

  public void clear(String tokenid) {
    holder.clearTokenState(ourbeanname + tokenid);
  }

  public void setBeanName(String name) {
    this.ourbeanname = name;
  }

}
