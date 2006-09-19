/*
 * Created on 19 Sep 2006
 */
package uk.org.ponder.rsf.templateresolver;

/** An superinterface for a TemplateResolverStrategy that explicitly participates
 * in root resolution. A TRS that does *not* implement this interface will have
 * inferred a default priority of <code>1</code>
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface RootAwareTRS extends TemplateResolverStrategy {
  /**
   * Returns positive numbers if this is a resolver for root templates (i.e.
   * those at which rendering begins for a view). The highest value
   * of resolver priority found for a particular request will be used to
   * determine the template actually used to render the root. <br>
   * A value of "0" indicates this template should *never* be used as a root
   * template.
   * <p>
   * A value of "1" is a default value given to various framework interfaces
   * (such as {@link CRITemplateResolverStrategy}) that *should in general* be
   * root templates.
   * <p>
   * User code should use a value of "2" or more to take priority over framework
   * defaults, where they want to override the default choice of root template.
   */
  public int getRootResolverPriority();
}
