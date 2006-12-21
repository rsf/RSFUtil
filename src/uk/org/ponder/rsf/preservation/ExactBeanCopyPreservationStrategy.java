/*
 * Created on Sep 10, 2006
 */
package uk.org.ponder.rsf.preservation;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.state.TokenStateHolder;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.Logger;

/** An "exact" BCPS that preserves to precisely named paths within
 * the target TSH, rather than trying to maximise speed and compactness as
 * the traditional BCPS.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */
public class ExactBeanCopyPreservationStrategy implements
    StatePreservationStrategy, GenericBeanCopyPreservationStrategy {
  private StringList beannames;
  private TokenStateHolder holder;
  private BeanModelAlterer alterer;
  private StringList targetkeys;
  private boolean haskeys;

  public void setPreservingBeans(StringList beannames) {
    this.beannames = beannames;
  }

  public void setTargetPreservingKeys(StringList targetkeys) {
    this.targetkeys = targetkeys;
  }

  public void setTokenStateHolder(TokenStateHolder holder) {
    this.holder = holder;
  }

  public TokenStateHolder getTokenStateHolder() {
    return holder;
  }

  public void setBeanModelAlterer(BeanModelAlterer alterer) {
    this.alterer = alterer;
  }

  public int preserve(BeanLocator source, String tokenid) {
    int cbeans = 0;
    for (int i = 0; i < beannames.size(); ++i) {
      String beanname = beannames.stringAt(i);
      String targetkey = haskeys ? beanname
          : targetkeys.stringAt(i);
      Object bean = alterer.getBeanValue(beanname, source);
      if (bean != null) {
        holder.putTokenState(targetkey, bean);
        ++cbeans;
        Logger.log
            .info("BeanCopy preserved to path " + targetkey + ": " + bean);
      }
    }
    return cbeans;
  }

  public int restore(WriteableBeanLocator target, String tokenid) {
    TargettedMessageList messages = ThreadErrorState.getErrorState().messages;
    int cbeans = 0;
    for (int i = 0; i < targetkeys.size(); ++i) {
      String key = targetkeys.stringAt(i);
      Object bean = holder.getTokenState(key);
      if (bean != null) {
        String path = beannames.stringAt(i);
        alterer.setBeanValue(path, target, bean, messages, false);
        Logger.log.info("BeanCopy restored value " + bean + " from key " + key
            + " to path " + path);
        ++cbeans;
      }
    }
    return cbeans;
  }

  public void clear(String tokenid) {
    for (int i = 0; i < targetkeys.size(); ++i) {
      String key = targetkeys.stringAt(i);
      holder.clearTokenState(key);
    }
  }

}
