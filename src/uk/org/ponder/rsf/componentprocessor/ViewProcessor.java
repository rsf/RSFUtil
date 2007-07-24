/*
 * Created on Nov 2, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.org.ponder.rsf.components.ComponentList;
import uk.org.ponder.rsf.components.FixableComponent;
import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.decorators.UIBoundDecorator;
import uk.org.ponder.rsf.components.decorators.UIDecorator;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.rsf.view.ViewReceiver;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;
import uk.org.ponder.util.Logger;

/**
 * A request-scope bean which processes a component tree (view) after its
 * production by the ViewGenerator, and before rendering by the ViewRender. This
 * performs "fixups" as specified by a list of ComponentProcessor objects,
 * including applying the system's form model, resolving URLs and any
 * application-registered processing.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public class ViewProcessor {
  private List frameworkprocessors;

  private List clientprocessors;
  
  public void setComponentProcessors(List processors) {
    this.frameworkprocessors = processors;
  }
  
  public void setClientComponentProcessors(List clientprocessors) {
    this.clientprocessors = clientprocessors;
  }

  // List of components found in the tree. We may
  // not use the tree directly because of possible folding mutations.
  private ComponentList worklist;

  private View view;
  
  private SAXalizerMappingContext mappingcontext;

  // This dependency is not set via RSAC since it would execute too early.
  public void setView(View view) {
    this.view = view;
  }

  public View getProcessedView() {
    performFixup();
    return view;
  }
  
  public void setMappingContext(SAXalizerMappingContext mappingcontext) {
    this.mappingcontext = mappingcontext;
  }

  private void performFixup() {
    List processors = new ArrayList();
    if (clientprocessors != null) {
      processors.addAll(clientprocessors);
    }
    processors.addAll(frameworkprocessors);
    
    for (int procind = 0; procind < processors.size(); ++procind) {
      ComponentProcessor proc = (ComponentProcessor) processors.get(procind);
      if (proc instanceof ViewReceiver) {
        ((ViewReceiver) proc).setView(view);
      }
    }

    generateWorkList();
    // ensure that the ID map is fully populated before any processors begin
    // to execute, which may be dependent on it.
    for (int compind = 0; compind < worklist.size(); ++compind) {
      UIComponent child = worklist.componentAt(compind);
      view.registerComponent(child);
    }
    OrphanFinder finder = new OrphanFinder();
    for (int compind = 0; compind < worklist.size(); ++compind) {
      UIComponent child = worklist.componentAt(compind);
      for (int procind = 0; procind < processors.size(); ++procind) {
        ComponentProcessor proc = (ComponentProcessor) processors.get(procind);
        try {
          proc.processComponent(child);
        }
        catch (Exception e) {
          Logger.log.warn("Error processing component " + child + " with ID " + child.getFullID(), e);
        }
      }
      finder.processComponent(child);
    }
    for (int compind = 0; compind < worklist.size(); ++compind) {
      UIComponent thischild = worklist.componentAt(compind);
      if (thischild instanceof FixableComponent) {
        ((FixableComponent)thischild).fixupComponent();
      }
    }
    finder.report();
  }

  private void generateWorkList() {
    worklist = new ComponentList();
    appendContainer(view.viewroot);
  }

  private void appendComponent(UIBound toappend, String fullID) {
    if (toappend != null) {
      if (fullID != null) {
        toappend.updateFullID(fullID);
      }
      worklist.add(toappend);
    }
  }

  private void appendContainer(UIContainer toappend) {
    ComponentList thischildren = toappend.flattenChildren();
    // add the actual children later, to ensure dependent components resolved
    // first
    // BUT we require at the very least that Forms are processed before their
    // children, otherwise any work done by ContainmentFCF will NOT BE SEEN
    // in time!
    worklist.addAll(thischildren);

    for (int i = 0; i < thischildren.size(); ++i) {
      UIComponent thischild = thischildren.componentAt(i);
      if (thischild instanceof UIContainer) {
        appendContainer((UIContainer) thischild);
      }
      ComponentChildIterator children = new ComponentChildIterator(thischild, mappingcontext);
      for (Iterator childit = children.iterator(); childit.hasNext(); ) {
        String childname = (String) childit.next();
        appendComponent((UIBound) children.locateBean(childname), thischild.getFullID() + "-" + childname);
      }
      if (thischild.decorators != null) {
        for (int j = 0; j < thischild.decorators.size(); ++ j) {
          UIDecorator dec = thischild.decorators.decoratorAt(j);
          if (dec instanceof UIBoundDecorator) {
            appendComponent(((UIBoundDecorator)dec).acquireBound(), thischild.getFullID() + "-decorator" + i);
          }
        }
      }
    }
 
  }
}
