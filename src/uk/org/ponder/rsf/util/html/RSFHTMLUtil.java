/*
 * Created on 21 Sep 2006
 */
package uk.org.ponder.rsf.util.html;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.util.RSFUtil;
import uk.org.ponder.rsf.view.ViewRoot;

public class RSFHTMLUtil {
  public static final String ELEMENT_ID_TO_FOCUS = "elementIDToFocus";
  
  public static void setElementToFocus(UIComponent tofocus) {
    ViewRoot root = RSFUtil.findViewRoot(tofocus);
    UIOutput tofocusel = (UIOutput) root.getComponent(ELEMENT_ID_TO_FOCUS);
    if (tofocusel == null) {
      tofocusel = UIOutput.make(root, "elementIDToFocus");  
    }
    tofocusel.setValue(tofocus.getFullID());
  }
}
