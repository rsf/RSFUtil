/*
 * Created on Jul 28, 2005
 */
package uk.org.ponder.rsf.components;

/**
 * Input of a single String-typed value. Not UIInputOne for familiarity.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UIInput extends UIBoundString {
  public UIInput() {
    fossilize = true;
    willinput = true;
  }
  // leave initvalue as constructed to perform "fixup" based on current bean contents.
  // this relies on some kind of "mini-submit" against current request scope
  // in order to initialise beans with non-user values.
  // Since we now have an explicit form model, it is easy to tell for a given
  // component, which "fast EL" will be doing early binding. This will cause
  // a sort of "roving mini-submit" when we use an XML view tree, during
  // stage "3" of a GET. This can never be confused with phase 1 of POST.
  public static UIInput make(UIContainer parent, String ID,
      String binding, String initvalue) {
    UIInput togo = new UIInput();
    togo.valuebinding = ELReference.make(binding);
    if (initvalue != null) {
      togo.setValue(initvalue);
    }
    togo.ID = ID;
    parent.addComponent(togo);
    //RSFUtil.setFossilisedBinding(parent, togo, binding, initvalue);
    return togo;
  }
  
  public static UIInput make(UIContainer parent, String ID,
      String binding) {
    return make(parent, ID, binding, null);
  }
}
