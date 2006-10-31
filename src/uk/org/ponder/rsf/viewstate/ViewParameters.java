/*
 * Created on Oct 25, 2004
 */
package uk.org.ponder.rsf.viewstate;

import uk.org.ponder.reflect.DeepBeanCloner;

/**
 * An RSF ViewParameters is a rule for extracting the relevant fields from
 * a URL (or similar spec) that specify the content of a view and representing
 * them in a typesafe way as an object. The actual work of parsing is
 * done by the more stateful bean ViewParametersParser.
 * <p> 
 * The base class abstracting common functionality for specifying a view
 * state of a web application, independent of any particular application 
 * or url mapping technology.
 * In order to get generate a complete external URL, you must use one of
 * the methods of {@link uk.org.ponder.rsf.viewstate.ViewStateHandler}.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
//for example:
//http://localhost:8080/FlowTalkServlet/faces/threaded-message?targetID=3mdugroBVjhPPTubjQkjAQjG
//|-----------------------------------------||---------------|
//       base url                              view id
// in fact, since we only ever have ONE view id, in fact we could simply
// omit baseurl!!

public abstract class ViewParameters implements AnyViewParameters {
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

  /** Returns a "parse specification" suitable for mapping (currently just the
   * attribute fields of) this ViewParameters into a URL. This is a comma-separated
   * list of field specifications. Each field specification takes the form of
   * the name of an attribute
   * @return A string representing a URL parse specification
   */
  
  public abstract String getParseSpec();
  
  /** Return a field which will be used to form the "anchor" text */
  public String getAnchorField() {
    return null;
  }
   /** Parse the "extra path info" field as defined as the "URL stub" following
   * the path that the handling servlet is mapped to, into correponding fields
   * in this object. This will at least include parsing the portion following
   * the initial forward slash (compulsory) up to the next one into the
   * viewID field.
   * @param pathinfo
   */
  public abstract void parsePathInfo(String pathinfo);
  /** Invert the operation of parseParseInfo, and render any state in this 
   * ViewParameters back into the corresponding URL stub. 
   */
  public abstract String toPathInfo();
 
  public abstract void clearParams();
  public static final String BASE_PARSE_SPEC = "flowtoken, endflow, errortoken, errorredirect"; 
  
  /** "Ephemeral" fields of ViewParameters state that will not propagate by
   * default.
   */
  public static final String[] cloneexceptions = new String[] {
    "flowtoken", "errortoken", "endflow", "parseSpec", "anchorField"
  };
// Note that copying does not copy the error token! All command links
// take the original request, and all non-command links should not share
// error state.
  /** Make a "deep clone" of this ViewParameters object, representing the
   * "same" view state but sharing no Object state with this object. To enable
   * this to work automatically, all extra members from derived classes must
   * be POJO beans or peas. See {@link DeepBeanCloner} for explanation of the 
   * algorithm used. Use this method if the performance or architecture impact
   * of the no-args method bothers you.
   */
  public ViewParameters copyBase(DeepBeanCloner cloner) {
    return (ViewParameters) cloner.cloneBean(this, cloneexceptions);
  }
  
  /** See <code>copyBase</code> above. Uses a ThreadLocal call to acquire
   * the standard DeepBeanCloner bound to the current thread - necessary since
   * ViewParameters objects are cloned in all sorts of lightweight contexts.
   * Use the method above by preference if at all possible.
   * **/
  public ViewParameters copyBase() {
    return copyBase(ViewParamUtil.getCloner());
  }

  /** Pea proxying method */
  public ViewParameters get() {
    return this;
  }
}