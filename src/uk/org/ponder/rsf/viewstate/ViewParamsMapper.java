/*
 * Created on 07-May-2006
 */
package uk.org.ponder.rsf.viewstate;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.stringutil.StringUtil;

/** Framework class performing the function of parsing and rendering 
 * ViewParameters objects to and from their raw representations.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class ViewParamsMapper {

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

  /**
   * Parse the supplied raw URL information into a ViewParameters object, whose
   * type has already been deduced.
   * 
   * @param target The ViewParameters object onto which URL information is to be
   *          parsed.
   * @param params The raw URL parameter map, a map of String onto String[].
   * @param pathinfo The "pathinfo" segment of the URL, which starts with a leading slash
   *          (/).
   */
  public void parseViewParameters(ViewParameters target, Map params,
      String pathinfo) {
    ViewParamsMapInfo mapinfo = vpmim.getMappingInfo(target);
    for (int i = 0; i < mapinfo.attrnames.length; ++i) {
      String path = mapinfo.paths[i];
      Object valueo = params.get(mapinfo.attrnames[i]);
      if (valueo != null) {
        bma.setBeanValue(path, target, valueo, null, true);
      }
    }
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
                  + " at trunk position " + i + " follows previous missing value");
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

  /**
   * Returns the "mid-portion" of the URL corresponding to these parameters,
   * i.e. /view-id/more-path-info?param1=val&param2=val
   */
  public String toHTTPRequest(ViewParameters toconvert) {
    CharWrap togo = new CharWrap();
    togo.append(toPathInfo(toconvert));
    ViewParamsMapInfo mapinfo = vpmim.getMappingInfo(toconvert);
    boolean isfirst = true;
    for (int i = 0; i < mapinfo.attrnames.length; ++i) {
      String attrname = mapinfo.attrnames[i];
      String path = mapinfo.paths[i];
      Object attrval = bma.getFlattenedValue(path, toconvert, null, null);
      if (attrval instanceof String) {
        togo.append(isfirst ? '?'
            : '&');
        togo.append(attrname);
        togo.append("=");
        togo.append(attrval);
        isfirst = false;
      }
      else if (attrval instanceof String[]) {
        String[] vals = (String[]) attrval;
        for (int j = 0; j < vals.length; ++j) {
          togo.append(isfirst ? '?'
              : '&');
          togo.append(attrname);
          togo.append("=");
          togo.append(vals[j]);
          isfirst = false;
        }
      }
    }
    String anchorfield = toconvert.getAnchorField();
    if (anchorfield != null) {
      String value = (String) bma.getFlattenedValue(anchorfield, toconvert,
          String.class, null);
      if (value != null) {
        togo.append("#").append(value);
      }
    }
    return togo.toString();
  }

}
