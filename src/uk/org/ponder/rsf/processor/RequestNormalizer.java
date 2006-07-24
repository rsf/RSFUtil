/*
 * Created on Nov 14, 2005
 */
package uk.org.ponder.rsf.processor;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.rsf.request.RenderSystemDecoder;
import uk.org.ponder.rsf.request.SubmittedValueEntry;

/** This class now disused in favour of PostDecoder **/

public class RequestNormalizer {
  private RenderSystemDecoder rendersystemstatic;
  private Map requestmap;
  private Map normalized;
  
  public void setRequestMap(Map requestmap) {
    this.requestmap = requestmap;
  }

  public void setRenderSystemStatic(RenderSystemDecoder rendersystemstatic) {
    this.rendersystemstatic = rendersystemstatic;
  }
  // cheaper than an init-method!
  private void checkInit() {
    if (normalized == null) {
      normalized = new HashMap();
      normalized.putAll(requestmap);
      rendersystemstatic.normalizeRequestMap(normalized);
    }
  }
  
  public Map getNormalizedRequestMap() {
    checkInit();
    return normalized;
  }
  
  public String getSubmittingControl() {
    checkInit();
    return (String) normalized.get(SubmittedValueEntry.SUBMITTING_CONTROL);
  }
}
