/*
 * Created on Apr 26, 2006
 */
package uk.org.ponder.rsf.components;

/** A component which allows raw markup to be inserted into the rendered
 * output. Any users of this class should be flayed alive and then burnt over
 * hot coals. You have been warned - you have stepped outside the bounds of 
 * RSF and the portability and coherence of your app cannot be guaranteed!
 * @author Martin G. Bananas (antranig@caret.cam.ac.uk)
 */

public class UIVerbatim extends UIComponent {
  /** An Object holding the raw markup to be rendered. 
   * See {@link #make(UIContainer, String, Object)} for supported conversions.
   */
  public Object markup;
  
  /** A reference to an object, or an object that can be converted to, a 
   * LeafRenderer or BeanResolver that can render the value above 
   * to and from a String representation.
   */
  public Object resolver;
  
  /** Construct a new UIVerbatim control, rendering raw unescaped markup to the client 
   *
   * @param parent The parent container to which the new control is to be attached.
   * @param ID The (RSF) ID of this control
   * @param markup An Object holding the raw markup to be rendered. Either a String, 
   * UIBoundString, InputStream (assumed UTF-8) or Reader, else toString() 
   * will be called to get character data. May also be an ELReference 
   * from which one of the previously mentioned types can be read.
   */
  
  public static UIVerbatim make(UIContainer parent, String ID, Object markup) {
    UIVerbatim togo = new UIVerbatim();
    togo.ID = ID;
    togo.markup = markup;
    
    parent.addComponent(togo);
    return togo;
  }
  
  /** Indicate that the "markup" field for this control is to be interpreted
   * as a message key, to be looked up to a piece of markup in the associated
   * message bundle.
   */
  
  public UIVerbatim setMessageKey() {
    this.resolver = new ELReference("#{messageLocator}");
    return this;
  }
}
