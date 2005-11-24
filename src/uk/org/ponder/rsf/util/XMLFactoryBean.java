/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.util;

import java.io.InputStream;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import uk.org.ponder.rsf.util.StandardBeanFinder;
import uk.org.ponder.saxalizer.XMLProvider;

/** A very useful base class for any bean constructed out of an XML
 * representation.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public abstract class XMLFactoryBean implements FactoryBean, ApplicationContextAware {
  String location;
  private ApplicationContext applicationcontext;
  private XMLProvider xmlprovider;

  public void setLocation(String location) {
    this.location = location;
  }

  public void setXMLProvider(XMLProvider xmlprovider) {
    this.xmlprovider = xmlprovider;
  }

  public Object getObject() throws Exception {
    Resource res = applicationcontext.getResource(location);
    InputStream is = res.getInputStream();
    
    Object togo = xmlprovider.readXML(getObjectType(), is);
    return togo;
  }

  public boolean isSingleton() {
    return true;
  }

  public void setApplicationContext(ApplicationContext applicationcontext)
      throws BeansException {
    this.applicationcontext = applicationcontext;
    this.xmlprovider = (XMLProvider) StandardBeanFinder.findBean(
        XMLProvider.class, applicationcontext);
  }
}