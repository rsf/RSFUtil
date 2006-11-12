/*
 * Created on 22 Jul 2006
 */
package uk.org.ponder.rsf.components.decorators;

import uk.org.ponder.rsf.content.ContentTypeInfo;

/** A decorator which allows customisation of IKAT's strategy for allocating
 * the "id" attribute on a tag-by-tag basis. This will override the more 
 * general strategy declared for the content type in {@link ContentTypeInfo}.
 *  
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIIDStrategyDecorator extends UIDecorator {
  /** A special constant in addition to those in ContentTypeInfo, specifying
   * that the ID of this element will be allocated to the value held in 
   * <code>ID</code>.
   */
  public static final String ID_MANUAL = "manual";
  
  public static final UIIDStrategyDecorator ID_FULL = 
    new UIIDStrategyDecorator(ContentTypeInfo.ID_FULL);
  
  public static final UIIDStrategyDecorator ID_NONE = 
    new UIIDStrategyDecorator(ContentTypeInfo.ID_NONE);
  
  public static final UIIDStrategyDecorator ID_RSF = 
    new UIIDStrategyDecorator(ContentTypeInfo.ID_RSF);
  
  public String IDStrategy;
  
  public String ID;

  /** Constructs a manual ID strategy setting the "id" attribute to the
   * specified value.
   */
  public UIIDStrategyDecorator(String ID) {
    this.ID = ID;
    this.IDStrategy = ID_MANUAL;
  }
  
  public UIIDStrategyDecorator() {}
}
