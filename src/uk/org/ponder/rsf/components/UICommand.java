/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.beanutil.PathUtil;

/**
 * Represents a control that will cause a non-idempotent (POST) request to the
 * server. The basic case where the command is rendered using a piece of
 * non-bound text is handled by filling in the "commandtext" field. For more
 * complex command contents including bound ones, leave this field as null and
 * add rendering components as childen of the command. You may NOT set the
 * (navigation) target of a UICommand, by RSF design it will ALWAYS (in the case
 * of an HTTP/HTML render system) post to the same URL as the page on which it
 * is contained.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UICommand extends UIParameterHolder {
  /**
   * The EL reference of the action/method binding to be invoked when this
   * control is operated.
   */
  public ELReference methodbinding;
  /** The text labelling this command control */
  public UIBoundString commandtext;

  //Creates a command link initiating the specified method binding on trigger,
  //but also backed by infrastructure to produce a GET redirect to the original
  //view requested in this cycle once the action has been handled. This depends
  //on the use of the custom ViewHandler "ClassViewHandler". (Memorial comment)
  
  /** Creates a command link which will initiate a non-idempotent request 
   * (action cycle) to handle the command.
   * @param parent The parent component to which this action link will be added
   *          as a child.
   * @param text The text that will be rendered to the user on this component.
   * @param methodbinding An RSF EL expression representing the action to be
   *          triggered when the user activates this link.
   */
  public static UICommand make(UIContainer parent, String ID, String text,
      String methodbinding) {
    UIBoundString commandtext = null;
    if (text != null) {
      commandtext = new UIOutput();
      commandtext.setValue(text);
    }
    return make(parent, ID, commandtext, methodbinding);
  }

  /**
   * Creates a command control which accepts a bound string (for example a
   * UIMessage) as the textual label.
   * 
   * @see #make(UIContainer, String, String, String)
   */
  public static UICommand make(UIContainer parent, String ID,
      UIBoundString commandtext, String methodbinding) {
    UICommand togo = new UICommand();
    togo.commandtext = commandtext;
    togo.ID = ID;
    togo.methodbinding = ELReference.make(methodbinding);
    // TODO: do this at fixup

    // if (parent.getActiveForm() == null) {
    // throw new UniversalRuntimeException("Component " + parent
    // + " does not have a form parent");
    // }
    parent.addComponent(togo);
    return togo;
  }

  /**
   * Construct a command control with a command text but no method binding.
   * @see #make(UIContainer, String, UIBoundString, String)
   */
  public static UICommand make(UIContainer parent, String ID,
      UIBoundString commandtext) {
    return make(parent, ID, commandtext, null);
  }

  /**
   * Construct a command control with a method binding, but the nested markup
   * unchanged from the template.
   * @see #make(UIContainer, String, String, String)
   */
  public static UICommand make(UIContainer parent, String ID,
      String methodbinding) {
    return make(parent, ID, (UIBoundString) null, methodbinding);
  }

  /**
   * Construct an "actionless" command link, suitable for a CRUD-type
   * application where the data alteration constitutes the entire action.
   */
  public static UICommand make(UIContainer parent, String ID) {
    return make(parent, ID, (UIBoundString) null, null);
  }

  /** Sets the method binding for this UICommand to perform no action but to
   * return the supplied value, should the cycle complete without errors. 
   * This will overwrite any existing method binding for the component.
   * @param returnvalue The required return value from the action cycle.
   * @return this component.
   */
  public UICommand setReturn(String returnvalue) {
    methodbinding = new ELReference(PathUtil.composePath("constantReturn", returnvalue));
    return this;
  }
  
}
