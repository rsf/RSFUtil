/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.view;

import org.springframework.beans.factory.FactoryBean;

import uk.org.ponder.webapputil.ViewParameters;

/** A utility bean to enable template loading to proceed by RSAC autowiring.
 * It's unclear whether this has improved the code structure significantly, but
 * is an interesting experiment. This is a FactoryBean since RSACBeanGetter
 * has not yet support for "factory-method".
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

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
