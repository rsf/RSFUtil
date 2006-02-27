package uk.org.ponder.rsf.request;

import java.util.Map;

import uk.org.ponder.rsf.viewstate.ViewParameters;


public class DynamicStreamEarlyRequestParser implements EarlyRequestParser {

	private String pathInfo;
	
	private Map requestMap = null;
	
	public void setRequestMap(Map requestMap) {
		this.requestMap = requestMap;
	}
	
	/*
	 * It is your responsibility to make sure this isn't null
	 * by the time you want RSF to use this...
	 */
    public Map getRequestMap() {
        return requestMap;
    }

    /*
     * FAKE pathinfo:
     * This INCLUDES an initial slash but no final slash,
     * e.g. "/thingy/foobar"
     */
    public void setPathInfo(String pathInfo) {
    	this.pathInfo = pathInfo;
    }
    
    /*
     * FAKE pathinfo as set-up by you earlier.
     * This INCLUDES an initial slash but no final slash,
     * e.g. "/thingy/foobar"
     */
    public String getPathInfo() {
      return pathInfo;
    }

    /*
     * A factory method for a String encoding the nature of the current request
     * cycle, either ViewParameters.RENDER_REQUEST
     * or ViewParameters.ACTION_REQUEST.
     * 
     * We always handle Render Requests at the moment (i.e. pass to IKAT);
     * this implies that if an HTTP Request is used to generate this, it
     * should be a GET request, NOT a POST.
     */
    public String getRequestType() {
      return ViewParameters.RENDER_REQUEST;
    }

  
}

