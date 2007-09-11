/*
 * Created on 11 Sep 2007
 */
package uk.org.ponder.rsf.view.support;

import java.util.List;

import uk.org.ponder.beanutil.BeanGetter;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.producers.LayoutProducer;
import uk.org.ponder.rsf.producers.NullaryProducer;
import uk.org.ponder.rsf.view.LayoutProducerHolder;
import uk.org.ponder.rsf.view.ViewGroup;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Central collection point for LayoutProducers. Focuses them from around the
 * context, evaluates which is to be selected, contextualises and executes it.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class LayoutCollector implements NullaryProducer {

  private List holders;
  private NullaryProducer upstream;
  private ViewGroupResolver viewGroupResolver;
  private BeanGetter ELevaluator;
  private ViewParameters viewParameters;

  public void setHolders(List holders) {
    this.holders = holders;
  }
  
  public void setUpstreamProducer(NullaryProducer upstream) {
    this.upstream = upstream;
  }

  public void setViewGroupResolver(ViewGroupResolver viewGroupResolver) {
    this.viewGroupResolver = viewGroupResolver;
  }
  
  public void setELEvaluator(BeanGetter ELevaluator) {
    this.ELevaluator = ELevaluator;
  }
  
  public void setViewParameters(ViewParameters viewParameters) {
    this.viewParameters = viewParameters;
  }
  
  public void fillComponents(UIContainer tofill) {
    
    if (holders != null) {
      for (int i = 0; i < holders.size(); ++ i) {
        LayoutProducerHolder holder = (LayoutProducerHolder) holders.get(i);
        ViewGroup group = holder.getViewGroup();
        if (holder.getViewGroupName() != null) {
          group = (ViewGroup) ELevaluator.getBean(holder.getViewGroupName());
        }
        if (viewGroupResolver.isMatch(group, viewParameters)) {
          LayoutProducer toact = holder.getProducer();
          toact.setPageProducer(upstream);
          toact.fillComponents(tofill);
          return;
        }
      }
      
    }
    upstream.fillComponents(tofill);
  }


}
