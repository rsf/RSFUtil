/*
 * Created on Nov 19, 2006
 */
package uk.org.ponder.rsf.request;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.rsac.RSACLazarusList;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.support.ViewParamsMapper;

/** Performs an *internal* redirect to the RSF system, by registering a 
 * "Lazarus listener" which will fire a further request at end of the current
 * RSAC cycle.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class LazarusRedirector {
  private ViewParamsMapper viewParamsMapper;
  private RSACLazarusList lazarusListReceiver;
  private String environmentType;

  public void setViewParamsMapper(ViewParamsMapper viewParamsMapper) {
    this.viewParamsMapper = viewParamsMapper;
  }

  public void setLazarusListReceiver(RSACLazarusList lazarusListReceiver) {
    this.lazarusListReceiver = lazarusListReceiver;
  }
  
  public void setEnvironmentType(String environmentType) {
    this.environmentType = environmentType;
  }

  public void lazarusRedirect(final ViewParameters target) {
    Map params = viewParamsMapper.renderViewParamAttributes(target);
    String pathinfo = viewParamsMapper.toPathInfo(target);
    StaticEarlyRequestParser serp = new StaticEarlyRequestParser(null,
        pathinfo, params, EarlyRequestParser.RENDER_REQUEST, environmentType);
    Map newmap = new HashMap();
    newmap.put("earlyRequestParser", serp);
    lazarusListReceiver.queueRunnable(lazarusListReceiver.getLazarusRunnable(
        newmap, "rootHandlerBean"));
  }

}
