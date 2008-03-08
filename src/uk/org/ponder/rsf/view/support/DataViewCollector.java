/*
 * Created on 7 Mar 2008
 */
package uk.org.ponder.rsf.view.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.rsf.view.DataView;

/** Collects all DataView beans from the context from both request and
 * application scope, and distributes information they report.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class DataViewCollector implements ApplicationContextAware {

  private RSACBeanLocator rsacbl;
  private ReflectiveCache reflectivecache;
  private ViewInfoDistributor viewInfoDistributor;

  public void setViewInfoDistributor(ViewInfoDistributor viewInfoDistributor) {
    this.viewInfoDistributor = viewInfoDistributor;
  }

  public void setRSACBeanLocator(RSACBeanLocator rsacbl) {
    this.rsacbl = rsacbl;
  }
  
  public void setReflectiveCache(ReflectiveCache reflectivecache) {
    this.reflectivecache = reflectivecache;
  }
  
  public DataView getView(String viewID) {
    Object got = viewMap.get(viewID);
    if (got instanceof String) {
      got = rsacbl.getBeanLocator().locateBean((String) got);
    }
    return (DataView) got;
  }
  
  public void init() {
    String[] viewbeans = rsacbl.beanNamesForClass(DataView.class);
    for (int i = 0; i < viewbeans.length; ++i) {
      String beanname = viewbeans[i];
      Class clazz = rsacbl.getBeanClass(beanname);
      DataView blank = (DataView) reflectivecache.construct(clazz);
      String key = viewInfoDistributor.distributeInfo(blank);
      viewMap.put(key, beanname);
    }
  }

  // this is a map of "blank" component objects to bean names.
  private Map viewMap = new HashMap();

  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    String[] viewbeans = applicationContext
        .getBeanNamesForType(DataView.class, false, false);
    for (int i = 0; i < viewbeans.length; ++i) {
      String beanname = viewbeans[i];
      DataView producer = (DataView) applicationContext.getBean(beanname);
      String key = viewInfoDistributor.distributeInfo(producer);
      viewMap.put(key, producer);
    }

  }

}
