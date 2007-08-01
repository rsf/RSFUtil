/*
 * Created on Sep 10, 2006
 */
package uk.org.ponder.rsf.preservation;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.rsf.state.TokenStateHolder;
import uk.org.ponder.stringutil.StringList;

public interface GenericBeanCopyPreservationStrategy extends
    TSHPreservationStrategy {

  public void setPreservingBeans(StringList beannames);

  public void setTokenStateHolder(TokenStateHolder holder);

  public void setBeanModelAlterer(BeanModelAlterer alterer);

  public int preserveImmediate(BeanLocator source, 
      StringList beannames, String tokenid);

}