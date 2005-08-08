/*
 * Created on Jan 6, 2005
 */
package uk.org.ponder.rsf.util;

import uk.org.ponder.webapputil.ConsumerRequestInfo;
import uk.org.ponder.webapputil.ViewParameters;
import uk.org.ponder.webapputil.ViewStateHandler;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RSFServletViewStateHandler implements ViewStateHandler {
  private String baseurl;
  private String resourcebaseurl;
  
  /** Sets the default base URL for this view state handler, including trailing
   * slash. However, if a URL has been registered for this request via 
   * ServletUtil.setRequestConsumerURLBase, that one will be used by preference.
   */
  public void setBaseURL(String baseurl) {
    this.baseurl = baseurl;
  }
  
  public String getBaseURL() {
    return baseurl;
  }
  
  /** Sets the default base URL for static resources, including trailing
   * slash. As with baseURL, this may be overridden by ServletUtil.
   */
  public void setResourceBaseURL(String resourcebaseurl) {
    this.resourcebaseurl = resourcebaseurl;
  }
  
  public String getResourceBaseURL() {
    return resourcebaseurl;
  }
  
  public String getFullURL(ViewParameters viewparams) {
    String requestparams = viewparams.toHTTPRequest();

    String usebaseurl = baseurl;
    String extraparams = "";
    ConsumerRequestInfo cri = ConsumerRequestInfo.getConsumerRequestInfo();
    if (cri != null) {
      if (cri.ci.urlbase != null) {
        usebaseurl = cri.ci.urlbase;
      }
      if (cri.ci.extraparameters != null) {
        extraparams = cri.ci.extraparameters;
        // rewrite the first character of extra params to ? if there 
        // a) are any extras and b) are not any base request params.
        if (requestparams.length() == 0 && extraparams.length() > 0) {
          extraparams = "?" + extraparams.substring(1);
        }
      }
    }
    // We don't make any use of sessions - remove all dependence on request
    // for URL encoding now.
    //ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

    // QQQQQ This is totally nonportable!! What will we do about this!!
    // Now a good deal less unportable. encodeRedirectURL seems to differ only
    // to help session tracking:
    // http://www.theserverside.com/discussions/thread.tss?thread_id=6039
    //HttpServletResponse response = (HttpServletResponse) ec.getResponse();
    String path = //response.encodeRedirectURL(
        usebaseurl + viewparams.viewID + requestparams + extraparams
        //)
        ;
    return path;
  }
  

  public String getResourceURL(String resourcepath) {
    String useresurl = resourcebaseurl;
    ConsumerRequestInfo cri = ConsumerRequestInfo.getConsumerRequestInfo();
    if (cri != null && cri.ci.resourceurlbase != null) {
      useresurl = cri.ci.resourceurlbase; 
    }
    return useresurl + resourcepath;
  }


  // in servlet context, rendered URLs agree with ultimate ones.
  public String getUltimateURL(ViewParameters viewparams) {
    ConsumerRequestInfo cri = ConsumerRequestInfo.getConsumerRequestInfo();
     if (cri != null) {
       if (cri.ci.externalURL != null) {
         return cri.ci.externalURL;
       }
     }
    return getFullURL(viewparams);
  }

//  public String parseExtraInfo(String requesturl) {
//    String pathinfo = JSFUtil.getHSRequest().getPathInfo();
//    return pathinfo.substring(1); // remove leading / which is specced to be there
//    //return JSFUtil.getRequestURL().substring(baseurl.length());
//  }


}
