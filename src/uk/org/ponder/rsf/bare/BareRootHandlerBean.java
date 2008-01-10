/*
 * Created on 9 Jan 2008
 */
package uk.org.ponder.rsf.bare;

import uk.org.ponder.rsf.processor.RootHandlerBeanBase;
import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.streamutil.write.StringPOS;

public class BareRootHandlerBean extends RootHandlerBeanBase {
  public static final ViewParameters NO_REDIRECT = new SimpleViewParameters();
  private StringPOS pos = new StringPOS();
  private AnyViewParameters redirect = NO_REDIRECT;

  public void issueRedirect(AnyViewParameters viewparamso, PrintOutputStream pos) {
    redirect = viewparamso;
  }

  public PrintOutputStream setupResponseWriter() {
    return pos;
  }

  public String getMarkup() {
    return pos.toString();
  }
  
  public AnyViewParameters getRedirectTarget() {
    return redirect;
  }
  
}
