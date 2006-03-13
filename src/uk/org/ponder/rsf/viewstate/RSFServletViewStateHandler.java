/*
 * Created on Jan 6, 2005
 */
package uk.org.ponder.rsf.viewstate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.webapputil.ConsumerInfo;

/**
 * A "simple" ViewStateHandler which in addition to accepting statically
 * configured Spring information, also is able to look in the
 * ConsumerRequestInfo threadlocal in order to discover relevant URL rendering
 * information. A request-scope bean.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RSFServletViewStateHandler implements ViewStateHandler {

  private BaseURLProvider urlprovider;
  private ConsumerInfo ci;
  private BeanModelAlterer bma;

  private Map ultimaterenderers = new HashMap();

  public void setBaseURLProvider(BaseURLProvider urlprovider) {
    this.urlprovider = urlprovider;
  }

  public void setConsumerInfo(ConsumerInfo ci) {
    this.ci = ci;
  }

  public void setBeanModelAlterer(BeanModelAlterer bma) {
    this.bma = bma;
  }

  public String getFullURL(ViewParameters viewparams) {
    // toHTTPRequest provides leading slash, and baseurl includes trailing slash
    String requestparams = ViewParamUtil.toHTTPRequest(bma, viewparams)
        .substring(1);

    String usebaseurl = urlprovider.getBaseURL();
    String extraparams = "";
    // ConsumerRequestInfo cri = ConsumerRequestInfo.getConsumerRequestInfo();
    boolean quest = requestparams.indexOf('?') != -1;
    // String presparams = "";
    // if (outgoingparams != null) {
    // RenderUtil.makeURLAttributes(outgoingparams);
    // if (!quest && presparams.length() > 0) {
    // presparams = "?" + presparams.substring(1);
    // quest = true;
    // }
    // }
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

    // We don't make any use of sessions - remove all dependence on request
    // for URL encoding now.
    // ExternalContext ec =
    // FacesContext.getCurrentInstance().getExternalContext();

    // QQQQQ This is totally nonportable!! What will we do about this!!
    // Now a good deal less unportable. encodeRedirectURL seems to differ only
    // to help session tracking:
    // http://www.theserverside.com/discussions/thread.tss?thread_id=6039
    // HttpServletResponse response = (HttpServletResponse) ec.getResponse();
    String path = // response.encodeRedirectURL(
    usebaseurl + requestparams + extraparams
    // )
    ;
    return path;
  }

  public String getResourceURL(String resourcepath) {
    String useresurl = urlprovider.getResourceBaseURL();
    // ConsumerRequestInfo cri = ConsumerRequestInfo.getConsumerRequestInfo();
    if (ci.resourceurlbase != null) {
      useresurl = ci.resourceurlbase;
    }
    return useresurl + resourcepath;
  }

  // in servlet context, rendered URLs agree with ultimate ones.
  public String getUltimateURL(ViewParameters viewparams) {
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
