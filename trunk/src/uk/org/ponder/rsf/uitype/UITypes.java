/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.uitype;

import java.util.HashMap;

public class UITypes {
  private static HashMap types = new HashMap();
  private static HashMap names = new HashMap();
  private static HashMap classes = new HashMap();
  
  public static void registerType(UIType toregister) {
    types.put(toregister.getPlaceholder(), toregister);
    names.put(toregister.getName(), toregister);
    classes.put(toregister.getPlaceholder().getClass(), toregister);
  }
  static {
    registerType(StringArrayUIType.instance);
    registerType(StringUIType.instance);
    registerType(BooleanUIType.instance);
  }
  public static boolean isPlaceholder(Object o) {
    UIType type = forObject(o);
    // Must compare by REFERENCE!
    return type == null? false : o == type.getPlaceholder();
  }
  /** Look up a UIType entry based on the class of the submitted object.
   * Returns <code>null</code> if this is not a recognised UIType.
   */
  public static UIType forObject(Object o) {
    return (UIType) classes.get(o.getClass());
  }
  public static UIType forName(String name) {
    return (UIType) names.get(name);
  }
}
