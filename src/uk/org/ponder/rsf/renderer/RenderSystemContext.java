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
  public boolean debugrender;
  public View view;
  public PrintOutputStream pos;
  public XMLWriter xmlw;
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
