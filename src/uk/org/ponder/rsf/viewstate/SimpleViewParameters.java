/*
 * Created on Dec 14, 2004
 */
package uk.org.ponder.rsf.viewstate;

/**
 * A simple set of view parameters that defines no extra fields, and maps the
 * viewID parameter onto the end of the servlet path.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class SimpleViewParameters extends ViewParameters {
  public SimpleViewParameters() {}
  
  public SimpleViewParameters(String viewID) {
    this.viewID = viewID;
  }
 
  public void clearParams() {
  }

  public void parsePathInfo(String pathinfo) {
    // remove leading / which is specced to be there
    viewID = pathinfo.substring(1);
  }

  public String toPathInfo() {
    return "/" + viewID;
  }

  public String getParseSpec() {
    return ViewParameters.BASE_PARSE_SPEC;
  }


}