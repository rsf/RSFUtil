/*
 * Created on 25 May 2007
 */
package uk.org.ponder.rsf.renderer;

import uk.org.ponder.rsf.template.XMLLumpMMap;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.xml.XMLWriter;

/**
 * Encapsulates the call-invariant arguments to a RenderSystem in a convenient
 * package.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * @since 0.7.1A
 */

public class RenderSystemContext {
  /** Flag indicating whether this rendering is to be performed in debug mode 
   */
  public boolean debugrender;
  /** The view currently being rendered - necessary to resolve
  * any inter-component references. */
  public View view;
  /** The output stream where the transformed template data is to be
  * written. */
  public PrintOutputStream pos;
  /** An XML-based wrapper for the PrintOutputStream **/
  public XMLWriter xmlw;
  /** @param idassigner An {@link IDAssigner} object to be used for assigning 
  * a value to outgoing XML <code>id</code> attributes.*/
  public IDAssigner IDassigner;
  public XMLLumpMMap collecteds;

  public RenderSystemContext(boolean debugrender, View view,
      PrintOutputStream pos, XMLWriter xmlw, IDAssigner dassigner,
      XMLLumpMMap collecteds) {
    this.debugrender = debugrender;
    this.view = view;
    this.pos = pos;
    this.xmlw = xmlw;
    IDassigner = dassigner;
    this.collecteds = collecteds;
  }

}
