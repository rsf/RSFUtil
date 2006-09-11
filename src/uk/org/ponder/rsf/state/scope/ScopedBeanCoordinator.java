/*
 * Created on 22 Jul 2006
 */
package uk.org.ponder.rsf.state.scope;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import sun.misc.GC;
import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.rsf.preservation.AutonomousStatePreservationStrategy;
import uk.org.ponder.rsf.preservation.BeanCopyPreservationStrategy;
import uk.org.ponder.rsf.preservation.ExactBeanCopyPreservationStrategy;
import uk.org.ponder.rsf.preservation.GenericBeanCopyPreservationStrategy;
import uk.org.ponder.rsf.preservation.TSHPreservationStrategy;
import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.stringutil.StringGetter;
import uk.org.ponder.stringutil.StringList;

/**
 * The central manager of ScopedBeanManagers - will be one per application
 * context.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class ScopedBeanCoordinator implements ApplicationContextAware,
    AutonomousStatePreservationStrategy {

  private BeanModelAlterer alterer;
  private ScopedBeanManager[] managers;
  private TSHPreservationStrategy[] strategies;
  private Map destroyed;
  private StringGetter requesttypeproxy;

  public void setBeanModelAlterer(BeanModelAlterer alterer) {
    this.alterer = alterer;
  }

  public void setRequestTypeProxy(StringGetter requesttypeproxy) {
    this.requesttypeproxy = requesttypeproxy;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    String[] mannames = applicationContext.getBeanNamesForType(
        ScopedBeanManager.class, false, false);
    strategies = new TSHPreservationStrategy[mannames.length];
    managers = new ScopedBeanManager[mannames.length];
    for (int i = 0; i < mannames.length; ++i) {
      final ScopedBeanManager sbm = (ScopedBeanManager) applicationContext
          .getBean(mannames[i]);
      StringList targetkeys = sbm.getTargetPreservingKeyList();
      GenericBeanCopyPreservationStrategy gbcps = null;
      if (targetkeys != null) {
        ExactBeanCopyPreservationStrategy ebcps = new ExactBeanCopyPreservationStrategy();
        ebcps.setTargetPreservingKeys(sbm.getTargetPreservingKeyList());
        gbcps = ebcps;
      }
      else {
        BeanCopyPreservationStrategy bcps = new BeanCopyPreservationStrategy();
        bcps.setStorageExpected(false);
        gbcps = bcps;
      }
      gbcps.setBeanModelAlterer(alterer);
      gbcps.setTokenStateHolder(sbm.getTokenStateHolder());
      gbcps.setPreservingBeans(sbm.getCopyPreservingBeanList());
      final GenericBeanCopyPreservationStrategy fgbcps = gbcps;
      sbm.setBeanDestroyer(new BeanDestroyer() {
        public void destroy() {
          String scopeName = sbm.getScopeName();
          fgbcps.clear(scopeName);
          destroyed.put(scopeName, scopeName);
        } 
      });
      
      strategies[i] = gbcps;
      managers[i] = sbm;
    }
  }

  public void setDestroyedScopeMap(Map destroyed) {
    this.destroyed = destroyed;
  }

  public StringList restore(WriteableBeanLocator target) {
    StringList togo = new StringList();
    for (int i = 0; i < managers.length; ++i) {
      String scopename = managers[i].getScopeName();
      int restored = strategies[i].restore(target, scopename);
      if (restored != 0) {
        String tshkey = strategies[i].getTokenStateHolder().getId();
        if (managers[i].getExclusive()) {
          togo.add(tshkey + "-" + scopename);
        }
      }
    }
    return togo;
  }

  public void preserve(BeanLocator source) {
    String requesttype = requesttypeproxy.get();
    for (int i = 0; i < managers.length; ++i) {
      String scopename = managers[i].getScopeName();
      // preserve *if* it was not destroyed, and *if* it is a render request,
      // ONLY if the manager was set to "always preserve"
      if (!destroyed.containsKey(scopename)
          && (!requesttype.equals(EarlyRequestParser.RENDER_REQUEST) 
              || managers[i].getAlwaysPreserve())) {
        strategies[i].preserve(source, scopename);
      }
    }
  }

}
