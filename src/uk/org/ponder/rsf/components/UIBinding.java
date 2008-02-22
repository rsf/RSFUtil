/*
 * Created on 14 Feb 2008
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.mapping.DataAlterationRequest;

/** A form of parameter that represents a model-directed binding.
 *  E.g. a {@link UIELBinding} or {@link UIDeletionBinding}
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIBinding extends UIParameter {
  /** If set to <code>true</code>, represents a "virtual" binding/parameter, that is,
   * one that is rendered inactive by default, to be "discovered" by some
   * client-side mechanism. Virtual bindings represent an "alternate execution path"
   * through the request container.
   */
  public boolean virtual;
  
  /** The encoding to be applied for r-values of non-leaf types **/
  public String encoding = DataAlterationRequest.JSON_ENCODING;
}
