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
  public String[] messagekeys;
  public Object[] arguments;
  
  /** Construct a "clustered" message component, suitable for being the label
   * of a UILink or UICommand, etc.
   */
  public static UIMessage make(String messagekey) {
    return make(messagekey, null);
  }
  
  /** Constructs a "standalone" message component
   * See {@link #make(String), #make(UIContainer, String, String[], Object[])
   */
  
  public static UIMessage make(String messagekey, Object[] arguments) {
    UIMessage togo = new UIMessage();
    togo.messagekeys = new String[] {messagekey};
    togo.arguments = arguments;
    return togo;
  }
 
  /** Constructs a "standalone" message component
   * See {@link #make(String), #make(UIContainer, String, String[], Object[])
   */
  
  private static UIMessage make(String[] messagekeys, Object[] arguments) {
    UIMessage togo = new UIMessage();
    togo.messagekeys = messagekeys;
    togo.arguments = arguments;
    return togo;
  }
  
  /** Construct a "standalone" message component suitable for appearing as
   * a top-level label component in the component tree.
   */ 
  
  public static UIMessage make(UIContainer parent, String ID, String messagekey) {
    return make(parent, ID, messagekey, null);
  }
  /** Constructs a message component suitable for appearing as a top-level
   * label component in the component tree, making use of more complex
   * formatting. The arguments array is supplied as if to a standard 
   * Java 
   * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/text/MessageFormat.html">MessageFormat</a>.
   */
  public static UIMessage make(UIContainer parent, String ID, String messagekey,
      Object[] arguments) {
    UIMessage togo = make(messagekey, arguments);
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }
  /** Constructs a top-level message component, supporting defaultible
   * message fallback as in the Spring 
   * <a href="http://www.springframework.org/docs/api/org/springframework/context/MessageSourceResolvable.html">
   * MessageSourceResolvable</a> interface.
   * See {@link #make(UIContainer, String, String, Object[])}
   */
  public static UIMessage make(UIContainer parent, String ID, String messagekeys[],
      Object[] arguments) {
    UIMessage togo = make(messagekeys, arguments);
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }

 
}
