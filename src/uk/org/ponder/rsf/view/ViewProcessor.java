/*
 * Created on Nov 2, 2005
 */
package uk.org.ponder.rsf.view;

import java.util.Iterator;
import java.util.List;

import uk.org.ponder.rsf.componentprocessor.ComponentProcessor;
import uk.org.ponder.rsf.components.ComponentList;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.util.Logger;

/** A request-scope bean which processes a component tree (view) 
 * after its production by the ViewGenerator, and before rendering by the
 * ViewRender. This performs "fixups" as specified by a list of ComponentProcessor
 * objects, including applying the system's form model, resolving URLs and any
 * application-registered processing.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class ViewProcessor {
  private List processors;
  
  public void setComponentProcessors(List processors) {
    this.processors = processors;
  }
  
  // List of components found in the tree. We may
  // not use the tree directly because of possible folding mutations.
  private ComponentList worklist;
  
  private View view;
  
  public void setView(View view) {
    this.view = view;
  }
  public View getView() {
    performFixup();
    return view;
  }
  
  private void performFixup() {
    generateWorkList();
    // ensure that the ID map is fully populated before any processors begin
    // to execute, which may be dependent on it.
    for (int compind = 0; compind < worklist.size(); ++ compind) {
      UIComponent child = worklist.componentAt(compind);
      view.registerComponent(child);
    }
    for (int compind = 0; compind < worklist.size(); ++ compind) {
      UIComponent child = worklist.componentAt(compind);
      for (int procind = 0; procind < processors.size(); ++ procind) {
        ComponentProcessor proc = (ComponentProcessor) processors.get(procind);
        try {
          proc.processComponent(child);
        }
        catch (Exception e) {
          Logger.log.warn("Error processing component " + child.getFullID(), e);
        }
      }
    }
    
  }
  private void generateWorkList() {
    worklist = new ComponentList();
    appendContainer(view.viewroot);
    
  }
  private void appendContainer(UIContainer toappend) {
    ComponentList thischildren = toappend.flattenChildren();
    worklist.addAll(thischildren);
    for (int i = 0; i < thischildren.size(); ++ i) {
      UIComponent thischild = thischildren.componentAt(i);
      if (thischild instanceof UIContainer) {
        appendContainer(toappend);
      }
    }
  }
  
}
