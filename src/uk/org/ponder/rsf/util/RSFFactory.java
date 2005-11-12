/*
 * Created on Jan 6, 2005
 */
package uk.org.ponder.rsf.util;

import java.util.StringTokenizer;

import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIIKATContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.util.Logger;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RSFFactory {  
  // typically used to add a fast EL binding - remember to allow UIForm elements
  // to appear in the tree to indicate scope. They can be omitted if scope
  // agrees with repetition scope. Assume that it is impossible to achieve
  // a super-repetition scope. fast EL will be useful for non-input components
  // too.
//  public static void addParameter(UIContainer parent, String name, String value) {
//    parent.getActiveForm().hiddenfields.put(name, value);
//  }
  
  // adds global parameters required for ALL consumer-rendered links, in
  // a form environment. 
  public static void extraParametersToForm(String extraparams,
      ParameterList target) {
    Logger.log
        .info("Action link requires extra parameters from " + extraparams);
    StringTokenizer st = new StringTokenizer(extraparams, "&");
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      int eqpos = token.indexOf("=");
      String key = token.substring(0, eqpos);
      String value = token.substring(eqpos + 1);
      target.add(new UIParameter(key, value));
      Logger.log.info("Added extra parameter key " + key + " value " + value
          + " to command link");
    }
  }

}
