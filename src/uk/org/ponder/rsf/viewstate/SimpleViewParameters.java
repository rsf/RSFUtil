/*
 * Created on Dec 14, 2004
 */
package uk.org.ponder.rsf.viewstate;

import uk.org.ponder.stringutil.StringList;

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
  static StringList attrnames = 
    StringList.fromString("flowtoken, endflow, errortoken, errorredirect");
  // NB - NONE of these parameters should appear in ViewParameters since they
  // are not "core state-defining". They should not be visible to client code
  // directly, but part of the overall request container state. Need to
  // KEEP THINKING about this "Part of container/self-contained" debate.
  public StringList getAttributeFields() {
   return attrnames;
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


}