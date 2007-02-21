/*
 * Created on 2 Nov 2006
 */
package uk.org.ponder.rsf.components.decorators;

import java.util.Map;

/** A decorator that will apply CSS styles directly to the rendered element.
 * In HTML, this will be rendered as the "style" attribute.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UICSSDecorator implements UIDecorator {
  public Map stylemap;
 
  public UICSSDecorator() {}
  
  /** 
   * @param stylemap A map of Strings to Strings, where the key is the CSS
   * style name and the value is its value. For example, 
   * <code>stylemap.put("text-indent", "5em");</code>
   * 
   */
  public UICSSDecorator(Map stylemap) {
    this.stylemap = stylemap;
  }
}
