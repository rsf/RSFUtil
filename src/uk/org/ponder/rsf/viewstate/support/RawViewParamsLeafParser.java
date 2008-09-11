/*
 * Created on 11 Sep 2008
 */
package uk.org.ponder.rsf.viewstate.support;

import uk.org.ponder.conversion.LeafObjectParser;
import uk.org.ponder.rsf.viewstate.RawViewParameters;
import uk.org.ponder.rsf.viewstate.URLRewriter;

public class RawViewParamsLeafParser implements LeafObjectParser {

  private URLRewriter rewriter;
  
  public void setURLRewriter(URLRewriter rewriter) {
    this.rewriter = rewriter;
  }

  public Object copy(Object tocopyo) {
    RawViewParameters tocopy = (RawViewParameters) tocopyo;
    return new RawViewParameters(tocopy.URL);
  }

  public Object parse(String toparse) {
    return new RawViewParameters(toparse);
  }

  public String render(Object torendero) {
    RawViewParameters torender = (RawViewParameters) torendero;
    return rewriter.rewriteResourceURL(torender.URL, "");
  }

}
