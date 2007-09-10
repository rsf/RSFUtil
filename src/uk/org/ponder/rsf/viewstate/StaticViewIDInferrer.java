/*
 * Created on 10 Sep 2007
 */
package uk.org.ponder.rsf.viewstate;

/** A StaticViewIDInferrer uses a constant policy to infer the ViewID. It is
 * either an attribute of fixed name or a trunk path component of fixed index.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface StaticViewIDInferrer extends ViewIDInferrer {
  /** Returns an attribute name or trunk path index where the viewID is to be
   * located. These names agree with those returned from 
   * {@link ViewParameters#getParseSpec()}.
   */
  public String getViewIDSpec();
}
