/*
 * Created on 21 May 2007
 */
package uk.org.ponder.rsf.viewstate;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.util.Logger;

/** Receives and coordinates view parameters information from the ViewProducers,
 * as well as contributing the default view marked there.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class DefaultViewInfoReceiver implements ViewParamsReceiver, ViewParamsReporter {
  // a concurrent map to be used when live
  private Map exemplarmap;
  
  // A single-threaded hashmap to be used during startup.
  private Map pendingmap = new HashMap();

  private String defaultview;
  // Called from ConcreteViewResolver.init(), i.e. "somewhat late"
  public void setDefaultView(String viewid) {
    defaultview = viewid;
  }
  
 
  public void init() {
    exemplarmap.putAll(pendingmap);
    pendingmap = null;
  }
  
  public void setReflectiveCache(ReflectiveCache reflectivecache) {
    exemplarmap = reflectivecache.getConcurrentMap(1);
  }
  
  /**
   * DO NOT call this method in application scope. Default view cannot be
   * guaranteed to be called until all views are read.
   * 
   * @return The default view to be redirected to in case of a Level-1
   *         exception.
   */
  public String getDefaultView() {
    if (defaultview == null) {
      Logger.log
          .warn("Warning: no view has been marked as default during initialisation -"
              + "\nDid you remember to define ViewProducers?");
    }
    return "/" + defaultview;
  }
  
  private ViewParameters defaultexemplar = new SimpleViewParameters();

  
  public void setDefaultExemplar(ViewParameters defaultexemplar) {
    this.defaultexemplar = defaultexemplar;
  }
  
  /**
   * AT MOST ONE view parameters map may be set as "pending" during startup
   * (presumably through reading of a static file). It will be actioned on
   * initialisation of the bean, all further will be actioned immediately.
   */
  public void setViewParametersMap(Map exemplarmap) {
    (this.exemplarmap == null ? pendingmap
        : this.exemplarmap).putAll(exemplarmap);
  }

  public ViewParameters getViewParamsExemplar(String viewid) {
    ViewParameters togo = (ViewParameters) exemplarmap.get(viewid);
    return togo == null? defaultexemplar : togo;
  }
  
  public void setViewParamsExemplar(String viewid, ViewParameters vpexemplar) {
    if (vpexemplar != null) {
      (exemplarmap == null ? pendingmap
          : exemplarmap).put(viewid, vpexemplar);
    }
  }
  
  public ViewParameters getViewParameters() {
    if (defaultview == null) {
      Logger.log
          .warn("Warning: no view has been marked as default during initialisation -"
              + "\nDid you remember to define ViewProducers?");
      return null;
    }
    else {
      ViewParameters togo = getViewParamsExemplar(defaultview).copyBase();
      togo.viewID = defaultview;
      return togo;
    }
  
  }

}
