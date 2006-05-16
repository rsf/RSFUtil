/*
 * Created on 07-May-2006
 */
package uk.org.ponder.rsf.viewstate;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.stringutil.CharWrap;

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

  public void parseViewParamAttributes(ViewParameters target, Map params) {
    ViewParamsMapInfo mapinfo = vpmim.getMappingInfo(target);
    for (int i = 0; i < mapinfo.attrnames.length; ++i) {
      String path = mapinfo.paths[i];
      Object valueo = params.get(mapinfo.attrnames[i]);
      if (valueo != null) {
        bma.setBeanValue(path, target, valueo, null);
      }
    }
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
    togo.append(toconvert.toPathInfo());
    ViewParamsMapInfo mapinfo = vpmim.getMappingInfo(toconvert);
    boolean isfirst = true;
    for (int i = 0; i < mapinfo.attrnames.length; ++i) {
      String attrname = mapinfo.attrnames[i];
      String path = mapinfo.paths[i];
      Object attrval = bma.getFlattenedValue(path, toconvert, null, null);
      if (attrval instanceof String) {
        togo.append(isfirst ? '?' : '&');
        togo.append(attrname);
        togo.append("=");
        togo.append(attrval);
        isfirst = false;
      }
      else if (attrval instanceof String[]) {
        String[] vals = (String[]) attrval;
        for (int j = 0; j < vals.length; ++j) {
          togo.append(isfirst ? '?' : '&');
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
