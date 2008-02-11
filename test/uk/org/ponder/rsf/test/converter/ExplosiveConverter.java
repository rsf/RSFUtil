/*
 * Created on 11 Feb 2008
 */
package uk.org.ponder.rsf.test.converter;

import uk.org.ponder.conversion.LeafObjectParser;

public class ExplosiveConverter implements LeafObjectParser {

  public Object copy(Object tocopy) {
    return null;
  }

  public Object parse(String toparse) {
    throw new IllegalArgumentException("ExplosiveConverter should not be applied, test definition only");
  }

  public String render(Object torender) {
    throw new IllegalArgumentException("ExplosiveConverter should not be applied, test definition only");
  }

}
