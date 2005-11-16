/*
 * Created on Nov 14, 2005
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Map;

import uk.org.ponder.util.Logger;

public class ViewParametersParser {
  private Map requestmap;
  private String pathinfo;

  private ViewParameters vpexemplar;

  public void setViewParametersExemplar(ViewParameters vpexemplar) {
    this.vpexemplar = vpexemplar;
  }

  public void setRequestMap(Map requestmap) {
    this.requestmap = requestmap;
  }
  
  public void setPathInfo(String pathinfo) {
    this.pathinfo = pathinfo;
  }
  
  public ViewParameters getViewParameters() {
    // JSF memorial comment:
    // restoreView is the very first of the ViewHandler methods to be called for
    // each request, and it is guaranteed to be called. We take this opportunity
    // to stash away a parsed parameter object corresponding to our original
    // request.
    ViewParameters origrequest = vpexemplar.copyBase();
    origrequest.fromRequest(requestmap, pathinfo);

    // Map requestmap = req.
    // requestmap.put(ViewParameters.CURRENT_REQUEST, origrequest);
    Logger.log.info("Restoring view " + origrequest.viewID
        + " from request parameters " + origrequest.toHTTPRequest());
    return origrequest;

  }
}
