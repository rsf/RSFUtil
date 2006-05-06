/*
 * Created on Nov 14, 2005
 */
package uk.org.ponder.rsf.renderer;

import uk.org.ponder.rsf.request.SubmittedValueEntry;
 
import java.util.Map;

/** The portion of the "RenderSystem" interface that deals with incoming actions
 * resulting from previously rendered submitting controls.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface RenderSystemDecoder {
  /** "Normalize" the supplied (writeable) request map by scouring it for
   * System-specific key/values that secretly encode a number of others.
   * TODO: Might also normalize by making multi-valued params into single-valued,
   * but unclear whether there is enough knowledge in the system to distinguish
   * values which OUGHT to be multivalued (probably only list submits).
   */
  public void normalizeRequestMap(Map requestparams);
  
  /** Fix up the "new value" in the supplied SVE, for example to take account of the
   * target idiom's "missing value" semantics. E.g. for HTTP, checkboxes in the
   * false state submit nothing, as do multiple list selections with nothing 
   * selected.
   */
  public void fixupUIType(SubmittedValueEntry sve);
}
