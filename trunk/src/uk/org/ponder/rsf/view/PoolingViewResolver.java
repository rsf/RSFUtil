/*
 * Created on 27-Feb-2006
 */
package uk.org.ponder.rsf.view;

import java.util.ArrayList;
import java.util.List;

/** A ViewResolver that will pool the results of two ViewResolvers into one */
public class PoolingViewResolver implements ViewResolver {

  private ViewResolver main;
  private ViewResolver fallback;

  public void setFirstResolver(ViewResolver main) {
    this.main = main;
  }
  
  public void setSecondResolver(ViewResolver fallback) {
    this.fallback = fallback;
  }
  
  public List getProducers(String viewid) {
    List togo = new ArrayList();
    List got = main.getProducers(viewid);
    togo.addAll(got);
    got = fallback.getProducers(viewid);
    togo.addAll(got);
    return togo;
  }

}
