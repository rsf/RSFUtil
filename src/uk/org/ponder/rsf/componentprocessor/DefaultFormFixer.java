/*
 * Created on Nov 2, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.rsf.view.ViewReceiver;
import uk.org.ponder.rsf.viewstate.ViewParamUtil;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;
import uk.org.ponder.stringutil.StringSet;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * The basic FormFixer implementation, with responsibilities for i) setting form
 * submission URL to current view URL ii) Folding form children up into parent,
 * leaving it as a leaf in the tree, iii) registering submitting nested UIBound
 * components into the FormModel. Point iii) must have ALREADY been set up by
 * having the <code>submittingcontrols</code> field of UIForm filled in
 * (possibly by ContainmentFormChildFixer)
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
// WHY do we treat forms in this strange way? Because we WOULD like IKAT to be
// form-agnostic, and also form nesting is not a "given" for WAP &c.
public class DefaultFormFixer implements ComponentProcessor, ViewReceiver {
  private ViewParameters viewparams;
  private ViewStateHandler viewstatehandler;
  private View view;
  private ParameterList outgoingparams;

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }

  public void setViewStateHandler(ViewStateHandler viewstatehandler) {
    this.viewstatehandler = viewstatehandler;
  }

  public void setView(View view) {
    this.view = view;
  }

  public void setOutgoingParams(ParameterList outgoingparams) {
    this.outgoingparams = outgoingparams;
  }

  public void processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIForm) {
      UIForm toprocess = (UIForm) toprocesso;
      toprocess.fold();

      StringSet submittingnames = new StringSet();

      // Check that anything registered so far as submitting exists and is
      // valid.
      for (int i = 0; i < toprocess.submittingcontrols.size(); ++i) {
        String childid = toprocess.submittingcontrols.stringAt(i);
        UIComponent child = view.getComponent(childid);
        if (!(child instanceof UIBound) && !(child instanceof UICommand)) {
          throw UniversalRuntimeException.accumulate(
              new IllegalArgumentException(), "Component with ID " + childid
                  + " listed as submitting child of form "
                  + toprocess.getFullID()
                  + " is not valid (non-Command, non-Bound or "
                  + "non-existent)");
        }
        if (child instanceof UIBound) {
          UIBound boundchild = (UIBound) child;
          if (boundchild.submittingname == null) {
            Logger.log.warn("Submitting name for " + boundchild.getFullID()
                + " not set by previos fixup");
            boundchild.submittingname = boundchild.getFullID();
          }
          submittingnames.add(boundchild.submittingname);
        }
        // formmodel.registerChild(toprocess, (UIBound) child);
      }
      // form will submit to current URL if none other specified
      if (toprocess.viewparams == null) {
        toprocess.viewparams = viewparams;
      }
      Map attrmap = viewstatehandler.getAttrMap(toprocess.viewparams);
      // remove any URL keys for which there exist controls for a GET form
      if (toprocess.type.equals(EarlyRequestParser.RENDER_REQUEST)) {
        for (Iterator keyit = attrmap.keySet().iterator(); keyit.hasNext();) {
          String key = (String) keyit.next();
          if (submittingnames.contains(key)) {
            keyit.remove();
          }
        }
        String fullURL = viewstatehandler.getFullURL(toprocess.viewparams);
        int qpos = fullURL.indexOf('?');
        if (qpos != -1)
          fullURL = fullURL.substring(0, qpos);
        toprocess.targetURL = fullURL;
      }
      else {
        toprocess.targetURL = viewstatehandler
            .getActionURL(toprocess.viewparams);
      }
      toprocess.parameters.addAll(ViewParamUtil.mapToParamList(attrmap));
      toprocess.parameters.addAll(outgoingparams);

    }
  }

}
