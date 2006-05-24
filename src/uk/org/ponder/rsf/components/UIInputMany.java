/*
 * Created on Jan 16, 2006
 */
package uk.org.ponder.rsf.components;

public class UIInputMany extends UIBoundList {
  public UIInputMany () {
    fossilize = true;
    willinput = true;
  }
  
  /**
   * A "bare" constructor suitable for the selection member of a multiple
   * selection control (UIInput);
   */
  public static UIInputMany make(String valuebinding) {
    UIInputMany togo = new UIInputMany();
    togo.valuebinding = new ELReference(valuebinding);
    return togo;
  }
}
