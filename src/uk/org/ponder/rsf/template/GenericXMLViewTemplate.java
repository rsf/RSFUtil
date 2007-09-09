/*
 * Created on 13 Sep 2006
 */
package uk.org.ponder.rsf.template;

import uk.org.ponder.rsf.view.ViewTemplate;

public class GenericXMLViewTemplate implements ViewTemplate {
  // a map from scr=contribute- suffix to list of lumps referenced, for collectingSCRs
  public XMLLumpMMap collectmap;
  
  public XMLLumpMMap mustcollectmap;
  
  public XMLLumpMMap globalmap = new XMLLumpMMap();
  
  public boolean hasComponent(String ID) {
    return globalmap.hasID(ID);
  }
}
