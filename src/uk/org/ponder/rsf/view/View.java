package uk.org.ponder.jsfutil;

import javax.faces.component.UIViewRoot;

import uk.org.ponder.errorutil.MessageLocator;

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

  public abstract void createView(UIViewRoot viewroot, ViewParameters origviewparams);

  public abstract String getViewID();

  public abstract ViewParameters getDefaultParameters();
  
  protected MessageLocator messagelocator;
  
  public void setMessageLocator(MessageLocator messagelocator) {
    this.messagelocator = messagelocator;
  }
  
  public MessageLocator getMessageLocator() {
    return messagelocator;
  }
}