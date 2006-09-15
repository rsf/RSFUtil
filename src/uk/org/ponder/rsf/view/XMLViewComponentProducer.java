/*
 * Created on Nov 24, 2005
 */
package uk.org.ponder.rsf.view;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.expander.TemplateExpander;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** A ViewComponentProducer that returns components deserialized from an
 * XML representation (after having been subject to a process of "expansion"
 * specified by the RSF "pseudocomponents" UISwitch and UIReplicator).
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class XMLViewComponentProducer implements ViewComponentProducer {
  private String viewID;
  private UIContainer templatecontainer;
  private TemplateExpander templateexpander;
  
  public void setViewID(String viewID) {
    this.viewID = viewID;
  }
  public String getViewID() {
    return viewID;
  }

  public void setTemplateExpander(TemplateExpander templateexpander) {
    this.templateexpander = templateexpander;
  }
  
  public void setTemplateContainer(UIContainer templatecontainer) {
    this.templatecontainer = templatecontainer;
  }
  
  public void fillComponents(UIContainer tofill, 
      ViewParameters origviewparams, ComponentChecker checker) {
    if (tofill instanceof ViewRoot && templatecontainer instanceof ViewRoot) {
      ViewRoot fillroot = (ViewRoot) tofill;
      ViewRoot templateroot = (ViewRoot) templatecontainer;
      fillroot.navigationCases = templateroot.navigationCases;
    }
    templateexpander.expandTemplate(tofill, templatecontainer);
  }

}
