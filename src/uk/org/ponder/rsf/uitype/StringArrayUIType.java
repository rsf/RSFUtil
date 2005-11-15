/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.uitype;

import uk.org.ponder.arrayutil.ArrayUtil;

public class StringArrayUIType implements UIType {
  public static final StringArrayUIType instance = new StringArrayUIType();
  private String[] PLACEHOLDER = new String[] {};

  public Object getPlaceholder() {
    return PLACEHOLDER;
  }

  public String getName() {
    return "stringarray";
  }

  public boolean valueUnchanged(Object oldvalue, Object newvalue) {
    String[] olds = (String[]) oldvalue;
    String[] news = (String[]) newvalue;
    return ArrayUtil.lexicalCompare(olds, olds.length, news, news.length) != 0;
  }

}
