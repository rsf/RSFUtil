/*
 * Created on Sep 19, 2005
 */
package uk.org.ponder.rsf.util;

import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UIBoundString;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.xml.XMLWriter;

public class ComponentDumper {
  
  public static void dumpContainer(UIBranchContainer container, int indent, PrintOutputStream pos) {
    UIComponent[] children = container.flatChildren();
    for (int i = 0; i < children.length; ++ i) {
      UIComponent component = children[i];
      String clazz = component.getClass().getName();
      XMLWriter.indent(indent, pos);
      pos.println(clazz + " with ID " + component.ID + " fullID " + component.getFullID());
      if (component instanceof UIBranchContainer) {
        dumpContainer((UIBranchContainer) component, indent + 1, pos);
      }
      else if (component instanceof UIBound) {
        XMLWriter.indent(indent, pos);
        pos.println("Value binding: " + ((UIBound)component).valuebinding);
      }
      if (component instanceof UIBoundString) {
        XMLWriter.indent(indent, pos);
        pos.println("Text: " + ((UIBoundString)component).getValue());
      }
      else if (component instanceof UIInput) {
        XMLWriter.indent(indent, pos);
        pos.println("Value: " + ((UIInput)component).getValue());
      }
      if (component instanceof UILink) {
        XMLWriter.indent(indent, pos);
        pos.println("Link target: " + ((UILink)component).target);
      }
    }
    pos.println();
  }
}
