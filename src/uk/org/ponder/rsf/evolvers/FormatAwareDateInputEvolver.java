/*
 * Created on 30-Nov-2006
 */
package uk.org.ponder.rsf.evolvers;

/** A date input evolved that is capable of being adjusted for various styles
 * of input.
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public interface FormatAwareDateInputEvolver extends DateInputEvolver {
  /** Alters the default message key to use in the case of entry of an invalid time
   * (the default is the same value as that for {@link DateInputEvolver#setInvalidDateKey}
   * @param invalidDateKey The message key for an invalid date error message
   */
  public void setInvalidTimeKey(String invalidTimeKey);
  
  public static final String DATE_INPUT = "date input";
  public static final String DATE_TIME_INPUT = "date time input";
  public static final String TIME_INPUT = "time input";
  
  public void setStyle(String style);
}
