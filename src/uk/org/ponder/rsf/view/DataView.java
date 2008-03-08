/*
 * Created on 7 Mar 2008
 */
package uk.org.ponder.rsf.view;

import uk.org.ponder.rsf.content.ContentTypeReporter;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;


/** An interface for a view which represents "pure data" - for example, a JSON
 * or XML-encoded object tree, or else a pure byte stream (image, PDF, etc.)
 * </p>
 * A DataView may in addition implement {@link ViewParamsReporter} to register
 * a custom view parameters type. Placing this implementation at request scope
 * will allow you to inject an HttpServletResponse allowing you to inject custom
 * headers.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * @since 0.7.3
 */ 

public interface DataView extends ContentTypeReporter, ViewIDReporter {
  /** 
   * Return the object specifying the view to be rendered.
   * In the case of 
   */
  public Object getData(ViewParameters viewparams);
}
