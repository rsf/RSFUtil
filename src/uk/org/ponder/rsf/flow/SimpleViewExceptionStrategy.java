/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.flow;

import uk.org.ponder.rsf.viewstate.URLUtil;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParametersParser;

/** A simple exception strategy which diverts to a specified view in the 
 * even of any exception
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
    ViewParameters togo = URLUtil.parse(parser, view);
    return togo;
  }
  
}
