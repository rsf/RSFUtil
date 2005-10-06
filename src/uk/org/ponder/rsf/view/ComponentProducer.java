package uk.org.ponder.rsf.view;

import uk.org.ponder.errorutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.webapputil.ViewParameters;

// This honorary comment left in honour of Hans Bergsten, who suggested
// as loudly as he could that JSF could be better.
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