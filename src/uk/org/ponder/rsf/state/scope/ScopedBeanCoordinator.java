/*
 * Created on 22 Jul 2006
 */
package uk.org.ponder.rsf.state.scope;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.rsf.preservation.AutonomousStatePreservationStrategy;
import uk.org.ponder.rsf.preservation.BeanCopyPreservationStrategy;
import uk.org.ponder.rsf.preservation.StatePreservationStrategy;

/** The central manager of ScopedBeanManagers - will be one per 
 * application context. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class ScopedBeanCoordinator implements ApplicationContextAware, 
  AutonomousStatePreservationStrategy {

  private BeanModelAlterer alterer;
  private StatePreservationStrategy[] strategies;
  private String[] scopenames;
  private Map destroyed;

  public void setBeanModelAlterer(BeanModelAlterer alterer) {
    this.alterer = alterer;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    String[] mannames = applicationContext.getBeanNamesForType(
        ScopedBeanManager.class, false, true);
    strategies = new StatePreservationStrategy[mannames.length];
    scopenames = new String[mannames.length];
    for (int i = 0; i < mannames.length; ++i) {
      ScopedBeanManager sbm = (ScopedBeanManager) applicationContext
          .getBean(mannames[i]);
      BeanCopyPreservationStrategy bcps = new BeanCopyPreservationStrategy();
      bcps.setBeanModelAlterer(alterer);
      bcps.setTokenStateHolder(sbm.getTokenStateHolder());
      bcps.setPreservingBeans(sbm.getCopyPreservingBeanList());
      bcps.setStorageExpected(false);
      strategies[i] = bcps;
      scopenames[i] = mannames[i];
    }
  }

  public void setDestroyedScopeMap(Map destroyed) {
    this.destroyed = destroyed;
  }
  
  public void restore(WriteableBeanLocator target) {
    for (int i = 0; i < scopenames.length; ++i) {
      strategies[i].restore(target, scopenames[i]);
    }
  }

  public void preserve(BeanLocator source) {
    for (int i = 0; i < scopenames.length; ++i) {
      String scopename = scopenames[i];
      if (!destroyed.containsKey(scopename)) {
        strategies[i].preserve(source, scopenames[i]);
      }
    }
  }

}
