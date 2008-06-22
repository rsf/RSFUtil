/*
 * Created on 22 Jun 2008
 */
package uk.org.ponder.rsf.test.badtree;

import uk.org.ponder.rsf.processor.FatalErrorHandler;
import uk.org.ponder.rsf.processor.support.DefaultFatalErrorHandler;
import uk.org.ponder.streamutil.write.PrintOutputStream;

public class RecordingFatalErrorHandler implements FatalErrorHandler {
  public Throwable error;
  public String handleFatalError(Throwable t, PrintOutputStream pos) {
    error = t;
    return DefaultFatalErrorHandler.handleFatalErrorStatic(t, pos);
  }

}
