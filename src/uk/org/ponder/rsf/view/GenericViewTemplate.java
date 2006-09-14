/*
 * Created on 13 Sep 2006
 */
package uk.org.ponder.rsf.view;

import uk.org.ponder.rsf.template.XMLLumpMMap;

public class GenericViewTemplate implements ViewTemplate {
  public XMLLumpMMap globalmap = new XMLLumpMMap();
  
  public boolean hasComponent(String ID) {
    return globalmap.hasID(ID);
  }
}
