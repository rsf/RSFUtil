/*
 * Created on 19 Aug 2006
 */
package uk.org.ponder.rsf.template;

import java.util.Map;

/** A default parse interceptor which performs no action **/

public class NullTemplateParseInterceptor implements TemplateParseInterceptor {
  public void adjustAttributes(String tag, Map attributes) {
    return;
  }
}
