/*
 * Created on 5 Jul 2006
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.errorutil.ErrorUtil;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.util.Logger;

/**
 * The default FatalErrorHandler for RSF. In addition to logic to print a hardwired
 * completelly fail-safe message, also contains definition of the strategy to 
 * be used for user-defined strategies as a first line. 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class DefaultFatalErrorHandler implements FatalErrorHandler {
  public static boolean handleFatalErrorStatic(Throwable t,
      PrintOutputStream pos) {
    // We may have such a fatal misconfiguration that we can't even rely on
    // IKAT to format this error message
    Logger.log.fatal("Completely fatal error populating view root", t);

    pos.println("<html><head><title>Internal Error</title></head></body><pre>");
    pos.println("Fatal internal error handling request: " + t);
    ErrorUtil.dumpStackTrace(t, pos);
    pos.println("</pre></body></html>");
    pos.close();
    return true;
  }

  public static void handleFatalErrorStrategy(FatalErrorHandler handler,
      Throwable t, PrintOutputStream pos) {
    boolean rendered = false;
    Throwable failed = null;
    try {
      rendered = handler.handleFatalError(t, pos);
    }
    catch (Throwable t2) {
      failed = t2;
    }
    if (!rendered || failed != null) {
      handleFatalErrorStatic(t, pos);
    }
  }

  public boolean handleFatalError(Throwable t, PrintOutputStream pos) {
    return handleFatalErrorStatic(t, pos);
  }

}
