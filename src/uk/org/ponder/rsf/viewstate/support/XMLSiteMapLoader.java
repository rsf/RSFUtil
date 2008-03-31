/*
 * Created on 11-Jan-2006
 */
package uk.org.ponder.rsf.viewstate.support;

import java.util.Iterator;

import org.springframework.beans.factory.InitializingBean;

import uk.org.ponder.rsf.viewstate.SiteMap;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReceiver;
import uk.org.ponder.springutil.XMLFactoryBean;

/** A Spring FactoryBean that will embody a BasicViewParametersParser
 * object based on the contents of an XML-formatted sitemap file.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public class XMLSiteMapLoader extends XMLFactoryBean implements InitializingBean {
  private ViewParamsReceiver vpreceiver;

  public XMLSiteMapLoader() {
    setObjectType(SiteMap.class);
  }
  
  public Class getObjectType() {
    return SiteMap.class;
  }
  
  public void setViewParametersReceiver(ViewParamsReceiver vpreceiver) {
    this.vpreceiver = vpreceiver;
  }
  
  private SiteMap sitemap;
  
  private SiteMap fetchSiteMap() throws Exception {
    if (sitemap == null) {
      sitemap = (SiteMap) super.getObject();
      for (Iterator vpit = sitemap.view.keySet().iterator(); vpit.hasNext();) {
        String viewid = (String) vpit.next();
        ViewParameters viewparams = (ViewParameters) sitemap.view.get(viewid);
        vpreceiver.setViewParamsExemplar(viewid, viewparams);
      }
      return sitemap;
    }
    return sitemap;
  }
  
  public Object getObject() throws Exception {
    return fetchSiteMap();
  }

  public void afterPropertiesSet() throws Exception {
    fetchSiteMap();
  }
}
