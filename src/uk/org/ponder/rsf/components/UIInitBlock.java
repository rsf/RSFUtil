/*
 * Created on 3 Sep 2007
 */
package uk.org.ponder.rsf.components;

/**
 * A component which will render an "initialisation block" to contextualise the
 * environment on the client side. This abstractly consists of the invocation of
 * a single client-side function call, with a set of arguments. For example, in
 * a HTML/Javascript environment, this would peer with a &lt;script&gt; and
 * render a single Javascript call, with arguments encoded as Strings or JSON.
 * For more information, see RSJCIS on
 * http://www2.caret.cam.ac.uk/rsfwiki/Wiki.jsp?page=BuildingRSFComponents
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class UIInitBlock extends UIVerbatim {
  /** The name of the function to be invoked on the client * */
  public String functionname;
  /** The arguments to be supplied to the invocation * */
  public Object[] arguments;

  /** Construct a client-side initialisation block accepting a single argument.
   * @see #make(UIContainer, String, String, Object[])
   */
  public static UIInitBlock make(UIContainer parent, String ID,
      String functionname, Object argument) {
    if (argument == null || functionname == null) {
      throw new NullPointerException("Cannot supply null argument or function name to UIInitBlock");
    }
    return make(parent, ID, functionname, new Object[] { argument });
  }

  /** Construct a client-side initialisation block. 
   * @param parent The parent container in which this init block is to be placed.
   * @param ID The ID to be given to this block.
   * @param functionname The client-side name of the function to be invoked when this block is encountered.
   * @param arguments An array of arguments to be passed to the client-side function.
   * Arguments may be of the types:
   * <ul>
   *   <li> Leaf types - these will be converted to String values by the standard conversion
   *   <li> UIComponents - these will be rendered as their own fullIDs.
   *   <li> ViewParameters - these will be converted to "fragment URLs", suitable for 
   *   rendering direct (unportalised) versions of the views they address.
   * </ul>
   */
  public static UIInitBlock make(UIContainer parent, String ID,
      String functionname, Object[] arguments) {
    if (arguments == null || functionname == null) {
      throw new NullPointerException("Cannot supply null arguments or function name to UIInitBlock");
    }
    UIInitBlock togo = new UIInitBlock();
    togo.ID = ID;
    togo.functionname = functionname;
    togo.arguments = arguments;
    
    parent.addComponent(togo);
    return togo;
  }

}
