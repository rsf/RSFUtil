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
import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.rsf.util.RSFUtil;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsCodec;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.Logger;

/**
 * A fixer to be run BEFORE the main form fixer, which implements the HTML/HTTP
 * form model whereby ALL nested child controls of the form are submitted. This
 * fixer will perform no action if it discovers that the "submittingcontrols"
 * fields of {@link uk.org.ponder.rsf.components.UIForm} has already been filled
 * in.
 * <p>
 * This fixer is HTML/HTTP SPECIFIC, and should not execute for other idioms. It
 * typically executes as the very first in the pipeline if it executes at all.
 * <p>
 * Note that it also is responsible for setting the "Submitting Control" packed
 * attribute for UICommand objects.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ContainmentFormChildFixer implements ComponentProcessor {

  private SAXalizerMappingContext mappingcontext;
  private ViewParamsCodec vpcodec;
  private FormModel formModel;

  public void setFormModel(FormModel formModel) {
    this.formModel = formModel;
  }

  public void setMappingContext(SAXalizerMappingContext mappingcontext) {
    this.mappingcontext = mappingcontext;
  }

  public void setViewParamsCodec(ViewParamsCodec vpcodec) {
    this.vpcodec = vpcodec;
  }

  public void processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIForm) {
      UIForm toprocess = (UIForm) toprocesso;
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
      // in the case of an "unmanaged" form, this will generate submitting names
      // that the processor is "not expecting" in repetitious cases.
      UIBound bound = (UIBound) child;
      String fullID = child.getFullID();
      if (bound.willinput) {
        String formID = toprocess.getFullID();

        if (getform) {
          if (bound.valuebinding != null) {
            ViewParameters viewparams = toprocess.viewparams;
            String attrname = vpcodec.getMappingInfo(toprocess.viewparams)
                .pathToAttribute(bound.valuebinding.value);
            if (attrname == null) {
              Logger.log.warn("Warning: Unable to look up path "
                  + bound.valuebinding.value + " in ViewParameters "
                  + viewparams.getClass() + " with parseSpec "
                  + viewparams.getParseSpec()
                  + ": falling back to ID-based strategy");
            }
            bound.submittingname = attrname;
          }
          if (bound.submittingname == null) {
            bound.submittingname = reduceGETSubmittingName(inferBaseSubmittingName(
                fullID, formID));
          }
        }
        else {
          bound.submittingname = fullID;
        }
        toprocess.submittingcontrols.add(fullID);
        formModel.registerChild(toprocess, bound);
      }
      else {
        // case of a non-inputting control that needs to be nonetheless located
        // on the client side. This is actually the non-HTML default.
        // bound.submittingname = fullID;
        // new interpretation - willinput = NO NAME
      }
    }
    // TODO: clarify UIForm/UICommand relationship for WAP-style forms.
    // Who is to be master!
    // we expect that UIForm -> do, UICommand -> go
    // http://www.codehelp.co.uk/html/wap1.html
    else if (child instanceof UICommand) {
      toprocess.submittingcontrols.add(child.getFullID());
      formModel.registerChild(toprocess, child);
    }
    if (child instanceof UIContainer) {
      registerContainer(toprocess, (UIContainer) child);
    }
    IterableBeanLocator children = new ConcreteChildIterator(child,
        mappingcontext);
    for (Iterator childit = children.iterator(); childit.hasNext();) {
      UIComponent nested = (UIComponent) children.locateBean((String) childit
          .next());
      registerComponent(toprocess, nested);
    }
  }

  private static String inferBaseSubmittingName(String fullID, String formID) {
    return fullID.substring(RSFUtil.commonPath(fullID, formID));
  }

  private static String reduceGETSubmittingName(String submittingname) {
    String[] comps = submittingname.split(":", -1);
    submittingname = "";
    for (int i = 0; i < comps.length; ++i) {
      // append prefix.localID. for each component that has one
      if ((i % 3) == 2) {
        if (comps[i].length() != 0)
          submittingname += comps[i - 2] + "." + comps[i] + ".";
      }
    }
    submittingname += comps[comps.length - 1];
    // slight "hack" to make cluster components in GET forms work
    // correctly - presumably there is only ONE of them that will actually
    // try to submit an HTML value.
    int hypos = submittingname.indexOf('-');
    if (hypos != -1) {
      submittingname = submittingname.substring(0, hypos);
    }
    return submittingname;
  }

  private void registerContainer(UIForm toprocess, UIContainer toregister) {
    ComponentList children = toregister.flattenChildren();
    for (int i = 0; i < children.size(); ++i) {
      UIComponent child = children.componentAt(i);
      registerComponent(toprocess, child);
    }
  }
}
