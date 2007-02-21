/*
 * Created on 24 Oct 2006
 */
package uk.org.ponder.rsf.components.decorators;

import uk.org.ponder.rsf.components.UIComponent;

/** Specifies that the decorated component is a label which targets another
 * component. In HTML, this would be represented by a &lt;label for=" tag.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class UILabelTargetDecorator implements UIDecorator {
  public String targetFullID;
  public UILabelTargetDecorator(UIComponent target) {
    targetFullID = target.getFullID();
  }
}
