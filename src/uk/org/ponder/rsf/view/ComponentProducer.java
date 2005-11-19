package uk.org.ponder.rsf.view;

import uk.org.ponder.errorutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

// This honorary comment left in honour of Hans Bergsten, who suggested
// as loudly as he could that JSF could be better (see Chapter 15!).
/**
 * This interface must be implemented by classes representing a view for the
 * ClassViewHandler.
 * 
 * @author Hans Bergsten, Gefion Software <hans@gefionsoftware.com>
 * @version 1.0
 */
public abstract class ComponentProducer {
  /** @param tofill The container into which produced components will be inserted.
   *  @param origviewparams The view parameters specifying the currently rendering 
   *  view
   *  @param checker A ComponentChecker (actually an interface into a ViewTemplate)
   *  that can be used by the producer to "short-circuit" the production of 
   *  potentially expensive components if they are not present in the chosen
   *  template for this view. Since the IKAT algorithm cannot run at this time, it
   *  is currently only economic to check components that are present at the root
   *  level of the template, but these are the most likely to be expensive.
   */
  public abstract void fillComponents(UIContainer tofill, ViewParameters origviewparams, 
      ComponentChecker checker);
//
//  protected MessageLocator messagelocator;
//  
//  public void setMessageLocator(MessageLocator messagelocator) {
//    this.messagelocator = messagelocator;
//  }
//  
//  public MessageLocator getMessageLocator() {
//    return messagelocator;
//  }

}