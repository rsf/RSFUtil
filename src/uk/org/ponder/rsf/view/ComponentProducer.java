package uk.org.ponder.rsf.util;

import uk.org.ponder.errorutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.webapputil.ViewParameters;

/**
 * This interface must be implemented by classes representing a view for the
 * ClassViewHandler.
 * 
 * @author Hans Bergsten, Gefion Software <hans@gefionsoftware.com>
 * @version 1.0
 */
public abstract class ComponentProducer {
 
  public abstract void fillComponents(UIContainer tofill, ViewParameters origviewparams, 
      ComponentChecker checker);

  protected MessageLocator messagelocator;
  
  public void setMessageLocator(MessageLocator messagelocator) {
    this.messagelocator = messagelocator;
  }
  
  public MessageLocator getMessageLocator() {
    return messagelocator;
  }

}