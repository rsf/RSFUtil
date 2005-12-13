/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.components;

/**
 * Represents a control that will cause a non-idempotent (POST) request to the
 * server. The basic case where the command is rendered using a piece of
 * non-bound text is handled by filling in the "commandtext" field. For more
 * complex command contents including bound ones, leave this field as null 
 * and add rendering components as childen of the command. You may NOT set the
 * (navigation) target of a UICommand, by RSF design it will ALWAYS (in the
 * case of an HTTP/HTML render system) post to the same URL as the page on
 * which it is contained.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UICommand extends UISimpleContainer {
  public String methodbinding;
  public String commandtext;
  /**
   * Creates a command link initiating the specified method binding on trigger,
   * but also backed by infrastructure to produce a GET redirect to the original
   * view requested in this cycle once the action has been handled. This depends
   * on the use of the custom ViewHandler "ClassViewHandler". (Memorial comment)
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
  public static UICommand make(UIContainer parent, String ID, String text,
      String methodbinding) {
    UICommand togo = new UICommand();
    togo.commandtext = text;
    togo.ID = ID;
    togo.methodbinding = methodbinding;
    // TODO: do this at fixup
  
//    if (parent.getActiveForm() == null) {
//      throw new UniversalRuntimeException("Component " + parent
//          + " does not have a form parent");
//    }
    parent.addComponent(togo);
    return togo;
  }

  public static UICommand make(UIContainer parent, String ID,
      String methodbinding) {
    return make(parent, ID, null, methodbinding);
  }

  // a map of param/values to be surreptitiously added to the parameter
  // map during submission. We maintain these as single-valued here for
  // simplicity, but they will be upgraded to the standard String[] values
  // on delivery.
  public ParameterList parameters = new ParameterList();
}
