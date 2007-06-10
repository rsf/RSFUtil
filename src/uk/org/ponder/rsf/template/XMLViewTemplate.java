/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.template;

import uk.org.ponder.rsf.view.BasedViewTemplate;
import uk.org.ponder.rsf.view.GenericViewTemplate;

/**
 * The raw constituents of an XML view template, being i) The XMLLump[] array,
 * ii) The "root lump" holding the initial downmap, and iii) the global headmap.
 * The system assumes that renders will be much more frequent than template
 * reads, and so takes special efforts to condense the representation for rapid
 * render-time access, at the expense of slightly slower parsing.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class XMLViewTemplate extends GenericViewTemplate 
implements BasedViewTemplate {
  // a hypothetical "root lump" whose downmap contains root RSF components.
  public XMLLump rootlump;
  // public XMLLumpMMap globalmap;
  // private HashMap foridtocomponent = new HashMap();
  public XMLLump[] lumps;
  // index of the first lump holding root document tag
  public int roottagindex;
  
  public char[] buffer;
  // The template's full path, for debugging purposes
  public String fullpath;
  
  private String resourcebase;

  private String extresourcebase;
  
  public void setExtResourceBase(String extresourcebase) {
    this.extresourcebase = extresourcebase;
  }
  
  public String getExtResourceBase() {
    return extresourcebase;
  }
  
  public void setRelativeResourceBase(String resourcebase) {
    this.resourcebase = resourcebase;
  }

  public String getRelativeResourceBase() {
    return resourcebase;
  }

}