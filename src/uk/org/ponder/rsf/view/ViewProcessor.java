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
  
  private UIContainer root;
  
  public void setRoot(UIContainer root) {
    this.root = root;
  }
  public UIContainer getRoot() {
    performFixup();
    return root;
  }
  private void performFixup() {
    generateWorkList();
    for (int compind = 0; compind < worklist.size(); ++ compind) {
      UIComponent child = worklist.componentAt(compind);
      for (int procind = 0; procind < processors.size(); ++ procind) {
        ComponentProcessor proc = (ComponentProcessor) processors.get(procind);
        int code = proc.processComponent(child);
      }
    }
    // TODO Auto-generated method stub
    
  }
  private void generateWorkList() {
    worklist = new ComponentList();
    appendContainer(root);
    
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
