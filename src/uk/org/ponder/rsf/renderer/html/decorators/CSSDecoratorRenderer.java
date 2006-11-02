/*
 * Created on 2 Nov 2006
 */
package uk.org.ponder.rsf.renderer.html.decorators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.htmlutil.HTMLUtil;
import uk.org.ponder.rsf.components.decorators.UICSSDecorator;
import uk.org.ponder.rsf.components.decorators.UIDecorator;
import uk.org.ponder.rsf.renderer.decorator.DecoratorRenderer;

public class CSSDecoratorRenderer implements DecoratorRenderer {

  public String getContentTypes() {
    return "HTML, HTML-FRAGMENT, XUL, SVG";
  }

  public Class getRenderedType() {
    return UICSSDecorator.class;
  }

  public String attrName = "style";
  
  public void modifyAttributes(UIDecorator decoratoro, String tagname, 
      Map tomodify) {
    UICSSDecorator decorator = (UICSSDecorator) decoratoro;
    String oldstyle = (String) tomodify.get(attrName);
    Map stylemap = new HashMap();
    if (oldstyle != null) {
      HTMLUtil.parseStyle(oldstyle, stylemap);
    }
    
    for (Iterator sit = decorator.stylemap.keySet().iterator(); sit.hasNext();) {
      String key = (String) sit.next();
      String value = (String) decorator.stylemap.get(key);
      stylemap.put(key, value);
    }
    String newstyle = HTMLUtil.renderStyle(stylemap);
    tomodify.put(attrName, newstyle);
  }

}
