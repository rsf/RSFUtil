/*
 * Created on Nov 23, 2004
 */
package uk.org.ponder.rsf.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RequestSubmittedValueCache implements Serializable {
  public void addEntry(SubmittedValueEntry sve) {
    if (sve.componentid != null) {
      idmap.put(sve.componentid, sve);
    }
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
  private Map idmap = new HashMap();
  private Map pathmap = new HashMap();
  private Map upstream;
  
  public void setUpstreamMap(Map upstream) {
    this.upstream = upstream;
  }
  
  public SubmittedValueEntry getUpstreamComponent(String writepath) {
    return (SubmittedValueEntry) upstream.get(writepath);
  }
  /** The list of entries, in order of application.
   */
  private List entries = new ArrayList();
  public int getEntries() {
    return entries.size();
  }
  public SubmittedValueEntry entryAt(int i) {
    return (SubmittedValueEntry) entries.get(i);
  }
  public RequestSubmittedValueCache copy() {
    RequestSubmittedValueCache togo = new RequestSubmittedValueCache();
    for (int i = 0; i < entries.size(); ++ i) {
      togo.addEntry(entryAt(i));
    }
    return togo;
  }
}
