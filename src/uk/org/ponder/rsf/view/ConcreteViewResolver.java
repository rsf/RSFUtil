/*
 * Created on Oct 20, 2004
 */
package uk.org.ponder.rsf.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.org.ponder.util.UniversalRuntimeException;

/**
 * A concrete implementation of ViewResolver which will resolve ComponentProducer
 * requests into a fixed collection of configured beans, set up by the 
 * setView() method. Can also fall back to a set of generic ViewResolvers
 * should initial lookup fail. This is the default RSF ViewResolver.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ConcreteViewResolver implements ViewResolver {
  // This key is used to identify a producer that *might* produce components
  // in all views. Upgrade this architecture once we think a little more about
  // how we might actually want to locate view templates and component
  // producers relative to views (view IDs or in general, ViewParams)
  public static final String ALL_VIEW_PRODUCER = "  all views  ";
  // private MessageLocator messagelocator;
  //  
  // public void setMessageLocator(MessageLocator messagelocator) {
  // this.messagelocator = messagelocator;
  // }

  private Map views = new HashMap();

  private List resolvers = new ArrayList();

  /**
   * Sets a static list of ViewComponentProducers which will be used as a first
   * pass to resolve requests for incoming views. Any plain ComponentProducers
   * will be added as default producers to execute for all views.
   */
  public void setViews(List viewlist) {
    for (Iterator it = viewlist.iterator(); it.hasNext();) {
      ComponentProducer view = (ComponentProducer) it.next();
      // view.setMessageLocator(messagelocator);
      String key = ALL_VIEW_PRODUCER;
      if (view instanceof ViewComponentProducer) {
        key = ((ViewComponentProducer) view).getViewID();
      }
      addView(key, view);

    }
  }

  /**
   * Sets a list of slave ViewResolvers which will be polled in sequence should
   * no static producer be registered, until the first which returns a non-null
   * result for this view.
   */
  public void setViewResolvers(List resolvers) {
    this.resolvers = resolvers;
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
    List specific = get(viewid);

    if (specific == null && resolvers != null) {
      for (int i = 0; i < resolvers.size(); ++i) {
        ViewResolver resolver = (ViewResolver) resolvers.get(i);
        specific = resolver.getProducers(viewid);
        if (specific != null) break;
      }
    }
    if (specific == null) {
      throw UniversalRuntimeException.accumulate(new ViewNotFoundException(),
          "Unable to resolve request for component tree for view " + viewid);
    }
    ArrayList togo = new ArrayList();
    List allproducers = get(ALL_VIEW_PRODUCER);
    if (allproducers != null) {
      togo.addAll(allproducers);
    }

    togo.addAll(specific);
    return togo;
  }

}
