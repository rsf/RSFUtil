/*
 * Created on Jan 6, 2005
 */
package uk.org.ponder.rsf.util;

import java.util.StringTokenizer;

import uk.org.ponder.rsf.components.BasicComponentIDs;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelectItem;
import uk.org.ponder.rsf.components.UISelectOne;
import uk.org.ponder.rsf.state.SubmittedValueEntry;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.AssertionException;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RSFFactory {

  public static UIForm makeForm(UIContainer parent, String ID, ViewParameters viewparams) {
    UIForm togo = new UIForm();
    togo.ID = ID;
    if (parent.getActiveForm() != null) {
      throw new AssertionException("Error starting form - form already in progress");
    }
    parent.addComponent(togo);
    parent.startForm(togo);
    String fullURL = viewparams.getFullURL();
    togo.postURL = fullURL;
    int questind = fullURL.indexOf('?');
    if (questind != -1) {
      extraParametersToForm(fullURL.substring(questind + 1), parent);
    }
    return togo;
  }
  
  public static UIForm makeForm(UIContainer parent, ViewParameters viewparams) {
    return makeForm(parent, BasicComponentIDs.BASIC_FORM, viewparams);
  }
  
  public static UIContainer makeContainer(UIContainer parent, String ID) {
    UIContainer togo = new UIContainer();
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }
  
  public static UIContainer make2Container(UIContainer parent, String ID1, String ID2) {
    UIContainer togo1 = makeContainer(parent, ID1);
    UIContainer togo2 = makeContainer(togo1, ID2);
    return togo2;
  }
  
  public static UIOutput makeText(UIContainer parent, String ID,
      String binding, String initvalue) {
    UIOutput togo = new UIOutput();
    togo.text = initvalue;
    togo.valuebinding = binding;
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }

  public static UIOutput makeText(UIContainer parent, String ID,
      String initvalue) {
    return makeText(parent, ID, null, initvalue);
  }

  public static UIOutput makeText(UIContainer parent, String ID) {
    return makeText(parent, ID, null, null);
  }
  
  // leave initvalue null to perform "fixup" based on current bean contents.
  // this relies on some kind of "mini-submit" against current request scope
  // in order to initialise beans with non-user values.
  // Since we now have an explicit form model, it is easy to tell for a given
  // component, which "fast EL" will be doing early binding. This will cause
  // a sort of "roving mini-submit" when we use an XML view tree, during
  // stage "3" of a GET. This can never be confused with phase 1 of POST.
  public static UIInput makeField(UIContainer parent, String ID,
      String binding, String initvalue) {
    UIInput togo = new UIInput();
    togo.valuebinding = binding;
    togo.value = initvalue;
    togo.ID = ID;
    parent.addComponent(togo);
    RSFUtil.setFossilisedBinding(parent, togo, binding, initvalue);
    return togo;
  }


  public static UISelectOne makeSelect(UIContainer parent, String ID, StringList values,
      String selected, String valuebinding) {
    UISelectOne togo = new UISelectOne();
    togo.ID = ID;
    togo.value = selected;
    togo.choices = new UISelectItem[values.size()];

    for (int i = 0; i < values.size(); ++i) {
      String value = values.stringAt(i);
      togo.choices[i] = new UISelectItem(value, value);
    }
    togo.valuebinding = valuebinding;
    RSFUtil.setFossilisedBinding(parent, togo, valuebinding, selected);
    return togo;
  }

  
  // typically used to add a fast EL binding - remember to allow UIForm elements
  // to appear in the tree to indicate scope. They can be omitted if scope
  // agrees with repetition scope. Assume that it is impossible to achieve
  // a super-repetition scope. fast EL will be useful for non-input components
  // too.
  public static void addParameter(UIContainer parent, String name, String value) {
    parent.getActiveForm().hiddenfields.put(name, value);
  }
  
  // adds global parameters required for ALL consumer-rendered links, in
  // a form environment. 
  public static void extraParametersToForm(String extraparams,
      UIContainer parent) {
    Logger.log
        .info("Action link requires extra parameters from " + extraparams);
    StringTokenizer st = new StringTokenizer(extraparams, "&");
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      int eqpos = token.indexOf("=");
      String key = token.substring(0, eqpos);
      String value = token.substring(eqpos + 1);
      addParameter(parent, key, value);
      Logger.log.info("Added extra parameter key " + key + " value " + value
          + " to command link");
    }
  }

  /**
   * Creates a command link initiating the specified method binding on trigger,
   * but also backed by infrastructure to produce a GET redirect to the original
   * view requested in this cycle once the action has been handled. This depends
   * on the use of the custom ViewHandler "ClassViewHandler".
   * 
   * @param parent
   *          The parent component to which this action link will be added as a
   *          child.
   * @param text
   *          The text that will be rendered to the user on this component.
   * @param methodbinding
   *          A JSF EL expression representing the action to be triggered when
   *          the user activates this link.
   */
  public static UICommand makeCommandLink(UIContainer parent, String ID, String text,
      String methodbinding) {
    UICommand togo = new UICommand();
    togo.text = text;
    togo.ID = ID;
    RSFUtil.addCommandLinkParameter(togo, SubmittedValueEntry.FAST_TRACK_ACTION,
        methodbinding);

    if (parent.getActiveForm() == null) {
      throw new UniversalRuntimeException("Component " + parent
          + " does not have a form parent");
    }
    parent.addComponent(togo);
    // we do not bother to put return URL here, it should be computed
    // by the action from the current URL, which will be put into
    // the form action URL.
    return togo;
  }

  public static UICommand makeCommandLink(UIContainer parent, String ID,
      String methodbinding) {
    return makeCommandLink(parent, ID, null, methodbinding);
  }

  public static UILink makeLink(UIContainer parent, String ID, String text, String target) {
    UILink togo = new UILink();
    togo.ID = ID;
    togo.text = text;
    togo.target = target;
    parent.addComponent(togo);
    return togo;
  }
  
   public static UILink makeLink(UIContainer parent, String ID, String target) {
    return makeLink(parent, ID, null, target);
  }

}
