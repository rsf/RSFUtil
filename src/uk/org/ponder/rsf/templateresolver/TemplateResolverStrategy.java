/*
 * Created on Apr 26, 2006
 */
package uk.org.ponder.rsf.templateresolver;

import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.stringutil.StringList;

/**
 * Resolves an incoming set of ViewParameters onto a list of template path names
 * (minus extension) to be searched for an appropriate template file.
 * <p>
 * Use of this interface implies the use of templates from webapp-local
 * storage, as in general served by the DefaultServlet. For more finegrained
 * control over template location and mapping, use the
 * {@link BaseAwareTemplateResolverStrategy}.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public interface TemplateResolverStrategy {
  /**
   * @param viewparams The view parameters for the view being rendered.
   * @return A (modifiable) list of resource names (as supplied to some kind of
   *         InputStreamSource) MINUS extension, that will be searched, IN
   *         ORDER, for a view template to be used for the current view. These
   *         are *relative* paths from the context root of the webapp, omitting
   *         the leading slash.
   */
  public StringList resolveTemplatePath(ViewParameters viewparams);
  
  /** Determines whether this resolver strategy is "static" - that is, whether
   * it returns results which are specific to the current view. The primary
   * usage of this return is to determine whether to import *all* branch
   * IDs into global resolution, or just branch IDs which occur at top level
   * in the returned templates.
   */
  public boolean isStatic();

}
