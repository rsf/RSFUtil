/*
 * Created on 5 Jul 2006
 */
package uk.org.ponder.rsf.processor.support;

import java.io.PrintWriter;
import java.io.StringWriter;

import uk.org.ponder.rsf.processor.FatalErrorHandler;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;
import uk.org.ponder.xml.XMLWriter;

/**
 * The default FatalErrorHandler for RSF. In addition to logic to print a hardwired
 * completelly fail-safe message, also contains definition of the strategy to 
 * be used for user-defined strategies as a first line. 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class DefaultFatalErrorHandler implements FatalErrorHandler {
  private Class[] propagated;

  public static final String ERROR_STRING = "Fatal internal error handling request: ";
  
  /** Set a list of exception classes which will cause no action to be
   * performed by the fatal handler.
   */
  public void setPropagatedExceptions(Class[] propagated) {
    this.propagated = propagated;
  }
  
  public static String handleFatalErrorStatic(Throwable t,
      PrintOutputStream pos) {
    // We may have such a fatal misconfiguration that we can't even rely on
    // IKAT to format this error message
    Logger.log.fatal("Completely fatal error populating view root", t);

    pos.println("<html><head><title>Internal Error</title></head></body><pre>");
    pos.println(ERROR_STRING);
    StringWriter todump = new StringWriter();
    t.printStackTrace(new PrintWriter(todump));
    XMLWriter xml = new XMLWriter(pos);
    xml.write(todump.toString());
    pos.println("</pre></body></html>");
    pos.close();
    return HANDLED;
  }

  public static String handleFatalErrorStrategy(FatalErrorHandler handler,
      Throwable t, PrintOutputStream pos) {
    String rendered = null;
    Throwable failed = null;
    try {
      rendered = handler.handleFatalError(t, pos);
    }
    catch (Throwable t2) {
      failed = t2;
    }
    if (rendered == null || failed != null) {
      return handleFatalErrorStatic(t, pos);
    }
    return rendered;
  }

  public String handleFatalError(Throwable t, PrintOutputStream pos) {
    Exception unwrapped = (Exception) UniversalRuntimeException.unwrapException(t);
    if (propagated != null) {
      for (int i = 0; i < propagated.length; ++ i) {
        if (propagated[i].isInstance(unwrapped)) return HANDLE_EXCEPTION_UPSTAIRS;
      }
    }
    return handleFatalErrorStatic(t, pos);
  }

}
