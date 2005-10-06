/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.renderer;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.streamutil.PrintOutputStream;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface RenderSystem {
  //public void setViewTemplate(ViewTemplate template);
  public int renderComponent(UIComponent torender, XMLLump[] lumps, 
      int lumpindex, PrintOutputStream pos);

  public void setStaticRenderers(StaticRendererCollection scrc);
  public String getDeclaration();
}
