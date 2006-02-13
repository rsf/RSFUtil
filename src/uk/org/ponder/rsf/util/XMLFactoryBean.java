/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.util;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.saxalizer.XMLProvider;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/** A very useful base class for any bean constructed out of an XML
 * representation.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class XMLFactoryBean implements FactoryBean, ApplicationContextAware {
  String location;
  private ApplicationContext applicationcontext;
  private XMLProvider xmlprovider;
  private Class objecttype;
  protected ReflectiveCache reflectivecache;

  public void setLocation(String location) {
    this.location = location;
  }

  public void setXMLProvider(XMLProvider xmlprovider) {
    this.xmlprovider = xmlprovider;
  }

  public void setReflectiveCache(ReflectiveCache reflectivecache) {
    this.reflectivecache = reflectivecache;
  }
  
  public void setObjectType(Class objecttype) {
    this.objecttype = objecttype;
  }

  public Class getObjectType() {
    return objecttype;
  }
  
  public Object getObject() throws Exception {
    Resource res = applicationcontext.getResource(location);
    Object togo = null;
    try {
      InputStream is = res.getInputStream();
      togo = xmlprovider.readXML(objecttype, is);
    }
    catch (Exception e) {
      UniversalRuntimeException tothrow = UniversalRuntimeException.accumulate(e, "Error loading object from path " + res +":  ");
      if (tothrow.getTargetException() instanceof IOException) {
        Logger.log.warn(tothrow.getTargetException().getClass().getName() + ": " +tothrow.getMessage());
        togo = reflectivecache.construct(objecttype);
      }
      else {
        throw tothrow;
      }
    }
    return togo;
  }

  public boolean isSingleton() {
    return true;
  }

  public void setApplicationContext(ApplicationContext applicationcontext)
      throws BeansException {
    this.applicationcontext = applicationcontext;
  }

}