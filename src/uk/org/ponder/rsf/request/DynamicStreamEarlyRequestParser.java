package uk.org.ponder.rsf.request;

import java.util.HashMap;
import java.util.Map;


/** A default concrete implementation of EarlyRequestParser that is a simple
 * repository for set values.
 * @author Raymond Chan (raymond@caret.cam.ac.uk)
 *
 */
public class DynamicStreamEarlyRequestParser implements EarlyRequestParser {

	private String pathInfo;
	
	private Map requestMap = null;
	
	public void setRequestMap(Map requestMap) {
		this.requestMap = requestMap;
	}
	
	/*
	 * A parameter map from which request parameters can be read. RSF expects
     * this to be the standard map of String to String[].
	 */
    public Map getRequestMap() {
        return requestMap;
    }

    /*
     * This INCLUDES an initial slash but no final slash,
     * e.g. "/thingy/foobar"
     */
    public void setPathInfo(String pathInfo) {
    	this.pathInfo = pathInfo;
    }
    
    /*
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
      return EarlyRequestParser.RENDER_REQUEST;
    }

    public Map getMultipartMap() {
      return new HashMap();
    }

    public String getEnvironmentType() {
      return null;
    }
  
}

