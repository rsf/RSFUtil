/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.opacity;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.beanutil.WriteableBeanLocator;

public class OpaqueBL implements WriteableBeanLocator {
  private Map map = new HashMap();

  public OpaqueBL() {
    map.put("string", "string");
    map.put("stringarray", new String[] {"string1", "string2"});
    map.put("nullarray", new String[] {null, "string"});
    map.put("inconvertible", new Inconvertible());
  }
  
  public Object locateBean(String name) {
    return map.get(name);
  }

  public boolean remove(String beanname) {
    return false;
  }

  public void set(String beanname, Object toset) {
    if (toset instanceof String[]) {
      String[] setarr = (String[]) toset;
      for (int i = 0; i < setarr.length; ++ i) {
        if (setarr[i] == null) {
          throw new IllegalArgumentException("Array containing null elements has been applied msg=invalid.data");
        }
      }
    }
    map.put(beanname, toset);
  }
}
