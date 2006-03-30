/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.viewstate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.stringutil.URLUtil;

/** Utilities for converting URL parameters to and from Objects (ViewParameters),
 * Maps, and Lists of various dizzying forms.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class ViewParamUtil {
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

  /** Parse a "reduced URL" (as often seen in serialized component trees and
   * the like) into a full ViewParameters object.
   * @param parser
   * @param reducedURL
   * @return
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

  public static void parseViewParamAttributes(BeanModelAlterer bma, ViewParameters target,
      Map params) {
    StringList pathlist = target.getAttributeFields();
    for (int i = 0; i < pathlist.size(); ++ i) {
      String path = pathlist.stringAt(i);
      Object valueo = params.get(path);
      if (valueo != null) {
        bma.setBeanValue(path, target, valueo, null);
      }
    }
  }
  
  /** Returns the "mid-portion" of the URL corresponding to these parameters,
   * i.e. /view-id/more-path-info?param1=val&param2=val 
   */
  public static String toHTTPRequest(BeanModelAlterer bma, ViewParameters toconvert) {
    CharWrap togo = new CharWrap();
    togo.append(toconvert.toPathInfo());
    StringList attrs = toconvert.getAttributeFields();
    boolean isfirst = true;
    for (int i = 0; i < attrs.size(); ++i) {
      String attrname = attrs.stringAt(i);
      String attrval = (String) bma.getFlattenedValue(attrname, toconvert, String.class, null);
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
