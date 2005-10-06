/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.view;

import org.springframework.beans.factory.FactoryBean;

import uk.org.ponder.webapputil.ViewParameters;

public class TemplateLoaderBean implements FactoryBean {
  private ViewParameters viewparams;
  private TemplateResolver resolver;

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }
  public void setTemplateResolver(TemplateResolver resolver) {
    this.resolver = resolver;
  }
 
  public Object getObject() throws Exception {
    return resolver.locateTemplate(viewparams);
  }

  public Class getObjectType() {
    return ViewTemplate.class;
  }

  public boolean isSingleton() {
    return true;
  }

}
