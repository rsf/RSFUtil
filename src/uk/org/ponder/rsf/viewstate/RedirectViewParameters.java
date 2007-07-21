/*
 * Created on 20 Jul 2007
 */
package uk.org.ponder.rsf.viewstate;

/** 
 * A possible return from a {@link ViewParamsInterceptor} which represents
 * that this view should be rendered as a redirect rather than handled 
 * directly. 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class RedirectViewParameters extends SimpleViewParameters {
  public AnyViewParameters target;

  public RedirectViewParameters(AnyViewParameters target) {
    this.target = target;
  }
}
