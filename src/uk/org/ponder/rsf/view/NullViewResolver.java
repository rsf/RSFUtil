/*
 * Created on 13-Feb-2006
 */
package uk.org.ponder.rsf.view;

import java.util.List;

public class NullViewResolver implements ViewResolver{
  public List getProducers(String viewid) {
    return null;
  }

}
