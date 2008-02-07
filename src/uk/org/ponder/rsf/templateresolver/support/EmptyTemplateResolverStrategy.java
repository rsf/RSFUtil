/*
 * Created on 7 Feb 2008
 */
package uk.org.ponder.rsf.templateresolver.support;

import uk.org.ponder.rsf.templateresolver.BaseAwareTemplateResolverStrategy;
import uk.org.ponder.rsf.templateresolver.RootAwareTRS;
import uk.org.ponder.rsf.templateresolver.TemplateResolverStrategy;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.stringutil.StringList;

/**
 * A template resolver which always returns an empty template, primarily for
 * testing purposes.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class EmptyTemplateResolverStrategy implements TemplateResolverStrategy,
    BaseAwareTemplateResolverStrategy, RootAwareTRS {

  public boolean isStatic() {
    return false;
  }

  public StringList resolveTemplatePath(ViewParameters viewparams) {
    return new StringList(
        "classpath:uk/org/ponder/rsf/templateresolver/support/empty-template");
  }

  public String getExternalURLBase() {
    return "";
  }

  public String getTemplateResourceBase() {
    return "";
  }

  public int getRootResolverPriority() {
    return 1;
  }

}
