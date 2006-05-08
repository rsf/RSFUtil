/*
 * Created on 07-May-2006
 */
package uk.org.ponder.rsf.viewstate;

/** (Cached) mapping information describing the mapping from URL attributes
 * onto bean paths for ViewParameters objects.
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public class ViewParamsMapInfo {
  /** Names of attributes to be parsed (e.g. from a URL) */
  public String[] attrnames;
  /** corresponding EL paths using the ViewParameters object as a base */
  public String[] paths;
}
