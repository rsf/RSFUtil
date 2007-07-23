/*
 * Created on 07-May-2006
 */
package uk.org.ponder.rsf.viewstate;

import java.util.HashMap;
import java.util.Map;

/** (Cached) mapping information describing the mapping from URL attributes
 * onto bean paths for ViewParameters objects.
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public class ViewParamsMapInfo {
  /** Names of attributes to be parsed (e.g. from a URL) */
  public String[] attrnames;
  /** corresponding EL paths using the ViewParameters object as a base */
  public String[] paths;
  /** The EL paths (if any) to be parsed onto the URL pathinfo trunk */
  public String[] trunkpaths;
  /** A lookup from the <code>paths</code> entry to <code>attrname</code> **/
  Map pathToAttr = new HashMap();
  /** A lookup from the <code>attrname</code> entry to <code>path</code> **/
  Map attrToPath = new HashMap();
  public String pathToAttribute(String path) {
    return (String) pathToAttr.get(path);
  }
  public String attributeToPath(String attribute) {
    return (String) attrToPath.get(attribute);
  }
}
