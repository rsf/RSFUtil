/*
 * Created on 22 Jul 2006
 */
package uk.org.ponder.rsf.state.scope.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * A request-scope representative to allow any bean scope to be deleted via EL.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class ScopedBeanDestroyer implements WriteableBeanLocator,
    ApplicationContextAware {

  private ApplicationContext applicationContext;

  public boolean remove(String beanname) {
    try {
      ScopedBeanManager sbm = (ScopedBeanManager) applicationContext
          .getBean(beanname);
      sbm.destroy();
    }
    catch (Exception e) {
      throw UniversalRuntimeException.accumulate(e,
          "Failed to destroy bean scope named " + beanname);
    }
    return true;
  }

  public void set(String beanname, Object toset) {
  }

  public Object locateBean(String path) {
    return null;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

}
