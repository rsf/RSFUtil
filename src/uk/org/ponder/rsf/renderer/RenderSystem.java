/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.renderer;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.streamutil.write.PrintOutputStream;

/**
 * The request-scope-specific portion of the RenderSystem in force.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface RenderSystem {
  //public void setViewTemplate(ViewTemplate template);
  /** Invoked by the IKAT renderer in order to perform a template rewrite based
   * on the System XML dialect. 
   * @param torender The UIComponent which IKAT determines is to be 
   * corresponded with the specified tag. The ID of this component will match
   * (at least in prefix) the rsf:id of tag specified in lumpindex. The 
   * component may be <code>null</code> where the tag is to be target of a 
   * static rewrite rule, in which case the System will invoke the relevant SCR.
   * @param lumps The full array of XMLLump object specifing the condensed
   * representation of the template set.
   * @param lumpindex The index of the lump (head lump holding open tag) where
   * the tag appears within the template requiring rewrite.
   * @param pos The output stream where the transformed template data is to be
   * written.
   */
  public int renderComponent(UIComponent torender, XMLLump[] lumps, 
      int lumpindex, PrintOutputStream pos);

 
  public void setStaticRenderers(StaticRendererCollection scrc);
  /** Returns an XML/HTML declaration suitable to appear at the head of rendered
   * (full) files. This method will be invoked should the IKAT renderer 
   * encounter a declaration at the head of the root template file. If this
   * method returns null, the template declaration will be retained.
   * TODO: Fix this functionality in ViewRender by recognising template
   * declaration.
   */
  public String getDeclaration();
  
}
