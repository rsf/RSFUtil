/*
 * Created on Oct 20, 2004
 */
package uk.org.ponder.jsfutil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.org.ponder.errorutil.MessageLocator;
import uk.org.ponder.jsfutil.View;
import uk.org.ponder.webapputil.ViewParameters;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ViewCollection {
  private ViewParameters vpexemplar;
  private MessageLocator messagelocator;
  public void setViewParametersExemplar(ViewParameters vpexemplar) {
    this.vpexemplar = vpexemplar;
  }
  
  public ViewParameters createViewParameters() {
    return vpexemplar.copyBase();
  }
  
  public void setMessageLocator(MessageLocator messagelocator) {
    this.messagelocator = messagelocator;
  }
  
  private Map views = new HashMap();
  private View defaultview;
//  public Map getViews() {
//    return views;
//  }
  public void setViews(List viewlist) {
    for (Iterator it = viewlist.iterator(); it.hasNext();) {
      View view = (View)it.next();
      view.setMessageLocator(messagelocator);
      views.put(view.getViewID(), view);
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
