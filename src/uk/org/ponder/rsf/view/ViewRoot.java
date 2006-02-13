/*
 * Created on 13-Feb-2006
 */
package uk.org.ponder.rsf.view;

import java.util.List;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ViewRoot extends UIBranchContainer {
  public static final String VIEWROOT_ID = "  viewroot  ";
  
  public ViewRoot() {
    ID = VIEWROOT_ID;
  }
  
  public List navigationCases;
  
  public ViewParameters viewParameters;
  
  public boolean defaultview;
}
