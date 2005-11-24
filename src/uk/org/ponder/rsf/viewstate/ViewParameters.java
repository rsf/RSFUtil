/*
 * Created on Oct 25, 2004
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Map;

import uk.org.ponder.reflect.FieldHash;

/**
 * The base class abstracting common functionality for specifying a view
 * state of a web application, independent of any particular application 
 * or url mapping technology.
 * In order to get generate a complete external URL, you must use one of
 * the methods of {@see ViewStateHandler}.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
//for example:
//http://localhost:8080/FlowTalkServlet/faces/threaded-message?targetID=3mdugroBVjhPPTubjQkjAQjG
//|-----------------------------------------||---------------|
//       base url                              view id
// in fact, since we only ever have ONE view id, in fact we could simply
// omit baseurl!!

public abstract class ViewParameters implements Cloneable {
  /** Identifies this request as part of a "render" cycle - for a simple HTTP
   * servlet, corresponds to a GET, a JSR168 "render" request or a WSRP 
   * getMarkup request.
   */ 
  public static final String RENDER_REQUEST = "render";
  
  /** Identifies this request as part of an "action" cycle - for a simple
   * HTTP servlet, corresponds to a POST, a JSR168 "processAction" request 
   * or a WRSP performBlockingInteration request.
   */
  public static final String ACTION_REQUEST = "action";
  /** A globally unique key identifying view-specific state that is held on the
   * server, restorable in the form of a request-scope bean container. Need not
   * be set. 
   */ 
  public String flowtoken;
  /** This field is set in the case of an error arising from user-submitted 
   * data, in which case the data is being returned for correction. Not
   * generally set.
   */
  public String errortoken;
  /** The primary key identifying this view - this is generally used as a key
   * not only to view templates, component producers, but also into flow states.
   * This field is always set.
   */
  public String viewID;
  /** This field is set indicating that this view is a redirect from a level-1
   * rendering error. Any further errors will be treated as fatal, to avoid a 
   * redirect loop. Usually not set.
   */
  public String errorredirect;
  /** This field is set indicating that this view is a "terminal flow view",
   * representing the summary to the user of a multi-request state (flow). Some, none 
   * or all of the request state accumulated during the flow may be available
   * to render this view, most likely in a more rapidly-expiring cache (default
   * is to use the same TokenStateHolder as the error state). It is only valid
   * to set this field if flowtoken is also set.
   */
  public String endflow;
  public abstract FieldHash getFieldHash();
  public abstract void clearParams();
  public abstract void parsePathInfo(String pathinfo);
  public abstract String toPathInfo();

  /** Fill in the fields of the supplied view parameter object with data
   * from the supplied URL (which may be null), the supplied parameter map
   * and any other statically accessible sources of information.
   */
  public void fromRequest(String pathinfo, Map parameters) {
    parsePathInfo(pathinfo);
    getFieldHash().fromMap(parameters, this);
  }
  
  
// Note that copying does not copy the error token! All command links
// take the original request, and all non-command links should not share
// error state.
  public ViewParameters copyBase() {
    try {
      ViewParameters togo = (ViewParameters) clone(); 
      togo.flowtoken = null;
      togo.errortoken = null;
      return togo;
    }
    catch (Throwable t) {
      return null;
    } // CANNOT THROW! IDIOTIC SYSTEM!!   
  }

}