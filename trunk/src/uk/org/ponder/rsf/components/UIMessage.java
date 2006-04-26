/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.components;

/** An output-only component that encapsulates the location data for a
 * message as defined in a .properties file and resolved by a MessageLocator.
 * This reference will be resolved at fixup time by the MessageFixer.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class UIMessage extends UIBoundString {
  public String messagekey;
  public Object[] arguments;
  
  public static UIMessage make(UIContainer parent, String ID, String messagekey) {
    UIMessage togo = new UIMessage();
    togo.messagekey = messagekey;
    parent.addComponent(togo);
    return togo;
  }
  
  public static UIMessage make(UIContainer parent, String ID, String messagekey,
      Object[] arguments) {
    UIMessage togo = new UIMessage();
    togo.messagekey = messagekey;
    togo.arguments = arguments;
    parent.addComponent(togo);
    return togo;
  }
}
