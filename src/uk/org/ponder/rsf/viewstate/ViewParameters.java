/*
 * Created on Oct 25, 2004
 */
package uk.org.ponder.rsf.viewstate;

import uk.org.ponder.reflect.DeepBeanCloner;

/**
 * An RSF ViewParameters is a rule for extracting the relevant fields from a URL
 * (or similar spec) that specify the content of a view and representing them in
 * a typesafe way as an object. Users will more frequently extend the more
 * convenient class {@link SimpleViewParameters}. The actual work of parsing is
 * done by the more stateful bean ViewParametersParser.
 * <p>
 * The base class abstracting common functionality for specifying a view state
 * of a web application, independent of any particular application or url
 * mapping technology. In order to get generate a complete external URL, you
 * must use one of the methods of
 * {@link uk.org.ponder.rsf.viewstate.ViewStateHandler}.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
// for example:
// http://localhost:8080/FlowTalkServlet/faces/threaded-message?targetID=3mdugroBVjhPPTubjQkjAQjG
// |-----------------------------------------||---------------|
// base url view id
// in fact, since we only ever have ONE view id, in fact we could simply
// omit baseurl!!
public abstract class ViewParameters implements AnyViewParameters {
  /** The special prefix which is used in {@link #getParseSpec()} to represent
   * a part of URL state which will be placed in the URL trunk rather than 
   * an attribute. For example, the {{@link #viewID} itself is commonly mapped
   * to <code>@0</code> indicating it is the first path segment in the "pathInfo"
   * section of the URL.
   */ 
  public static final String TRUNK_PARSE_PREFIX = "@";
  /**
   * Returns a "parse specification" suitable for mapping (currently just the
   * attribute fields of) this ViewParameters into a URL. 
   * This is a comma-separated list of field specifications. Each field specification
   * takes the form of the name of an URL attribute, optionally followed by the
   * colon character : and an EL path into this ViewParameters object onto which
   * the attribute should be mapped. Instead of a URL attribute, URL pathinfo
   * segments can be specified by providing strings of the form <code>@0</code>,
   * <code>@1</code>, etc.
   * </p>If users derive from 
   * {@link SimpleViewParameters} and do not override this method, a default
   * parse specification will be inferred.
   * 
   * @return A string representing a URL parse specification
   */

  public abstract String getParseSpec();
  
  /** Return a field which will be used to form the "anchor" text in the URL
   * state - in HTTP, this typically follows the URL body with a <code>#</code> 
   * character. */
  public String getAnchorField() {
    return null;
  }
  // Note that copying does not copy the error token! All command links
  // take the original request, and all non-command links should not share
  // error state.
  /*****************************************************************************
   * See {@link #copyBase(DeepBeanCloner)} below. Uses a ThreadLocal call to
   * acquire the standard DeepBeanCloner bound to the current thread - necessary
   * since ViewParameters objects are cloned in all sorts of lightweight
   * contexts. Use the method below by preference if at all possible.
   ****************************************************************************/
  public ViewParameters copyBase() {
    return copyBase(ViewParamUtil.getCloner());
  }

  /**
   * Make a "deep clone" of this ViewParameters object, representing the "same"
   * view state but sharing no Object state with this object. To enable this to
   * work automatically, all extra members from derived classes must be POJO
   * beans or peas. See {@link DeepBeanCloner} for explanation of the algorithm
   * used. Use this method if the performance or architecture impact of the
   * no-args method bothers you.
   */
  public ViewParameters copyBase(DeepBeanCloner cloner) {
    return (ViewParameters) cloner.cloneBean(this, cloneexceptions);
  }

  /**
   * The primary key identifying this view - this is generally used as a key not
   * only to view templates, component producers, but also into flow states.
   * This field is always set.
   */
  public String viewID;
  
  /** The following public fields define RSF housekeeping information, and should
   * not generally be read or written by user code.
   * <p/> 
   * A globally unique key identifying view-specific state that is held on the
   * server, restorable in the form of a request-scope bean container. Need not
   * be set.
   */
  public String flowtoken;
  /**
   * This field is set in the case of an error arising from user-submitted data,
   * in which case the data is being returned for correction. Not generally set.
   */
  public String errortoken;

  /**
   * This field is set indicating that this view is a redirect from a level-1
   * rendering error. Any further errors will be treated as fatal, to avoid a
   * redirect loop. Usually not set.
   */
  public String errorredirect;
  /**
   * This field is set indicating that this view is a "terminal flow view",
   * representing the summary to the user of a multi-request state (flow). Some,
   * none or all of the request state accumulated during the flow may be
   * available to render this view, most likely in a more rapidly-expiring cache
   * (default is to use the same TokenStateHolder as the error state). It is
   * only valid to set this field if flowtoken is also set.
   */
  public String endflow;

  public static final String BASE_PARSE_SPEC = "flowtoken, endflow, errortoken, errorredirect, @0:viewID";

  /**
   * "Ephemeral" fields of ViewParameters state that will not propagate by
   * default.
   */
  public static final String[] cloneexceptions = new String[] { "flowtoken",
      "errortoken", "endflow", "parseSpec", "anchorField" };

  /** Pea proxying method */
  public ViewParameters get() {
    return this;
  }
}