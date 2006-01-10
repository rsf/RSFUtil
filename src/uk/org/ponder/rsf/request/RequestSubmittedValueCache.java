/*
 * Created on Nov 23, 2004
 */
package uk.org.ponder.rsf.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RequestSubmittedValueCache {
  public void addEntry(SubmittedValueEntry sve) {
    idmap.put(sve.componentid, sve);
    pathmap.put(sve.valuebinding, sve);
    entries.add(sve);
  }
  /** Used by RSVCFixer to fix up component values in view tree just
   * after production, and just before rendering, in the case this is
   * an erroneous submission being returned to the user.
   */
  public SubmittedValueEntry byID(String componentid) {
    return (SubmittedValueEntry)idmap.get(componentid);
  }
  /** Used so that we can fixup errors from this submission, which are
   * generated referring to bean paths, back onto component IDs.
   */
  public SubmittedValueEntry byPath(String beanpath) {
    return (SubmittedValueEntry) pathmap.get(beanpath);
  }
  private HashMap idmap = new HashMap();
  private HashMap pathmap = new HashMap();
  /** The list of entries, in order of application.
   */
  public List entries = new ArrayList();
  public SubmittedValueEntry entryAt(int i) {
    return (SubmittedValueEntry) entries.get(i);
  }
}
