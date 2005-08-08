/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.html;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.streamutil.PrintOutputStream;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface ComponentRenderer {
  public int renderComponent(UIComponent torender, HTMLLump[] lumps,
      int lumpindex, PrintOutputStream pos);
}
