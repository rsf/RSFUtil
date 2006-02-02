/*
 * Created on Nov 24, 2005
 */
package uk.org.ponder.rsf.view;


import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.expander.TemplateExpander;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class XMLViewComponentProducer implements ViewComponentProducer {
  private String viewID;
  private UIContainer templatecontainer;
  private TemplateExpander templateexpander;
  public long lastchecked; // millisecond datestamp that filesystem freshness was checked
  public long modtime; // modification time of the file giving rise to current tree
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
    templateexpander.expandTemplate(tofill, templatecontainer);
  }

}
