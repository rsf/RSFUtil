package uk.org.ponder.jsfutil;

import javax.faces.component.UIViewRoot;

/**
 * This interface must be implemented by classes representing a view for the
 * ClassViewHandler.
 * 
 * @author Hans Bergsten, Gefion Software <hans@gefionsoftware.com>
 * @version 1.0
 */
public abstract class View {
  /**
   * The UIViewRoot object returned from createView will be given a String
   * attribute with this name representing the title required for the specific
   * view rendered.
   */
  public static final String VIEW_NAME_ATTRIBUTE = "View name";

  public abstract UIViewRoot createView(ViewParameters origviewparams);

  public abstract String getViewID();

  public abstract ViewParameters getDefaultParameters();
}