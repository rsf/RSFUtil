/*
 * Created on 11 Sep 2007
 */
package uk.org.ponder.rsf.view;

import uk.org.ponder.rsf.producers.LayoutProducer;

/** The type of a bean definition "holding" an outer, or "layout" template.
 * As well as the LayoutProducer itself, it also holds a {@link ViewGroup} 
 * specification, or an EL path where one may be found.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class LayoutProducerHolder {
  private String viewGroupName;
  private ViewGroup viewGroup;
  
  private LayoutProducer producer;

  public String getViewGroupName() {
    return viewGroupName;
  }

  public void setViewGroupName(String viewGroupName) {
    this.viewGroupName = viewGroupName;
  }

  public ViewGroup getViewGroup() {
    return viewGroup;
  }

  public void setViewGroup(ViewGroup viewGroup) {
    this.viewGroup = viewGroup;
  }

  public LayoutProducer getProducer() {
    return producer;
  }

  public void setProducer(LayoutProducer producer) {
    this.producer = producer;
  }
}
