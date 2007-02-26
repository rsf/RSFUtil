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
  
  /** Construct a "clustered" message component, suitable for being the label
   * of a UILink or UICommand, etc.
   */
  public static UIMessage make(String messagekey) {
    return make(messagekey, null);
  }
  
  public static UIMessage make(String messagekey, Object[] arguments) {
    UIMessage togo = new UIMessage();
    togo.messagekey = messagekey;
    togo.arguments = arguments;
    return togo;
  }
  
  /** Construct a "standalone" message component suitable for appearing as
   * a top-level label component in the component tree.
   */ 
  
  public static UIMessage make(UIContainer parent, String ID, String messagekey) {
    return make(parent, ID, messagekey, null);
  }
  /** Constructs a standalone message component, making use of more complex
   * formatting. The arguments array is supplied as if to a standard 
   * Java 
   * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/text/MessageFormat.html">MessageFormat</a>.
   */
  public static UIMessage make(UIContainer parent, String ID, String messagekey,
      Object[] arguments) {
    UIMessage togo = new UIMessage();
    togo.messagekey = messagekey;
    togo.arguments = arguments;
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }
}
