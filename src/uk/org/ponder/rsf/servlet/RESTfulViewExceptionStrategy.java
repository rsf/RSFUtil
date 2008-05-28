/*
 * Created on 26 Mar 2008
 */
package uk.org.ponder.rsf.servlet;

import java.io.PrintStream;

import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.errorutil.PermissionException;
import uk.org.ponder.rsf.flow.errors.ViewExceptionStrategy;
import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.NoViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParametersPredicate;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

public class RESTfulViewExceptionStrategy implements ViewExceptionStrategy {

  private ViewParametersPredicate viewParametersPredicate;

  private HttpServletResponse httpServletResponse;

  public void setViewParametersPredicate(ViewParametersPredicate viewParametersPredicate) {
    this.viewParametersPredicate = viewParametersPredicate;
  }

  public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
    this.httpServletResponse = httpServletResponse;
  }

  public AnyViewParameters handleException(Exception e, ViewParameters viewparams) {
    if (viewParametersPredicate.accept(viewparams)) {
      try {
        if (e instanceof SecurityException || e instanceof PermissionException) {
          Logger.log.info("Sending HTTP status 401 for exception ", e);
          httpServletResponse.sendError(401, e.getLocalizedMessage());
        }
        else {
          Logger.log.info("Sending HTTP status 500 for exception ", e);
          httpServletResponse.sendError(500, e.getLocalizedMessage());
        }
        try { // Jetty will refuse to allow access to Writer after sendError 
          e.printStackTrace(httpServletResponse.getWriter());
        }
        catch (IllegalStateException e3) {
          e.printStackTrace(new PrintStream(httpServletResponse.getOutputStream()));
        }
      }
      catch (Exception e2) {
        throw UniversalRuntimeException
            .accumulate(e2, "Error setting HTTP response code");
      }
      NoViewParameters togo = new NoViewParameters();
      togo.errorredirect = "1";
      return togo;
    }
    return null;
  }

}
