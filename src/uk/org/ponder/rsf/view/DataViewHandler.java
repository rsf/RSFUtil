/*
 * Created on 25 Mar 2008
 */
package uk.org.ponder.rsf.view;

import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Handles access to a data view on behalf of the framework **/

public interface DataViewHandler {
  public void handleView(DataView view, ViewParameters viewparams);
  public void handleInput(DataInputHandler handler, ViewParameters viewparams);

}