/*
 * Created on 11-Jan-2006
 */
package uk.org.ponder.rsf.viewstate;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.rsf.util.XMLFactoryBean;

/** A Spring FactoryBean that will embody a BasicViewParametersParser
 * object based on the contents of an XML-formatted sitemap file.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public class XMLSiteMapLoader extends XMLFactoryBean {
  private BeanModelAlterer bma;

  public XMLSiteMapLoader() {
    setObjectType(SiteMap.class);
  }
  
  public void setBeanModelAlterer(BeanModelAlterer bma) {
    this.bma = bma;
  }
  
  public Class getObjectType() {
    return BasicViewParametersParser.class;
  }

  public Object getObject() throws Exception {
    SiteMap sitemap = (SiteMap) super.getObject();
    BasicViewParametersParser togo = new BasicViewParametersParser();
    togo.setViewParametersMap(sitemap.view);
    togo.setBeanModelAlterer(bma);
    return togo;
  }
}
