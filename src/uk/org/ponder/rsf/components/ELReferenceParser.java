/*
 * Created on 13-Jan-2006
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.conversion.LeafObjectParser;


public class ELReferenceParser implements LeafObjectParser {

  public Object parse(String toparse) {
    ELReference togo = new ELReference();
    togo.value = BeanUtil.stripEL(toparse);
    return togo;
  }

  public String render(Object torendero) {
    ELReference torender = (ELReference) torendero;
    return "#{" + torender.value + "}";
  }

  public Object copy(Object tocopyo) {
    ELReference tocopy = (ELReference) tocopyo;
    ELReference togo = new ELReference();
    togo.value = tocopy.value;
    return togo;
  }

}
