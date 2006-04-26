/*
 * Created on 11-Jan-2006
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Iterator;

import uk.org.ponder.springutil.XMLFactoryBean;

/** A Spring FactoryBean that will embody a BasicViewParametersParser
 * object based on the contents of an XML-formatted sitemap file.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public class XMLSiteMapLoader extends XMLFactoryBean {
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
  
  public Object getObject() throws Exception {
    SiteMap sitemap = (SiteMap) super.getObject();
    for (Iterator vpit = sitemap.view.keySet().iterator(); vpit.hasNext();) {
      String viewid = (String) vpit.next();
      ViewParameters viewparams = (ViewParameters) sitemap.view.get(viewid);
      vpreceiver.setViewParamsExemplar(viewid, viewparams);
    }
    return sitemap;
  }
}
