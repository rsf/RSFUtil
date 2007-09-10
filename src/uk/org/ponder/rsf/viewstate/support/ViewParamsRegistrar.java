/*
 * Created on 18 Jul 2007
 */
package uk.org.ponder.rsf.viewstate.support;

import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.rsf.viewstate.SiteMap;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReceiver;

/** A utility class to allow views without producers to easily register
 * their ViewParameters types using a Spring definition (alternative was the
 * use of {@link SiteMap}).
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class ViewParamsRegistrar {
  private ViewParamsReceiver vpreceiver;
  private ReflectiveCache reflectiveCache;

  public void setReflectiveCache(ReflectiveCache reflectiveCache) {
    this.reflectiveCache = reflectiveCache;
  }

  public void setViewID(String viewID) {
    this.viewID = viewID;
  }

  public void setViewParamsClass(Class viewParamsClass) {
    this.viewParamsClass = viewParamsClass;
  }

  public void setViewParametersReceiver(ViewParamsReceiver vpreceiver) {
    this.vpreceiver = vpreceiver;
  }
  
  private String viewID;
  
  private Class viewParamsClass;
  
  public void init() {
    ViewParameters viewparams = (ViewParameters) reflectiveCache.construct(viewParamsClass); 
    vpreceiver.setViewParamsExemplar(viewID, viewparams);
  }
}
