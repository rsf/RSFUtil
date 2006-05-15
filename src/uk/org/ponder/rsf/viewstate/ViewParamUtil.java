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
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.stringutil.StringList;
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
   * @return
   */
  
  public static DeepBeanCloner getCloner() {
    return (DeepBeanCloner) GlobalBeanAccessor.getBean("deepBeanCloner");
  }
  
  /** Converts the portion of ViewParameters that will be rendered into URL
   * attributes into name/value pairs as a Map.
   * @param vsh
   * @param viewparams
   * @return
   */
  
  public static Map viewParamsToMap(ViewStateHandler vsh, ViewParameters viewparams) {
    String fullURL = vsh.getFullURL(viewparams);
    int qpos = fullURL.indexOf('?');
    HashMap togo = new HashMap();
    return URLUtil.paramsToMap(fullURL.substring(qpos + 1), togo);
  }
  
  /** Embody "most" of the state from a ViewParameters object into a GET
   * form. I.e create a set of "forced" submitted values that will construct
   * navigation state that agrees with the parameters, ignoring i) state that does
   * not enter attributes, ii) any named attributes (which presumably correspond to
   * values that will be altered by "real" form components that are being 
   * rendered manually). Unclear that this will in general have a meaning 
   * outside HTTP but there is a fighting chance - RSF is not really portable
   * to architectures that don't communicate via name/value pairs anyway.
   * @param vsh A ViewStateHandler that can render the ViewParameters into 
   * a URL for the current request.
   * @param orig The ViewParameters whose state is to be imbued into the form.
   * @param target The Form to receive the state.
   * @param exceptions A list of "exceptions" from the state copying procedure.
   * Note that these are expressed in the <b>attribute</b> name space, and <b>
   * not</b> in terms of the parent EL paths through ViewParameters, since the 
   * former are generally more compact. However, this strictly is a violation of
   * view/model separation, so you may want to use the ViewParamsMapper to
   * compute this list for full isolation.
   */
  
  public static void addAroundGETForm(ViewStateHandler vsh, ViewParameters orig, 
      UIForm target, StringList exceptions) {
    Map parammap = viewParamsToMap(vsh, orig);
    for (int i = 0; i < exceptions.size(); ++ i) {
      parammap.remove(exceptions.get(i));
    }
    ParameterList params = mapToParamList(parammap);
    target.parameters.add(params);
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

}
