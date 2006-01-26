/*
 * Created on Nov 14, 2005
 */
package uk.org.ponder.rsf.viewstate;


import java.net.MalformedURLException;
import java.util.Map;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/** A simple parser of view parameters, which will parse into clones of
 * supplied "exmplar" objects. Here it is the parser which is Compound, 
 * not the parameters. The lookup will be performed on the first segment
 * of the pathinfo (up to first slash), which will be assumed to represent
 * the viewID.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class BasicViewParametersParser implements ViewParametersParser {
  private Map exemplarmap;
  private BeanModelAlterer bma;
  
  public void setViewParametersMap(Map exemplarmap) {
    this.exemplarmap = exemplarmap;
  }
  
  public void setBeanModelAlterer(BeanModelAlterer bma) {
    this.bma = bma;
  }

  public ViewParameters parse(String pathinfo, Map requestmap) {
    int firstslashpos = pathinfo.indexOf('/', 1);
    String viewID = firstslashpos == -1? pathinfo.substring(1) :
      pathinfo.substring(1, firstslashpos);
      
    ViewParameters vpexemplar = (ViewParameters) exemplarmap.get(viewID);
    if (vpexemplar == null) {
      throw UniversalRuntimeException.accumulate(new MalformedURLException(), 
          "View " + viewID + " unknown in BasicViewParametersParser");
    }
    ViewParameters origrequest = vpexemplar.copyBase();
    URLUtil.parseViewParamAttributes(bma, origrequest, requestmap);
    origrequest.parsePathInfo(pathinfo);

    // Map requestmap = req.
    // requestmap.put(ViewParameters.CURRENT_REQUEST, origrequest);
    Logger.log.info("Restoring view " + origrequest.viewID
        + " from request parameters " + URLUtil.toHTTPRequest(bma, origrequest));
    return origrequest;

  }
}
