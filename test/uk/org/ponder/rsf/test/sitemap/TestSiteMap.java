/*
 * Created on 11-Jan-2006
 */
package uk.org.ponder.rsf.test.sitemap;

import uk.org.ponder.rsf.bare.junit.PlainRSFTests;
import uk.org.ponder.rsf.viewstate.EntityCentredViewParameters;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.SiteMap;

public class TestSiteMap extends PlainRSFTests {
  public TestSiteMap() {
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/sitemap/sitemap-context.xml");
  }
   
  public void testParseECVP() {
    SiteMap map = (SiteMap) applicationContext.getBean("siteMap");
    
    assertEquals(EntityCentredViewParameters.class, map.view.get("recipe").getClass());
    assertEquals(SimpleViewParameters.class, map.view.get("recipes").getClass());
    
 }

}

