/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.viewstate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.reflect.DeepBeanCloner;
import uk.org.ponder.rsac.GlobalBeanAccessor;
import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.stringutil.URLUtil;

/** Utilities for converting URL parameters to and from Objects (ViewParameters),
 * Maps, and Lists of various dizzying forms.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class ViewParamUtil {
  /** Returns the standard RSF "DeepBeanCloner" bound to the current thread.
   * It is always undesirable to name beans in Java code, but this access is
   * necessary to permit ViewParameters objects to clone themselves in contexts
   * too small to inject this dependency.
   */
  
  public static DeepBeanCloner getCloner() {
    return (DeepBeanCloner) GlobalBeanAccessor.getBean("deepBeanCloner");
  }
  
  /** Converts the portion of ViewParameters that will be rendered into URL
   * attributes into name/value pairs as a Map.
   * @param vsh The ViewStateHandler encoding URL information
   * @param viewparams The ViewParameters object to be encoded
   * @return A map of String to String holding the key/value pairs.
   */
  
  public static Map viewParamsToMap(ViewStateHandler vsh, ViewParameters viewparams) {
    String fullURL = vsh.getFullURL(viewparams);
    int qpos = fullURL.indexOf('?');
    HashMap togo = new HashMap();
    return URLUtil.paramsToMap(fullURL.substring(qpos + 1), togo);
  }
  
  public static ParameterList mapToParamList(Map toconvert) {
    ParameterList togo = new ParameterList();
    for (Iterator keyit = toconvert.keySet().iterator(); keyit.hasNext();) {
      String key = (String) keyit.next();
      Object value = toconvert.get(key);
      if (value instanceof String) {
        togo.add(new UIParameter(key, (String) value));
      }
      else if (value instanceof String[]) {
        String[] values = (String[]) value;
        for (int i = 0; i < values.length; ++ i) {
          togo.add(new UIParameter(key, values[i]));
        }
      }
    }
    return togo;
  }

  public static String toHTTPRequest(ViewParamsCodec vpcodec, ViewParameters viewparams) {
    RawURLState rus = vpcodec.renderViewParams(viewparams);
    return ViewParamUtil.toHTTPRequest(rus);
  }
  
  /**
   * Returns the "mid-portion" of the URL corresponding to these parameters,
   * i.e. /view-id/more-path-info?param1=val&param2=val#anchor
   */
  
  public static String toHTTPRequest(RawURLState rawstate) {
    CharWrap togo = new CharWrap();
    togo.append(rawstate.pathinfo);
    boolean isfirst = true;
    for (Iterator keyit = rawstate.params.keySet().iterator(); keyit.hasNext();) {
      String attrname = (String) keyit.next();
      Object attrval = rawstate.params.get(attrname);
      if (attrval instanceof String) {
        URLUtil.appendAttribute(togo, isfirst, attrname, (String) attrval);
        isfirst = false;
      }
      else if (attrval instanceof String[]) {
        String[] vals = (String[]) attrval;
        for (int j = 0; j < vals.length; ++j) {
          URLUtil.appendAttribute(togo, isfirst, attrname, vals[j]);
          isfirst = false;
        }
      }
    }
    String anchorfield = rawstate.anchor;
    if (rawstate.anchor != null) {
        togo.append("#").append(anchorfield);
    }
    return togo.toString();
  }
  
  
  /** Parse a "reduced URL" (as often seen in serialized component trees and
   * the like) into a full ViewParameters object.
   */
  
  public static ViewParameters parse(ViewParametersParser parser, String reducedURL) {
    int qpos = reducedURL.indexOf('?');
    String pathinfo = qpos == -1? reducedURL : reducedURL.substring(0, qpos);
    HashMap params = new HashMap();
    if (qpos != -1) {
      URLUtil.paramsToMap(reducedURL.substring(qpos + 1), params);
    }
    return parser.parse(pathinfo, params); 
  }

  public static String getAnyFullURL(AnyViewParameters viewparams, ViewStateHandler vsh) {
    return viewparams instanceof RawViewParameters ? ((RawViewParameters) viewparams).URL
        : vsh.getFullURL((ViewParameters) viewparams);
  }
}
