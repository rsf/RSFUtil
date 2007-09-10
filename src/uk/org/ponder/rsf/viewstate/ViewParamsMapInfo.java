/*
 * Created on 10 Sep 2007
 */
package uk.org.ponder.rsf.viewstate;

/**
 * Mapping information describing the mapping from URL attributes onto bean
 * paths for ViewParameters objects.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public interface ViewParamsMapInfo {

  public String pathToAttribute(String path);

  public String attributeToPath(String attribute);

}