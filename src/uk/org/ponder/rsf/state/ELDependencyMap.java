/*
 * Created on Nov 8, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.stringutil.StringList;

public class ELDependencyMap {
  public static final List VALID_LIST_MARKER = new ArrayList();
  
  public static final String WRITE = "write";
  public static final String READ = "read";
  
  private HashMap writemap = new HashMap();
  private HashMap readmap = new HashMap();
  
  private HashMap mapread = new HashMap();
  public HashMap mapwrite = new HashMap();
  
  public static void recordMap(Map recordin, String upstring, SubmittedValueEntry sve) {
    ArrayList deps = (ArrayList) recordin.get(upstring);
    if (deps == null) {
      deps = new ArrayList();
      recordin.put(upstring, deps);
    }
    deps.add(sve); 
  }
 
  public String getReadPath(SubmittedValueEntry sve) {
    return (String) mapread.get(sve);
  }
  
  public String getWritePath(SubmittedValueEntry sve) {
    return (String) mapwrite.get(sve);
  }
  
  public void recordWrite(String upstring, SubmittedValueEntry sve) {
    recordMap(writemap, upstring, sve);
    mapwrite.put(sve, upstring);
  }
  
  public void recordRead(String upstring, SubmittedValueEntry sve) {
    recordMap(readmap, upstring, sve);
    mapread.put(sve, upstring);
  }
  
  public List getReaders(String path) {
    return (List) readmap.get(path);
  }
  
  public List getWriters(String path) {
    return (List) writemap.get(path);
  }
  
  public void recordPathValid(String path) {
    writemap.put(path, VALID_LIST_MARKER);
  }
  
  /** Disused method */
  public List getInvalidatingEntries(String path) {
    ArrayList togo = new ArrayList();
    String[] splitEL = BeanUtil.splitEL(path);
    StringList goup = new StringList();
    for (int i = 0; i < splitEL.length; ++ i) {
      goup.add(splitEL[i]);
      String upstring = BeanUtil.composeEL(goup);
      List deps = (List) writemap.get(upstring);
      if (deps != null) {
        togo.addAll(deps);
      }
    }
    return togo;
  }
}
