/*
 * Created on 12 Feb 2008
 */
package uk.org.ponder.rsf.test.diminish;

public class DiminishValidator {
  public void setDiminisher(Diminisher diminisher) {
    if (diminisher.int1.intValue() == 1) 
      throw new IllegalArgumentException("Value of first integer must be 2");
  }
}
