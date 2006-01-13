/*
 * Created on Dec 14, 2004
 */
package uk.org.ponder.rsf.viewstate;

import uk.org.ponder.beanutil.entity.EntityID;
import uk.org.ponder.stringutil.StringList;

/**
 * View parameters, which as well as defining the core viewID, are centred 
 * upon a particular entity, defined by Java class and entity ID.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class EntityCentredViewParameters extends ViewParameters {
  public static final String MODE_NEW = "new";
  public static final String MODE_EDIT = "edit";
  private static StringList attrfields = StringList.fromString("flowtoken, " +
        "endflow, errortoken, errorredirect, entity.ID, mode");
  public StringList getAttributeFields() {
   return attrfields;
  }

  public void clearParams() {
  }

  public EntityID entity;
  public String mode;
  
  public void parsePathInfo(String pathinfo) {
    String[] pathcomps = URLUtil.splitPathInfo(pathinfo);
    // remove leading / which is specced to be there
    viewID = pathcomps[1];
    entity.ID = pathcomps[2];
  }

  public String toPathInfo() {
    return "/" + viewID;
  }
  
  public ViewParameters copyBase() {
    // TODO: replace all this with deepClone from BMA
    EntityCentredViewParameters togo = (EntityCentredViewParameters) super.copyBase();
    EntityID neweid = new EntityID();
    neweid.entityname = togo.entity.entityname;
    neweid.ID = togo.entity.ID;
    return togo;
  }


}