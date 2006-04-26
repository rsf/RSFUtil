/*
 * Created on 13-Jan-2006
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.beanutil.BeanUtil;

/** A special class to hold EL references so they may be detected in the
 * object tree. When held in this member, it is devoid of the packaging #{..}
 * characters - they are removed and replaced by the parser in transit from
 * XML form.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class ELReference {
  public ELReference() {}
  public ELReference(String value) {
    this.value = BeanUtil.stripELNoisy(value);
  }
  public String value;
  public static ELReference make(String value) {
    return value == null? null : new ELReference(value);
  }
}
