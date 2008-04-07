/*
 * Created on 07-May-2006
 */
package uk.org.ponder.rsf.viewstate.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.mapping.DARList;
import uk.org.ponder.mapping.DataAlterationRequest;
import uk.org.ponder.rsf.viewstate.CoreViewParamsCodec;
import uk.org.ponder.rsf.viewstate.RawURLState;
import uk.org.ponder.rsf.viewstate.ViewParamUtil;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsMapInfo;
import uk.org.ponder.stringutil.StringList;

/**
 * Framework class performing the function of parsing and rendering
 * ViewParameters objects to and from their raw representations.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class ViewParamsMapper implements CoreViewParamsCodec {

  private BeanModelAlterer bma;

  private ViewParamsMappingInfoManager vpmim;

  public void setVPMappingInfoManager(ViewParamsMappingInfoManager vpmim) {
    this.vpmim = vpmim;
  }

  public ViewParamsMappingInfoManager getVPMappingInfoManager() {
    return vpmim;
  }

  public void setBeanModelAlterer(BeanModelAlterer bma) {
    this.bma = bma;
  }

  public BeanModelAlterer getBeanModelAlterer() {
    return bma;
  }

  public ViewParamsMapInfo getMappingInfo(ViewParameters target) {
    return vpmim.getMappingInfo(target);
  }

  public boolean parseViewParams(ViewParameters target, RawURLState rawstate, Map unusedParams) {
    parseViewParameters(target, rawstate.params, rawstate.pathinfo, unusedParams);
    return true;
  }

  public RawURLState renderViewParams(ViewParameters torender) {
    RawURLState togo = new RawURLState();
    togo.pathinfo = toPathInfo(torender);
    togo.params = renderViewParamAttributes(torender);
    String anchorfield = torender.getAnchorField();
    if (anchorfield != null) {
      String value = (String) bma.getFlattenedValue(anchorfield, torender,
          String.class, null);
      togo.anchor = value;
    }
    return togo;
  }

  /**
   * Parse the supplied raw URL information into a ViewParameters object, whose
   * type has already been deduced.
   * 
   * @param target The ViewParameters object onto which URL information is to be
   *            parsed.
   * @param params The raw URL parameter map, a map of String onto String[].
   * @param pathinfo The "pathinfo" segment of the URL, which starts with a
   *            leading slash (/).
   */
  public void parseViewParameters(ViewParameters target, Map params,
      String[] pathinfo, Map unusedParams) {
    ConcreteViewParamsMapInfo mapinfo = vpmim.getMappingInfo(target);
    DARList toapply = new DARList();
    
    if (pathinfo != null) {
      for (int i = 0; i < mapinfo.trunkpaths.length; ++i) {
        // An extra segment will be produced for the initial /
        int reqindex = i;
        if (reqindex < pathinfo.length) {
          // look for surrogate attributes first
          Object segment = params.get(ViewParamUtil.getAttrIndex(i, true));
          // only apply low priority if we are at an endpoint
          if (segment == null && unusedParams == null) {
            segment = params.get(ViewParamUtil.getAttrIndex(i, false));
          }
          if (segment == null) {
            segment = pathinfo[reqindex];
          }
          
          toapply.add(new DataAlterationRequest(mapinfo.trunkpaths[i], segment));
        }
      }
    }
    for (Iterator keyit = params.keySet().iterator(); keyit.hasNext();) {
      String attr = (String) keyit.next();
      Object valueo = params.get(attr);
      String path = mapinfo.attributeToPath(attr);
      if (path != null) {
        if (valueo != null) {
          toapply.add(new DataAlterationRequest(path, valueo));
        }
      }
      else {
        if (unusedParams != null) unusedParams.put(attr, valueo);
      }
    }
    bma.applyAlterations(target, toapply, null);
  }

  public String[] toPathInfo(ViewParameters toconvert) {
    StringList togo = new StringList();
    ConcreteViewParamsMapInfo mapinfo = vpmim.getMappingInfo(toconvert);
    boolean nullstarted = false;
    for (int i = 0; i < mapinfo.trunkpaths.length; ++i) {
      String trunkpath = mapinfo.trunkpaths[i];
      // errors would be checked at parse assembly time
      String attrval = (String) bma.getFlattenedValue(trunkpath, toconvert,
          null, null);
      if (attrval != null) {
        if (nullstarted) {
          throw new IllegalArgumentException(
              "Illegal outgoing URL state - value " + attrval
                  + " at trunk position " + i
                  + " follows previous missing value");
        }
        togo.add(attrval);
      }
      else {
        nullstarted = true;
      }
    }
    return togo.toStringArray();
  }

  /**
   * Converts the attributes portion of the ViewParameters object to a map of
   * String to String[].
   */

  public Map renderViewParamAttributes(ViewParameters toconvert) {
    Map togo = new HashMap();
    ConcreteViewParamsMapInfo mapinfo = vpmim.getMappingInfo(toconvert);
    for (int i = 0; i < mapinfo.attrnames.length; ++i) {
      String attrname = mapinfo.attrnames[i];
      String path = mapinfo.paths[i];
      putAttr(togo, path, attrname, toconvert);
    }
    return togo;
  }

  public boolean isSupported(ViewParameters viewparams) {
    return true;
  }

  public Map renderViewParamsNonTrunk(ViewParameters torender,
      boolean highpriority) {
    Map togo = renderViewParamAttributes(torender);
    ConcreteViewParamsMapInfo mapinfo = vpmim.getMappingInfo(torender);
    for (int i = 0; i < mapinfo.trunkpaths.length; ++ i) {
      putAttr(togo, mapinfo.trunkpaths[i], ViewParamUtil.getAttrIndex(i, highpriority), torender);
    }
    return togo;
  }

  private void putAttr(Map target, String path, String attrname, ViewParameters torender) {
    Object attrval = bma.getFlattenedValue(path, torender, null, null);
    if (attrval instanceof String[]) {
      target.put(attrname, attrval);
    }
    else if (attrval instanceof String) {
      target.put(attrname, new String[] { (String) attrval });
    }
  }
  
}
