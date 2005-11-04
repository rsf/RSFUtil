/*
 * Created on Jan 6, 2005
 */
package uk.org.ponder.rsf.util;

import java.util.StringTokenizer;

import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.rsf.components.UISelectItem;
import uk.org.ponder.rsf.components.UISelectOne;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.Logger;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RSFFactory {
  
  public static UIContainer makeContainer(UIContainer parent, String ID) {
    UIContainer togo = new UIContainer();
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }
  
  public static UIContainer make2Container(UIContainer parent, String ID1, String ID2) {
    UIContainer togo1 = makeContainer(parent, ID1);
    UIContainer togo2 = makeContainer(togo1, ID2);
    return togo2;
  }
  
  public static UIOutput makeText(UIContainer parent, String ID,
      String binding, String initvalue) {
    UIOutput togo = new UIOutput();
    togo.text = initvalue;
    togo.valuebinding = binding;
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }

  public static UIOutput makeText(UIContainer parent, String ID,
      String initvalue) {
    return makeText(parent, ID, null, initvalue);
  }

  public static UIOutput makeText(UIContainer parent, String ID) {
    return makeText(parent, ID, null, null);
  }
 
  
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

  public static UILink makeLink(UIContainer parent, String ID, String text, String target) {
    UILink togo = new UILink();
    togo.ID = ID;
    togo.text = text;
    togo.target = target;
    parent.addComponent(togo);
    return togo;
  }
  
   public static UILink makeLink(UIContainer parent, String ID, String target) {
    return makeLink(parent, ID, null, target);
  }

}
