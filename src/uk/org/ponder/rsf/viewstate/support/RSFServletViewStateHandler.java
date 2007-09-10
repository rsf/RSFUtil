/*
 * Created on Jan 6, 2005
 */
package uk.org.ponder.rsf.viewstate.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ponder.rsf.viewstate.BaseURLProvider;
import uk.org.ponder.rsf.viewstate.ContextURLProvider;
import uk.org.ponder.rsf.viewstate.RawURLState;
import uk.org.ponder.rsf.viewstate.UltimateURLRenderer;
import uk.org.ponder.rsf.viewstate.ViewParamUtil;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsCodec;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;
import uk.org.ponder.stringutil.URLUtil;
import uk.org.ponder.webapputil.ConsumerInfo;

/**
 * A "simple" ViewStateHandler which in addition to accepting statically
 * configured Spring information, also is able to look in the
 * ConsumerRequestInfo threadlocal in order to discover relevant URL rendering
 * information.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RSFServletViewStateHandler implements ViewStateHandler {

  private BaseURLProvider urlprovider;
  private ConsumerInfo ciproxy;

  private Map ultimaterenderers = new HashMap();
  private ViewParamsCodec vpcodec;
  private ContextURLProvider cup;

  public void setBaseURLProvider(BaseURLProvider urlprovider) {
    this.urlprovider = urlprovider;
  }
  
  public void setContextURLProvider(ContextURLProvider cup) {
    this.cup = cup;
  }

  public void setConsumerInfo(ConsumerInfo ciproxy) {
    this.ciproxy = ciproxy;
  }

  public void setViewParamsCodec(ViewParamsCodec vpcodec) {
    this.vpcodec = vpcodec;
  }

  public String getFullURL(ViewParameters viewparams) {
    // toHTTPRequest provides leading slash, and baseurl includes trailing slash
    String requestparams = 
      ViewParamUtil.toHTTPRequest(vpcodec, viewparams).substring(1);

    String usebaseurl = urlprovider.getBaseURL();
    String extraparams = "";
    boolean quest = requestparams.indexOf('?') != -1;

    ConsumerInfo ci = ciproxy.get();
    if (ci.urlbase != null) {
      usebaseurl = ci.urlbase;
    }
    if (ci.extraparameters != null) {
      extraparams = ci.extraparameters;
      // rewrite the first character of extra params to ? if there
      // a) are any extras and b) are not any base request params.
      if (!quest && extraparams.length() > 0) {
        extraparams = "?" + extraparams.substring(1);
        quest = true;
      }
    }
    String baseurl = usebaseurl + requestparams;
    int hpos = baseurl.indexOf('#');

    String path = hpos == -1 ? baseurl + extraparams
        : baseurl.substring(0, hpos) + extraparams + baseurl.substring(hpos);
    return path;
  }

  public String getActionURL(ViewParameters viewparams) {
    String fullURL = getFullURL(viewparams);
    int qpos = fullURL.indexOf('?');
    return qpos == -1 ? fullURL
        : fullURL.substring(0, qpos);
  }

  public Map getAttrMap(ViewParameters viewparams) {
    RawURLState rus = vpcodec.renderViewParams(viewparams);
    Map togo = rus.params;
    return togo;
  }

  public String encodeResourceURL(String resourcepath) {
    if (URLUtil.isAbsolute(resourcepath) || resourcepath.charAt(0) == '/') {
      return resourcepath;
    }
    else {
      return cup.getContextBaseURL() + resourcepath;
    }
  }

  // in servlet context, rendered URLs agree with ultimate ones.
  public String getUltimateURL(ViewParameters viewparams) {
    ConsumerInfo ci = ciproxy.get();
    // ConsumerRequestInfo cri = ConsumerRequestInfo.getConsumerRequestInfo();
    if (ci.externalURL != null) {
      UltimateURLRenderer uur = (UltimateURLRenderer) ultimaterenderers
          .get(ci.consumertype);
      return uur == null ? ci.externalURL
          : uur.getUltimateURL(ci.externalURL, viewparams, this);
    }
    return getFullURL(viewparams);
  }

  public void setUltimateRenderers(List ultimates) {
    for (int i = 0; i < ultimates.size(); ++i) {
      UltimateURLRenderer ultimate = (UltimateURLRenderer) ultimates.get(i);
      ultimaterenderers.put(ultimate.getConsumerType(), ultimate);
    }
  }

}
