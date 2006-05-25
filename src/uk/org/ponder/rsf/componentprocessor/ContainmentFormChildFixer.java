/*
 * Created on Nov 2, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import java.util.Iterator;

import uk.org.ponder.beanutil.IterableBeanLocator;
import uk.org.ponder.rsf.components.ComponentList;
import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.rsf.request.SubmittedValueEntry;
import uk.org.ponder.rsf.util.RSFUtil;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;
import uk.org.ponder.stringutil.StringList;

/**
 * A fixer to be run BEFORE the main form fixer, which implements the HTML/HTTP
 * form model whereby ALL nested child controls of the form are submitted. This
 * fixer will perform no action if it discovers that the "submittingcontrols"
 * fields of {@see uk.org.ponder.rsf.components.UIForm} has already been filled
 * in.
 * <p>
 * This fixer is HTML/HTTP SPECIFIC, and should not execute for other idioms.
 * <p>
 * Note that it also is responsible for setting the "Submitting Control" packed
 * attribute for UICommand objects.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ContainmentFormChildFixer implements ComponentProcessor {

  private SAXalizerMappingContext mappingcontext;

  public void setMappingContext(SAXalizerMappingContext mappingcontext) {
    this.mappingcontext = mappingcontext;
  }

  public void processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIForm) {
      UIForm toprocess = (UIForm) toprocesso;
      System.out.println("Top level form: " + toprocess);
      if (toprocess.submittingcontrols == null) {
        toprocess.submittingcontrols = new StringList();
        registerContainer(toprocess, toprocess);
      }
    }
  }

  private void registerComponent(UIForm toprocess, UIComponent child) {
    // TODO: produce some useful diagnostic on an attempt to create a nested
    // form. This is "presumably" forbidden in every dialect but HTML, and
    // "certainly" forbidden in HTML, even though it actually seems to work
    // in practice (apart from double-registration of SUBMITTING_CONTROL &c).
    // The problem is there is currently no (portable) place for this
    // housekeeping information since we abolished FormModel.
    if (child instanceof UIBound) {
      boolean getform = toprocess.type
          .equals(EarlyRequestParser.RENDER_REQUEST);
      if (RSFUtil.isBound(child) || getform) {
        String fullID = child.getFullID();
        String formID = toprocess.getFullID();
        UIBound bound = (UIBound) child;
        bound.submittingname = fullID.substring(RSFUtil.commonPath(fullID,
            formID));

        if (getform) {
          // slight "hack" to make cluster components in GET forms work
          // correctly - presumably there is only ONE of them that will actually
          // try to submit an HTML value.
          int hypos = bound.submittingname.indexOf('-');
          if (hypos != -1) {
            bound.submittingname = bound.submittingname.substring(0, hypos);
          }
        }

        toprocess.submittingcontrols.add(fullID);
      }
    }
    // TODO: clarify UIForm/UICommand relationship for WAP-style forms.
    // Who is to be master!
    // we expect that UIForm -> do, UICommand -> go
    // http://www.codehelp.co.uk/html/wap1.html
    else if (child instanceof UICommand) {
      toprocess.submittingcontrols.add(child.getFullID());
      // add the notation explaining which control is submitting, when it does
      UICommand command = (UICommand) child;
      command.parameters.add(new UIParameter(
          SubmittedValueEntry.SUBMITTING_CONTROL, child.getFullID()));
      if (command.methodbinding != null) {
        System.out.println("Registering " + child + " with "
            + toprocess.getFullID());
        command.parameters
            .add(new UIParameter(SubmittedValueEntry.FAST_TRACK_ACTION,
                command.methodbinding.value));
      }
    }
    if (child instanceof UIContainer) {
      registerContainer(toprocess, (UIContainer) child);
    }
    IterableBeanLocator children = new ComponentChildIterator(child,
        mappingcontext);
    for (Iterator childit = children.iterator(); childit.hasNext();) {
      UIComponent nested = (UIComponent) children.locateBean((String) childit
          .next());
      registerComponent(toprocess, nested);
    }
  }

  private void registerContainer(UIForm toprocess, UIContainer toregister) {
    ComponentList children = toregister.flattenChildren();
    for (int i = 0; i < children.size(); ++i) {
      UIComponent child = children.componentAt(i);
      registerComponent(toprocess, child);
    }
  }
}
