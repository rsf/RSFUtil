/*
 * Created on 5 Feb 2007
 */
package uk.org.ponder.rsf.view;

import java.util.List;

public interface MappingViewResolver extends ViewResolver {

  public ComponentProducer mapProducer(Object tomap);

  public List getUnmappedProducers(String viewid);

}