/*
 * Created on 11-Jan-2006
 */
package uk.org.ponder.rsf.viewstate;

import java.util.HashMap;
import java.util.Map;

/** A root placeholder class to aid deserialization of the sitemap, into
 * its ultimate home in a BasicViewParametersParser. This will be a map of
 * String viewIDs into ViewParameters exemplars.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class SiteMap {
  public Map view = new HashMap();
}
