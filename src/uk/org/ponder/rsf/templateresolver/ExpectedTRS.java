/*
 * Created on 13 Nov 2006
 */
package uk.org.ponder.rsf.templateresolver;

/** A tag interface determining whether a successfully resolved template is
 * expected from this resolver. Resolvers *not* implementing this interface will
 * be assumed to default to <code>true</code> 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface ExpectedTRS {

  /** Indicates whether a resolved template is "expected" from this
  * TRS. An error will be thrown if none from the complete set of 
  * TemplateResolverStrategies implementing this interface returns any templates,
  * listing the paths which were searched. */
  public boolean isExpected();
}
