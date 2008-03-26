/*
 * Created on 21 Nov 2006
 */
package uk.org.ponder.rsf.viewstate;

/** A special class of ViewParameters accepted as a redirect specification
 * which performs no redirect. That is, a NavigationCase may report
 * a <code>resultingView</code> as NoViewParameters indicating that the
 * handler has somehow succeeded in processing the action request by itself.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class NoViewParameters extends SimpleViewParameters {
  public static final NoViewParameters instance = new NoViewParameters();
}
