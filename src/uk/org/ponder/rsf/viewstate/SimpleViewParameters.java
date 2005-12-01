/*
 * Created on Dec 14, 2004
 */
package uk.org.ponder.rsf.viewstate;

import uk.org.ponder.reflect.FieldHash;

/**
 * A simple set of view parameters that defines no extra fields, and maps the
 * viewID parameter onto the end of the servlet path.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class SimpleViewParameters extends ViewParameters {
  private static FieldHash fieldhash = new FieldHash(SimpleViewParameters.class);
  static {
    fieldhash.addField("flowtoken");
    fieldhash.addField("endflow");
    fieldhash.addField("errortoken");
    fieldhash.addField("errorredirect");
  }
  public FieldHash getFieldHash() {
    return fieldhash;
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