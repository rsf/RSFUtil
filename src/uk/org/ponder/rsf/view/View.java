package uk.org.ponder.jsfutil;

import javax.faces.component.UIViewRoot;


/**
 * This interface must be implemented by classes representing a
 * view for the ClassViewHandler.
 *
 * @author Hans Bergsten, Gefion Software <hans@gefionsoftware.com>
 * @version 1.0
 */
public abstract class View {
  /** The UIViewRoot object returned from createView will be given a
   * String attribute with this name representing the title required for
   * the specific view rendered.
   */
  public static final String VIEW_NAME = "View name";
    public abstract UIViewRoot createView(ViewParameters origviewparams);

    /**
     * @param viewid
     */

    String viewid;
    public void setViewID(String viewid) {
      this.viewid = viewid;
    }
    public String getViewID() {
      return viewid;
    }
    public abstract ViewParameters getDefaultParameters();
}
