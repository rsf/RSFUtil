/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.uitype;

/** UIType marks a "User Interface" type, which is a type that can be expected
 * to originate (atomically) **FROM** the input layer of a UI. This is a system-wide fixed
 * collection of types that is rarely expected to expand. For example, the only
 * expansion as a result of HTTP is supporting the String[] type, in addition 
 * to the String and Boolean types that would be needed for GUI programming.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface UIType {
  /** Gets a placeholder value, that signifies not only that no value has
   * yet been set, but also the type of value that is expected 
   */
  public Object getPlaceholder();
  public String getName();

  /** A "UI-level" indication of whether a value was changed. At this point
   * the values are in String/String[] form and this method is intended to 
   * perform a cheap "cull" of incoming change requests to avoid unnecessary
   * trips to the model. 
   */
  public boolean valueUnchanged(Object oldvalue, Object newvalue);
}
