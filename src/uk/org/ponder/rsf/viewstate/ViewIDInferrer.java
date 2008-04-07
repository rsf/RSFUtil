/*
 * Created on 11 Jul 2006
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Map;

/** Infer from the supplied raw URL state the RSF viewID for the current
 * request. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface ViewIDInferrer {
  /** Infer the corresponding viewID from the supplied URL information.
   * @param pathinfo In a Servlet environment, the value returned by 
   * <code>HttpServletRequest.getPathInfo()</code>. In other environments 
   * (e.g. portlets), this value may be to some extent fictitious, 
   * but may be relied upon to maintain the same semantics.
   * 
   * Corresponds to the value of the request scope bean <code>requestPathInfo</code>.
   * @param requestmap The attribute map for the request. Unlikely to 
   * contain information defining the viewID, but included for completeness.
   * 
   * Corresponds to the value of the request scope bean <code>requestMap</code>
   * @return The inferred view ID.
   */
  public String inferViewID(String[] pathinfo, Map requestmap);
}
