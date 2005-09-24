/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.view;

import org.springframework.beans.factory.FactoryBean;

import uk.org.ponder.rsf.template.XMLViewTemplate;
import uk.org.ponder.util.Copiable;
import uk.org.ponder.webapputil.BlankViewParameters;
import uk.org.ponder.webapputil.ViewParameters;

public class TemplateLoaderBean implements Copiable, FactoryBean {
  private ViewParameters viewparams;
  private TemplateResolver resolver;

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }
  public void setTemplateResolver(TemplateResolver resolver) {
    this.resolver = resolver;
  }
  
  public Object copy() {
    TemplateLoaderBean togo = new TemplateLoaderBean();
    togo.resolver = resolver;
    return togo;
  }

  public Object getObject() throws Exception {
    // we are probably a dead bean.
    if (viewparams instanceof BlankViewParameters) {
      return new XMLViewTemplate();
    }
    else {
      return resolver.locateTemplate(viewparams);
    }
  }

  public Class getObjectType() {
    return ViewTemplate.class;
  }

  public boolean isSingleton() {
    return true;
  }

}
