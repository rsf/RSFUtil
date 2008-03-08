/*
 * Created on Oct 20, 2004
 */
package uk.org.ponder.rsf.view.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ponder.booleanutil.BooleanGetter;
import uk.org.ponder.booleanutil.BooleanHolder;
import uk.org.ponder.rsf.flow.errors.SilentRedirectException;
import uk.org.ponder.rsf.view.ComponentProducer;
import uk.org.ponder.rsf.view.MappingViewResolver;
import uk.org.ponder.rsf.view.ViewResolver;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * A concrete implementation of ViewResolver which will resolve
 * ComponentProducer requests into a fixed collection of configured beans, set
 * up by the setViews() method. Can also fall back to a set of generic
 * ViewResolvers should initial lookup fail. This is the default RSF
 * ViewResolver - it is an application scope bean, although it collaborates with
 * the AutoComponentProducerManager to accept request-scope producers.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ConcreteViewResolver implements MappingViewResolver {
  private Map views = new HashMap();

  private List resolvers = new ArrayList();
  private BooleanGetter unknowniserror = new BooleanHolder(true);
  private AutoComponentProducerManager automanager;
  private ViewInfoDistributor viewInfoDistributor;

  public void setViewInfoDistributor(ViewInfoDistributor viewInfoDistributor) {
    this.viewInfoDistributor = viewInfoDistributor;
  }

  public void setUnknownViewIsError(BooleanGetter unknowniserror) {
    this.unknowniserror = unknowniserror;
  }

  private List pendingviews = new ArrayList();

  // Apologies for this lack of abstraction. There is currently only one of
  // these,
  // and we do use it for two purposes...
  public void setAutoComponentProducerManager(
      AutoComponentProducerManager requestmanager) {
    this.automanager = requestmanager;
    pendingviews.addAll(requestmanager.getProducers());
  }

  /**
   * Sets a static list of ViewComponentProducers which will be used as a first
   * pass to resolve requests for incoming views. Any plain ComponentProducers
   * will be added as default producers to execute for all views.
   */
  public void setViews(List viewlist) {
    pendingviews.addAll(viewlist);
  }
  
  public void init() {
    for (int i = 0; i < pendingviews.size(); ++i) {
      ComponentProducer view = (ComponentProducer) pendingviews.get(i);
      // view.setMessageLocator(messagelocator);
      String key = viewInfoDistributor.distributeInfo(view);
      addView(key, view);
    }
    pendingviews.clear();
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

  public ComponentProducer mapProducer(Object tomap) {
    return automanager.wrapProducer((ComponentProducer) tomap);
  }
  
  public List getUnmappedProducers(String viewid) {
    List specific = get(viewid);

    if (specific == null && resolvers != null) {
      for (int i = 0; i < resolvers.size(); ++i) {
        ViewResolver resolver = (ViewResolver) resolvers.get(i);
        specific = resolver.getProducers(viewid);
        if (specific != null)
          break;
      }
    }
    if (specific == null && unknowniserror.get() == Boolean.TRUE) {
      throw UniversalRuntimeException.accumulate(new SilentRedirectException(),
          "No ViewComponentProducer is registered for view " + viewid);
    }
    ArrayList togo = new ArrayList();
    List allproducers = get(ViewInfoDistributor.ALL_VIEW_PRODUCER);
    if (allproducers != null) {
      togo.addAll(allproducers);
    }
    if (specific != null) {
      togo.addAll(specific);
    } 
    return togo;
  }
  
  public List getProducers(String viewid) {
    List togo = getUnmappedProducers(viewid); 
    mapProducers(togo);
    return togo;
  }

  private void mapProducers(List producers) {
    for (int i = 0; i < producers.size(); ++i) {
      ComponentProducer producer = (ComponentProducer) producers.get(i);
      ComponentProducer ultimate = automanager.wrapProducer(producer);
      producers.set(i, ultimate);
    }
  }

}
