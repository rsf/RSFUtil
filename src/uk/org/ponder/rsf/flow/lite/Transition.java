/*
 * Created on Nov 22, 2005
 */
package uk.org.ponder.rsf.flow.lite;

/** Currently only concrete transitions for now.
 * Rather unclear that we will ever want anything else...
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class Transition {
  public static final String WILDCARD_ON = "*";
  public String on;
  public String to;
}
