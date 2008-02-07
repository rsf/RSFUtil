/*
 * Created on 7 Feb 2008
 */
package uk.org.ponder.rsf.templateresolver.support;

import java.io.InputStream;

import uk.org.ponder.rsf.templateresolver.TemplateResolverStrategy;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.stringutil.StringList;

public class TemplateResolutionContext {
  public ViewParameters viewparams;
  public TemplateResolverStrategy trs;
  public StringList bases;
  public StringList tried;
  public boolean logfailure;
  
  public String fullpath;
  public InputStream is;
  
  public TemplateResolutionContext(ViewParameters viewparams, 
      TemplateResolverStrategy trs, StringList tried, boolean logfailure) {
    this.viewparams = viewparams;
    this.trs = trs;
    this.tried = tried;
    this.logfailure = logfailure;
  }
}
