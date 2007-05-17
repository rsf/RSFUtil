/*
 * Created on Sep 26, 2006
 */
package uk.org.ponder.rsf.templateresolver;

/** A TemplateResolverStrategy that distinguishes that its returns should
 * *all* be aggregated into the current template set, rather than the
 * first that resolves.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public interface MultipleTemplateResolverStrategy extends TemplateResolverStrategy {
  /** @return <code>true</code> if ALL the strings returns from this resolver
   * should be used to aggregate templates for this request. If <code>false</code>,
   * aggregating will stop at the first template which resolves successfully.
   */
  boolean isMultiple();
}
