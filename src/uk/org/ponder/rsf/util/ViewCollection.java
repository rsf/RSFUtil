/*
 * Created on Oct 20, 2004
 */
package uk.org.ponder.jsfutil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.jsfutil.View;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ViewCollection {
  private Map views = new HashMap();
  private View defaultview;
  public Map getViews() {
    return views;
  }
  public void setViews(Map views) {
    this.views = views;
    for (Iterator it = views.keySet().iterator(); it.hasNext();) {
      String viewid = (String)it.next();
      View view = getView(viewid);
      view.setViewID(viewid);
    }
  }
  public View getView(String viewid) {
    return (View)views.get(viewid);
  }
  public void setDefaultView(View defaultview) {
    this.defaultview = defaultview;
  }
  
  public View getDefaultView() {
    return defaultview;
  }
  
}
