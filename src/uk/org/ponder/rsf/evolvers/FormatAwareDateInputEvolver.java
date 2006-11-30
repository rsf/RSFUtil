/*
 * Created on 30-Nov-2006
 */
package uk.org.ponder.rsf.evolvers;

/** A date input evolved that is capable of being adjusted for various styles
 * of input.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public interface FormatAwareDateInputEvolver extends DateInputEvolver {
  public static final String DATE_INPUT = "date input";
  public static final String DATE_TIME_INPUT = "date time input";
  public static final String TIME_INPUT = "time input";
  
  public void setStyle(String style);
}
