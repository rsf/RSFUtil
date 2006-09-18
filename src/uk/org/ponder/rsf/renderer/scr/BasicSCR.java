/*
 * Created on 18 Sep 2006
 */
package uk.org.ponder.rsf.renderer.scr;

import uk.org.ponder.rsf.renderer.ComponentRenderer;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.xml.XMLWriter;

public interface BasicSCR extends StaticComponentRenderer {
  /** Renders the rewritten template data to the output stream.
   * @param lump The  lump corresponding to the opening tag holding
   * the <code>rsf:id</code> matching this renderer's name.
   * @param xmlw The {@link XMLWriter} to which the rewritten data is to be
   * sent.
   * @return An integer code identifying the action this renderer took, either
   * {@link ComponentRenderer#LEAF_TAG} or {@link ComponentRenderer#NESTING_TAG}
   * depending on whether just the opening tag or also the closing tag was consumed from 
   * the template. 
   */
  public int render(XMLLump lump, XMLWriter xmlw);
}
