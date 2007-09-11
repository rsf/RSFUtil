/*
 * Created on 18 Sep 2006
 */
package uk.org.ponder.rsf.renderer.scr;

import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.template.XMLLumpList;
import uk.org.ponder.xml.XMLWriter;

/** A static component renderer that can incorporate markup "collected" from
 * elsewhere in the template tree. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface CollectingSCR extends StaticComponentRenderer {
  /** Return an array of names of "contributing" SCRs,
   * consisting of the text following {@link XMLLump#SCR_CONTRIBUTE_PREFIX}
   * in the rsf:id of the contribution marker.
   * 
   */
  public String[] getCollectingNames();
  /** @see BasicSCR for details of behaviour.
   * @param collected The list of XMLLump items corresponding to the collected
   * markup that should be incorporated into the rendering of the peering tag.
   */
  public int render(XMLLump lump, XMLLumpList collected, XMLWriter xmlw);
}
