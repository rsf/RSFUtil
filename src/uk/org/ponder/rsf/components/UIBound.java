/*
 * Created on Aug 9, 2005
 */
package uk.org.ponder.rsf.components;

/**
 * This is essentially the core RSF component.
 * 
 * Represents a single (possibly vector) value as transferred between the bean
 * model and the rendererd view. Unless this is a pure output component, the
 * value will be bound to an (EL) bean reference. This reference is used both on
 * render to give them their initial values, and on submission to apply the
 * input value to the model. <br>
 * Immediate descendents are UIInput(One) and UIInputMany, as well as UIOutput.
 * <br>
 * A bound value may well be the container for other bound values, or
 * annotations of other types. However, any containment hierarchy below this
 * level is invisible to IKAT, which hands off component subtrees to renderers
 * at anything below UIBranchContainer.
 * <p>
 * The most important and commonly set fields of UIBound are <code>value</code>
 * and <code>valuebinding</code>. The fields <code>willinput</code> and
 * <code>fossilize</code> are generally set by subclasses such as UIInput and
 * UIOutput used to define the nature of the component. The fields
 * <code>darshaper</code> and <code>resolver</code> are set in more advanced
 * scenarios where some type adjustment is required. Finally the fields
 * <code>fossilizedbinding</code> and <code>fossilizedshaper</code> should
 * not be set by client code - they are a convenient repository for rendered
 * bindings as they pass through the fixup stage.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public abstract class UIBound extends UIComponent {
  /**
   * The EL value reference that this component's value is bound to. This will
   * be a string of the form <code>#{rootbean.property1.property2}</code>
   */
  public ELReference valuebinding;
  /**
   * An EL reference to a "DAR reshaper" that should be used to adjust the value
   * of any attempt to write a value based on the valuebinding. The reference
   * should be to a bean of type {@link uk.org.ponder.mapping.DARReshaper}.
   * <p>
   * This is principally (currently) useful for selection controls, where a
   * write of the value (typically an Object ID) that was read via the
   * valuebinding would not have the "correct" effect on the object model. This
   * field will typically be <code>null</code>.
   */
  public ELReference darreshaper;
  /**
   * The "initial" or "current" value of the component. Since components in
   * general only exist between production and rendering, this value is a
   * transit between these processes and is not subject to any amusing
   * processing. In general, this value <it>may</it> be set an initial value by
   * the producer. If it is not set a value, <it>and</it> the
   * <code>valuebinding</code> is set, the value will be queried from the bean
   * model during the fixup phase.
   * <p>
   * I would dearly have loved to have made this value of differing concrete
   * types in subclasses (<code>boolean</code>, <code>String[]</code> &c)
   * but this created too much of a burden for fixup code. Also, fixups need to
   * be able to reliably distinguish a missing value <code>null</code> which
   * would be impossible with a primitive type.
   * <p>
   * Most importantly, <code>value</code> itself must never become
   * independently visible as an bean property to serializers, which should
   * instead only see the typesafe access functions defined in subclasses. For
   * this reason, the field itself is protected, and the accessors are called
   * <code>acquireValue</code> and <code>updateValue</code>
   */
  protected Object value;

  /**
   * Returns the "value" Object reference. This method and
   * <code>updateValue</code> are not named "get" and "set" to avoid confusing
   * bean serialisers.
   */
  public Object acquireValue() {
    return value;
  }

  /** Sets the "value" Object reference */
  public void updateValue(Object value) {
    this.value = value;
  }

  /** A reference to an object, or an object that can be converted to, a 
   * LeafRenderer or BeanResolver that can render the bound value above 
   * to and from a String representation, or applied to elements of the array
   * if a String array.  
   */
  public Object resolver;
  
  /**
   * A field recording whether the value of this binding at render time will be
   * "fossilized", i.e. recorded by the client and resubmitted with modified
   * values. Defaults to <code>true</code> for input components, and
   * <code>false</code> for output components.
   * <p>
   * Producers of output components with high consistency requirements (i.e.
   * those that will be used to provide critical values to users during multi-
   * requests should override the constructor default in UIOutput with
   * <code>true</code>.
   */
  public boolean fossilize = false;
  /**
   * A field recording whether any input is expected to result from this
   * component. Note that if this flag is set to <code>true</code>, the
   * <code>fossilize</code> flag MUST also be set to true.
   */
  public boolean willinput = false;
  /**
   * The key/value pair that will be submitted to implement the fossilized
   * binding. Component producers should NOT attempt to set this field, it will
   * be computed during a fixup.
   */
  public UIParameter fossilizedbinding;
  /**
   * The key/value pair of the binding corresponding to the DAR reshaper. Again,
   * computed during fixup stage if the darshaper is set.
   */
  public UIParameter fossilizedshaper;
  
  /** The key that this bound control will submit under. May be set during 
   * early fixup - if not, will default to the fullID of the component, unless
   * the renderer has special requirements (e.g. radio button group).
   */
  public String submittingname;
}
