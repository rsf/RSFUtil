/*
 * Created on Nov 14, 2005
 */
package uk.org.ponder.rsf.viewstate;


import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.util.Logger;

/** A simple parser of view parameters, which will parse into clones of
 * supplied "exemplar" objects. 
 * The lookup will be performed on the first segment
 * of the pathinfo (up to first slash), which will be assumed to represent
 * the viewID.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class BasicViewParametersParser implements ViewParametersParser, ViewParamsReceiver {
  private Map exemplarmap;
  private BeanModelAlterer bma;
  
  // A single-threaded hashmap to be used during startup.
  private Map pendingmap = new HashMap();
  
  private ViewParameters defaultexemplar = new SimpleViewParameters();
  
  private String defaultview;
  

  public void setDefaultView(String viewid) {
    defaultview = viewid;
  }
  /** DO NOT call this method in application scope. Default view cannot
   * be guaranteed to be called until all views are read.
   * @return The default view to be redirected to in case of a Level-1 exception.
   */
  public String getDefaultView() {
    if (defaultview == null) {
      Logger.log.warn("Warning: no view has been marked as default during initialisation -"
          +"\nDid you remember to define ViewProducers?");
    }
    return "/"+defaultview;
  }
/** AT MOST ONE view parameters map may be set as "pending" during startup 
 * (presumably through reading of a static file). It will be actioned on 
 * initialisation of the bean, all further will be actioned immediately.
 */
  public void setViewParametersMap(Map exemplarmap) {
    (this.exemplarmap == null? pendingmap : this.exemplarmap).putAll(exemplarmap);
  }
  
  public void setViewParamsExemplar(String viewid, ViewParameters vpexemplar) {
    if (vpexemplar != null) {
      (exemplarmap == null? pendingmap : exemplarmap).put(viewid, vpexemplar);
    }
  }
  
  public void setBeanModelAlterer(BeanModelAlterer bma) {
    this.bma = bma;
  }
  
  public void setReflectiveCache(ReflectiveCache reflectivecache) {
    exemplarmap = reflectivecache.getConcurrentMap(1);
  }

  public void init() {
    exemplarmap.putAll(pendingmap);
    pendingmap = null;
  }
  
  public void setDefaultExemplar(ViewParameters defaultexemplar) {
    this.defaultexemplar = defaultexemplar;
  }
  
  public ViewParameters parse(String pathinfo, Map requestmap) {
    // JSF memorial comment:
    // restoreView is the very first of the ViewHandler methods to be called for
    // each request, and it is guaranteed to be called. We take this opportunity
    // to stash away a parsed parameter object corresponding to our original
    // request.
    int firstslashpos = pathinfo.indexOf('/', 1);
    String viewID = firstslashpos == -1? pathinfo.substring(1) :
      pathinfo.substring(1, firstslashpos);
      
    ViewParameters vpexemplar = (ViewParameters) exemplarmap.get(viewID);
    if (vpexemplar == null) {
      vpexemplar = defaultexemplar;
    }
    ViewParameters origrequest = vpexemplar.copyBase();
    ViewParamUtil.parseViewParamAttributes(bma, origrequest, requestmap);
    origrequest.parsePathInfo(pathinfo);

    // Map requestmap = req.
    // requestmap.put(ViewParameters.CURRENT_REQUEST, origrequest);
    Logger.log.info("Restoring view " + origrequest.viewID
        + " from request parameters " + ViewParamUtil.toHTTPRequest(bma, origrequest));
    return origrequest;

  }


}
