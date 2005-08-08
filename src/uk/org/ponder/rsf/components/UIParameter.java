/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.components;

/**
 * Typically used for binding "fast bean" parameters. This is registered
 * as an "only child" of an input
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UIParameter extends UIComponent {
  
  // these parameters are optional, and ensure that a bean
  // referenced by this component will be initialised with any context.
  // the name is the EL, probably referring to the same bean as above,
  // and the value will be assigned to it BEFORE any user values are applied.
  
  // However, on interpretation, fast_bind_name will be interpreted as an
  // EL with #{ } escaping, so setting this fields is a method which technically
  // allows arbitrary request parameters to be set when the field containing
  // this form is submitted.
  public String name;
  public String value;
}
