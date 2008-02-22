/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.components;

/**
 * A base class for any "pure" (componentless) bindings encoded in the 
 * component tree. The base function is that the name/value pair recorded
 * in this base class will be submitted as part of the request map.
 * These may appear as children of either UIForm components, or of UIComponent
 * components. Derived classes are UIELBinding, UIDeletionBinding etc.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UIParameter {
  public String name;
  public String value;

  public UIParameter() {}
  public UIParameter(String name, String value) {
    this.name = name;
    this.value = value;
  }
}
