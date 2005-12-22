/*
 * Created on Nov 14, 2005
 */
package uk.org.ponder.rsf.viewstate;


import java.util.Map;

import uk.org.ponder.util.Logger;

/** A simple parser of view parameters, which will always parse into the
 * same type of ViewParameters object. The type of the returned parameters
 * is determined by the "exemplar" object which is set as a property. Here it
 * is the parser which is Simple, not the parameters - will parse any statically
 * determined ViewParameters.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class SimpleViewParametersParser implements ViewParametersParser {
  private ViewParameters vpexemplar;
  
  public void setViewParametersExemplar(ViewParameters vpexemplar) {
    this.vpexemplar = vpexemplar;
  }

  public ViewParameters parse(String pathinfo, Map requestmap) {
    // JSF memorial comment:
    // restoreView is the very first of the ViewHandler methods to be called for
    // each request, and it is guaranteed to be called. We take this opportunity
    // to stash away a parsed parameter object corresponding to our original
    // request.
    ViewParameters origrequest = vpexemplar.copyBase();
    origrequest.fromRequest(pathinfo, requestmap);

    // Map requestmap = req.
    // requestmap.put(ViewParameters.CURRENT_REQUEST, origrequest);
    Logger.log.info("Restoring view " + origrequest.viewID
        + " from request parameters " + URLUtil.toHTTPRequest(origrequest));
    return origrequest;

  }
}
