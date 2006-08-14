/*
 * Created on 10 Aug 2006
 */
package uk.org.ponder.rsf.viewstate;

public class NullInternalURLRewriter implements InternalURLRewriter {

  public String rewriteActionURL(String original) {
    return original;
  }

  public String rewriteRenderURL(String original) {
    return original;
  }

}
