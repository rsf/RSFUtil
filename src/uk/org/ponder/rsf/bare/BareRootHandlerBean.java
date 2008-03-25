/*
 * Created on 9 Jan 2008
 */
package uk.org.ponder.rsf.bare;

import uk.org.ponder.rsf.processor.support.RootHandlerBeanBase;
import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.streamutil.write.StringPOS;

/** An implementation of the core RSF request cycle bean "rootHandlerBean"
 * suitable for a bare or test environment.
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public class BareRootHandlerBean extends RootHandlerBeanBase {
  private StringPOS pos = new StringPOS();
  private AnyViewParameters redirect;

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
