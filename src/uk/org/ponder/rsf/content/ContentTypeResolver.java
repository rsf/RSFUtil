/*
 * Created on May 6, 2006
 */
package uk.org.ponder.rsf.content;

import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Resolves the correct "content type string" identifying the content type
 * to be in force for the current render cycle. Some sample values can be
 * seen in {@link ContentTypeInfoRegistry}.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface ContentTypeResolver {
  public String resolveContentType(ViewParameters viewparams);
}
