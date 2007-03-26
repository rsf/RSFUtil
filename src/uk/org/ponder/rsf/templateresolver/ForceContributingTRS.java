/*
 * Created on 26 Mar 2007
 */
package uk.org.ponder.rsf.templateresolver;

/** A TemplateResolverStrategy that forces contribution of its collecting matter,
 * whether it is referenced or not.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface ForceContributingTRS {
  public void setMustContribute(boolean mustcontribute);
  
  public boolean getMustContribute();
}
