/*
 * Created on 19 Sep 2006
 */
package uk.org.ponder.rsf.templateresolver;

import uk.org.ponder.rsf.viewstate.ContextURLProvider;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.stringutil.StringList;

/**
 * A simple TemplateResolverStrategy that follows a static strategy guided by
 * field values.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class StaticTemplateResolverStrategy implements RootAwareTRS,
    BaseAwareTemplateResolverStrategy {
  private String templateResourceBase = "/";
  private int rootResolverPriority = 1;
  private String names;
  private ContextURLProvider cup;
  private String externalURLbase;
  private String basedir;

  public void setContextURLProvider(ContextURLProvider cup) {
    this.cup = cup;
  }

  /** Set a comma-separated list of template names to be returned */
  public void setTemplateNames(String names) {
    this.names = names;
  }

  public void setRootResolverPriority(int rootResolverPriority) {
    this.rootResolverPriority = rootResolverPriority;
  }

  /** Set the base directory in which templates will be found * */
  public void setBaseDirectory(String basedir) {
    this.basedir = basedir;
  }

  public StringList resolveTemplatePath(ViewParameters viewparams) {
    StringList togo = StringList.fromString(names);
    for (int i = 0; i < togo.size(); ++i) {
      togo.set(i, basedir + togo.get(i));
    }
    return togo;
  }

  public void setExternalURLBase(String externalURLBase) {
    this.externalURLbase = externalURLBase;
  }

  public String getExternalURLBase() {
    return externalURLbase == null ? cup.getContextBaseURL()
        : externalURLbase;
  }

  public String getTemplateResourceBase() {
    return templateResourceBase;
  }

  public int getRootResolverPriority() {
    return rootResolverPriority;
  }

}
