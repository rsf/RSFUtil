/*
 * Created on 13 Sep 2006
 */
package uk.org.ponder.rsf.template;


public class XMLCompositeViewTemplate extends GenericXMLViewTemplate {
  public XMLViewTemplate roottemplate;
  
  public XMLCompositeViewTemplate() {
    mustcollectmap = new XMLLumpMMap();
  }
}
