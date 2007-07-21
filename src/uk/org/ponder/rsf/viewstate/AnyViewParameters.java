/*
 * Created on 31 Oct 2006
 */
package uk.org.ponder.rsf.viewstate;

/** A base interface tagging a ViewParameters which refers to "any" view,
 * whether internal or external. Currently internal views are all represented
 * by descendents of {@link ViewParameters}, and external views by 
 * {@link RawViewParameters} holding a raw URL.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface AnyViewParameters {
  /** Pea proxying method */
  public AnyViewParameters get();
  
  public AnyViewParameters copy();
 
}
