/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.flow;

import uk.org.ponder.rsf.viewstate.ViewParamUtil;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParametersParser;

/** A simple exception strategy which diverts to a specified view in the 
 * event of any exception. The view is specified in the form of a "reduced URL"
 * compactly specifying a ViewParameters object. This contains the "stub" portion
 * of the URL starting at the part specifying the viewID, and including any 
 * navigational parameters.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class SimpleViewExceptionStrategy implements ViewExceptionStrategy {
  private ViewParametersParser parser;
  private String view;
  public void setDefaultView(String view) {
    this.view = view;
  }
  public void setViewParametersParser(ViewParametersParser parser) {
    this.parser = parser;
  }
  public ViewParameters handleException(Exception e, ViewParameters incoming) {
    ViewParameters togo = ViewParamUtil.parse(parser, view);
    return togo;
  }
  
}
