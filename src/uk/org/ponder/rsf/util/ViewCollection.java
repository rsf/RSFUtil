/*
 * Created on Oct 20, 2004
 */
package uk.org.ponder.rsf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.org.ponder.errorutil.MessageLocator;
import uk.org.ponder.rsf.view.ComponentProducer;
import uk.org.ponder.rsf.view.ViewComponentProducer;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ViewCollection {
  // This key is used to identify a producer that *might* produce components
  // in all views. Upgrade this architecture once we think a little more about
  // how we might actually want to locate view templates and component 
  // producers relative to views (view IDs or in general, ViewParams)
  public static final String ALL_VIEW_PRODUCER = "  all views  ";
  private MessageLocator messagelocator;
  
  public void setMessageLocator(MessageLocator messagelocator) {
    this.messagelocator = messagelocator;
  }
  
  private Map views = new HashMap();
  private ViewComponentProducer defaultview;
//  public Map getViews() {
//    return views;
//  }
  public void setViews(List viewlist) {
    for (Iterator it = viewlist.iterator(); it.hasNext();) {
      ComponentProducer view = (ComponentProducer)it.next();
      view.setMessageLocator(messagelocator);
      String key = ALL_VIEW_PRODUCER;
      if (view instanceof ViewComponentProducer) {
        key = ((ViewComponentProducer)view).getViewID();
      }
      addView(key, view);
      
    }
  }
  private void addView(String key, ComponentProducer view) {
    List got = get(key);
    if (got == null) {
      got = new ArrayList();
      views.put(key, got);
    }
    got.add(view);
  }
  
  private List get(String key) {
    return (List) views.get(key);
  }
  
  public List getProducers(String viewid) {
    ArrayList togo = new ArrayList();
    togo.addAll(get(ALL_VIEW_PRODUCER));
    togo.addAll(get(viewid));
    return togo;
  }
  
  public void setDefaultView(ViewComponentProducer defaultview) {
    this.defaultview = defaultview;
  }
  
  public ViewComponentProducer getDefaultView() {
    return defaultview;
  }
  
}
