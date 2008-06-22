/*
 * Created on 22 Jun 2008
 */
package uk.org.ponder.rsf.viewstate.support;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;

public class ViewParamsValidator {
  private ViewParamsMappingInfoManager vpmim;
  private BeanModelAlterer bma;
  private boolean productionMode;

  // A public repository of errors, to be used in test environments
  public Map errors = new HashMap();
  
  public void setProductionMode(boolean productionMode) {
    this.productionMode = productionMode;
  }

  public void setVPMappingInfoManager(ViewParamsMappingInfoManager vpmim) {
    this.vpmim = vpmim;
  }
  
  public void setBeanModelAlterer(BeanModelAlterer bma) {
    this.bma = bma;
  }
  
  
  public void validate(String viewID, ViewParameters viewparams) {
    ConcreteViewParamsMapInfo vpmi = vpmim.getMappingInfo(viewparams);
    
    for (int i= 0; i < vpmi.paths.length; ++ i) {
      validatePath(viewID, viewparams, vpmi.paths[i]);
      if (vpmi.paths[i].equals("")) {
        reportError("Path is reported empty for attribute with name " 
            + vpmi.attrnames[i] + ":\n   did you swap round attribute and path around colon? (path should come after colon)",
            viewID, viewparams, null);
      }
    }
    
    for (int i = 0; i < vpmi.trunkpaths.length; ++ i) {
      validatePath(viewID, viewparams, vpmi.trunkpaths[i]);
      if (vpmi.trunkpaths[i].equals("")) {
        reportError("Path is reported empty for trunk path at index " + i, 
            viewID, viewparams, null);
      }
    }    
  }

  private void reportError(String extraMess, String viewID, ViewParameters viewparams, Exception e) {

    String message = "Invalid parseSpec for ViewParameters of " + viewparams.getClass() + extraMess;
    // In production mode we simply capture and report errors.
    if (productionMode) {
      errors.put(viewparams.getClass(), message);
      if (e != null) {
        Logger.log.error(message, e);
      }
      else {
        Logger.log.error(message);
      }
    }
    else {
      throw new IllegalArgumentException(message);
    }
    
  }

  private void validatePath(String viewID, ViewParameters viewparams, String path) {
    try {
      bma.getBeanValue(path, viewparams, null);
    }
    catch (Exception e) {
      reportError(": path " + path + " is not readable for exemplar reported for view with ID " + viewID,
          viewID, viewparams, e);
    }
    
  }
  
}
