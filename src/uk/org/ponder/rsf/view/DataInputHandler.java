/*
 * Created on 1 Jul 2008
 */
package uk.org.ponder.rsf.view;

import uk.org.ponder.conversion.TypeDecodable;
import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.content.ContentTypeReporter;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * <p>An interface for a bean which is registered as a direct handler for input over
 * an HTTP protocol. By making a return of </p> 
 * 
 * <p>A DataInputHandler may in addition implement {@link ViewParamsReporter} to register
 * a custom view parameters type.</p>
 * 
 * <p>When registering a content type of {@link ContentTypeInfoRegistry#JSON}, the 
 * DataInputHandler may also request automatic conversion of incoming data into
 * a specified Class or prototype Object by implementing the {@link TypeDecodable} 
 * interface</p>
 * 
 * <p> Placing this implementation at request scope
 * will allow you to inject an HttpServletRequest allowing you to get access to
 * raw request primitives</p>
 * @author Antranig Basman (amb26@ponder.org.uk)
 * @since 0.7.3
 */

public interface DataInputHandler extends ContentTypeReporter, ViewIDReporter {
  /** Returns a comma-separated list of HTTP methods which will be routed to 
   * this handler.
   * @return A comma-separated list of HTTP methods.
   */
  public String getHandledMethods();
  
  /** Handle the input for this request.
   * 
   * @param The view parameters for the request location.
   * @param method The HTTP method used for this request.
   * @param data The data supplied as the body of the request. If a specific
   * return has been made from #getContentType(), such as 
   * {@link ContentTypeInfoRegistry#JSON} or {@link ContentTypeInfoRegistry#AJAX},
   * the data will be supplied decoded into that format. If #getContentType returns
   * {@link ContentTypeInfoRegistry#CUSTOM}, the raw InputStream of bytes from the
   * request will be delivered.
   */
  public void handleInput(ViewParameters viewparams, String method, Object data);
}
