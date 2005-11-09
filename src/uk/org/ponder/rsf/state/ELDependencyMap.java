/*
 * Created on Nov 8, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.stringutil.StringList;

public class ELDependencyMap {
  public static final String WRITE = "write";
  public static final String READ = "read";
  
  private HashMap map = new HashMap();
 
  public void recordWrite(String upstring, SubmittedValueEntry sve) {
    ArrayList deps = (ArrayList) map.get(upstring);
    if (deps == null) {
      deps = new ArrayList();
      map.put(upstring, deps);
    }
    deps.add(sve);
  }
  
  public List getInvalidatingEntries(String path) {
    ArrayList togo = new ArrayList();
    String[] splitEL = BeanUtil.splitEL(path);
    StringList goup = new StringList();
    for (int i = 0; i < splitEL.length; ++ i) {
      goup.add(splitEL[i]);
      String upstring = BeanUtil.composeEL(goup);
      List deps = (List) map.get(upstring);
      if (deps != null) {
        togo.addAll(deps);
      }
    }
    return togo;
  }
}
