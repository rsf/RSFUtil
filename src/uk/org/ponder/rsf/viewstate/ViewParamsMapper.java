/*
 * Created on 07-May-2006
 */
package uk.org.ponder.rsf.viewstate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.stringutil.StringUtil;

/**
 * Framework class performing the function of parsing and rendering
 * ViewParameters objects to and from their raw representations.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class ViewParamsMapper implements ViewParamsCodec {

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
      String pathinfo, Map unusedParams) {
    ViewParamsMapInfo mapinfo = vpmim.getMappingInfo(target);
    for (Iterator keyit = params.keySet().iterator(); keyit.hasNext();) {
      String attr = (String) keyit.next();
      Object valueo = params.get(attr);
      String path = mapinfo.attributeToPath(attr);
      if (path != null) {
        if (valueo != null) {
          bma.setBeanValue(path, target, valueo, null, true);
        }
      }
      else {
        if (unusedParams != null) unusedParams.put(attr, valueo);
      }
    }
    if (pathinfo != null) {
      String[] segments = StringUtil.split(pathinfo, '/');
      for (int i = 0; i < mapinfo.trunkpaths.length; ++i) {
        // An extra segment will be produced for the initial /
        int reqindex = i + 1;
        if (reqindex < segments.length) {
          String segment = segments[reqindex];
          bma.setBeanValue(mapinfo.trunkpaths[i], target, segment, null, true);
        }
      }
    }
  }

  public String toPathInfo(ViewParameters toconvert) {
    CharWrap togo = new CharWrap();
    ViewParamsMapInfo mapinfo = vpmim.getMappingInfo(toconvert);
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
        togo.append('/').append(attrval);
      }
      else {
        nullstarted = true;
      }
    }
    return togo.toString();
  }

  /**
   * Converts the attributes portion of the ViewParameters object to a map of
   * String to String[].
   */

  public Map renderViewParamAttributes(ViewParameters toconvert) {
    Map togo = new HashMap();
    ViewParamsMapInfo mapinfo = vpmim.getMappingInfo(toconvert);
    for (int i = 0; i < mapinfo.attrnames.length; ++i) {
      String attrname = mapinfo.attrnames[i];
      String path = mapinfo.paths[i];
      Object attrval = bma.getFlattenedValue(path, toconvert, null, null);
      if (attrval instanceof String[]) {
        togo.put(attrname, attrval);
      }
      else if (attrval instanceof String) {
        togo.put(attrname, new String[] { (String) attrval });
      }
    }
    return togo;
  }

}
