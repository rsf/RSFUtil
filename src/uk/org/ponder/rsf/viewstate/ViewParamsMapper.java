/*
 * Created on 07-May-2006
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Map;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.stringutil.StringList;

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

  public void parseViewParamAttributes(ViewParameters target, Map params) {
    ViewParamsMapInfo mapinfo = vpmim.getMappingInfo(target);
    for (int i = 0; i < mapinfo.attrnames.length; ++ i) {
      String path = mapinfo.paths[i];
      Object valueo = params.get(mapinfo.attrnames[i]);
      if (valueo != null) {
        bma.setBeanValue(path, target, valueo, null);
      }
    }
  }
  
  /** Returns the "mid-portion" of the URL corresponding to these parameters,
   * i.e. /view-id/more-path-info?param1=val&param2=val 
   */
  public String toHTTPRequest(ViewParameters toconvert) {
    CharWrap togo = new CharWrap();
    togo.append(toconvert.toPathInfo());
    ViewParamsMapInfo mapinfo = vpmim.getMappingInfo(toconvert);
    boolean isfirst = true;
    for (int i = 0; i < mapinfo.attrnames.length; ++ i) {
      String attrname = mapinfo.attrnames[i];
      String path = mapinfo.paths[i];
      String attrval = (String) bma.getFlattenedValue(path, toconvert, String.class, null);
      if (attrval != null) {
        togo.append(isfirst? '?' : '&');
        togo.append(attrname);
        togo.append("=");
        togo.append(attrval);
        isfirst = false;
      }
    }
    String anchorfield = toconvert.getAnchorField();
    if (anchorfield != null) {
      String value = (String) bma.getFlattenedValue(anchorfield, toconvert, String.class, null);
      if (value != null) {
        togo.append("#").append(value);
      }
    }
    return togo.toString();
  }
  
  
}
