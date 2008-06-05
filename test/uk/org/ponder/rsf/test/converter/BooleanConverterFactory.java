/*
 * Created on 5 Jun 2008
 */
package uk.org.ponder.rsf.test.converter;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.conversion.LeafObjectParser;

public class BooleanConverterFactory implements BeanLocator {

  public Object locateBean(final String name) {
    return new LeafObjectParser() {

      public Object copy(Object tocopy) {
        return tocopy;
      }

      public Object parse(String toparse) {
        return toparse.equals("true") ? name : null;
      }

      public String render(Object torender) {
        return name.equals(torender)? "true" : "false";
      }

    };
  }

}
