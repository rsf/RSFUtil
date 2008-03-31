/*
 * Created on 31 Mar 2008
 */
package uk.org.ponder.rsf.renderer.message;

import java.text.MessageFormat;

public class MessageUtil {
  public static final String NOT_FOUND = "[Message for key {0} not found]";
  
  public static String renderDefaultMessage(String key) {
    MessageFormat mf = new MessageFormat(NOT_FOUND);
    return mf.format(new Object[] {key});
  }
}
