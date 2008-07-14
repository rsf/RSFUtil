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
import uk.org.ponder.rsf.content.ContentTypeReporter;
import uk.org.ponder.rsf.view.DataInputHandler;
import uk.org.ponder.rsf.view.DataView;

/**
 * Collects all DataView beans from the context from both request and
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

  public boolean hasView(String viewID) {
    return viewMap.get(viewID) != null || inputMap.get(viewID) != null;
  }

  public DataView getView(String viewID) {
    return (DataView) fetchBean(viewMap, viewID);
  }

  public DataInputHandler getHandler(String viewID) {
    return (DataInputHandler) fetchBean(inputMap, viewID);
  }
  
  private Object fetchBean(Map map, String viewID) {
    Object got = map.get(viewID);
    if (got instanceof String) {
      got = rsacbl.getBeanLocator().locateBean((String) got);
    }
    return got;
  }
  
  private Class[] getExceptions(Class handlerclazz) {
    return handlerclazz == DataInputHandler.class? 
        new Class[]{ContentTypeReporter.class} : null;
  }
  
  public void init() {
    hooverRequestScope(DataView.class, viewMap);
    hooverRequestScope(DataInputHandler.class, inputMap);
  }
  
  private void hooverRequestScope(Class clazz, Map map) {
    String[] beans = rsacbl.beanNamesForClass(clazz);
    for (int i = 0; i < beans.length; ++i) {
      String beanname = beans[i];
      Class concreteclazz = rsacbl.getBeanClass(beanname);
      DataView blank = (DataView) reflectivecache.construct(concreteclazz);
      String key = viewInfoDistributor.distributeInfo(blank, getExceptions(clazz));
      map.put(key, beanname);
    }
  }

  // this is a map of viewInfoDistrubutor key objects to bean names or beans
  private Map viewMap = new HashMap();
  private Map inputMap = new HashMap();

  private void hooverAppScope(ApplicationContext applicationContext, Class clazz, Map map) {
    String[] viewbeans = applicationContext.getBeanNamesForType(clazz, false,
        false);
    for (int i = 0; i < viewbeans.length; ++i) {
      String beanname = viewbeans[i];
      Object producer = applicationContext.getBean(beanname);
      String key = viewInfoDistributor.distributeInfo(producer, getExceptions(clazz));
      map.put(key, producer);
    }
  }
  
  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    hooverAppScope(applicationContext, DataView.class, viewMap);
    hooverAppScope(applicationContext, DataInputHandler.class, inputMap);

  }

}
