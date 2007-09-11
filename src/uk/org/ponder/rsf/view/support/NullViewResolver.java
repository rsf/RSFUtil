/*
 * Created on 13-Feb-2006
 */
package uk.org.ponder.rsf.view.support;

import java.util.List;

import uk.org.ponder.rsf.view.ViewResolver;

public class NullViewResolver implements ViewResolver {
  public List getProducers(String viewid) {
    return null;
  }

}
