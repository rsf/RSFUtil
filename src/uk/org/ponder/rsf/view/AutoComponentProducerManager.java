/*
 * Created on 27-Feb-2006
 */
package uk.org.ponder.rsf.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.rsac.RSACBeanLocator;

/**
 * Manages a set of beans representing view producers. The slightly unusual
 * approach adopted for request-scope producers is recommended by all sorts of
 * considerations of user convenience - we would like implementors of request
 * scope view producers to i) simply implement normal request-scope beans ii)
 * not have any of them instantiated except for the ones required for a request,
 * and iii) signal their capabilities through the selection of interfaces they
 * implement. <br>
 * All of these considerations militate against the use of some kind of AOP
 * technique, which would become quite cumbersome, and also tedious to make
 * efficient. <br>
 * Instead, this class internally maintains a hash of application-scope "blank"
 * producers that are dispensed to users (the framework) - these will answer to
 * all application-scope and other static queries per contract - however, when
 * the users want to invoke the production method, they must first make a call
 * to wrapProducer - for an application scope producer, this simply returns the
 * original bean. <br>
 * This bean ALSO scours the application context similarly for application
 * scope ViewProducers in the straightforward way.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */

public class AutoComponentProducerManager implements ComponentProducerWrapper,
    ApplicationContextAware {

  private RSACBeanLocator rsacbl;
  private ReflectiveCache reflectivecache;

  public void setRSACBeanLocator(RSACBeanLocator rsacbl) {
    this.rsacbl = rsacbl;
  }

  public void setReflectiveCache(ReflectiveCache reflectivecache) {
    this.reflectivecache = reflectivecache;
  }

  public void init() {
    String[] viewbeans = rsacbl.beanNamesForClass(ComponentProducer.class);
    for (int i = 0; i < viewbeans.length; ++i) {
      String beanname = viewbeans[i];
      Class clazz = rsacbl.getBeanClass(beanname);
      // this strategy currently assumes uniqueness on bean classes - could be
      // upgraded to something more subtle, but where would we get the context
      // from?
      ComponentProducer blank = (ComponentProducer) reflectivecache
          .construct(clazz);
      componentMap.put(blank, beanname);
      if (blank instanceof ViewComponentProducer) {
        advertised.add(blank);
      }
    }
  }

  // this is a map of "blank" component objects to bean names.
  private Map componentMap = new HashMap();

  private List advertised = new ArrayList();

  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    String[] viewbeans = applicationContext
        .getBeanNamesForType(ViewComponentProducer.class, false, false);
    for (int i = 0; i < viewbeans.length; ++i) {
      String beanname = viewbeans[i];
      ViewComponentProducer producer = (ViewComponentProducer) applicationContext
          .getBean(beanname);
      advertised.add(producer);
    }

  }

  /**
   * For a request-scope "blank" producer, fetches and returns the corresponding
   * request-scope bean. For any other producer, returns the argument unchanged.
   */

  public ComponentProducer wrapProducer(ComponentProducer towrap) {
    String beanname = (String) componentMap.get(towrap);
    ComponentProducer togo = towrap;
    if (beanname != null) {
      togo = (ComponentProducer) rsacbl.getBeanLocator().locateBean(beanname);
    }
    return togo;
  }

  /**
   * Returns the list of "advertised" producers. These will be the ones of type
   * ViewComponentProducer found at either application or request scope. This
   * differs from the set which can be "translated" by wrapProducer, which is
   * the set of ALL producers found at request scope.
   * 
   * @return
   */
  public Collection getProducers() {
    return advertised;
  }

}
