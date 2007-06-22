/*
 * Created on 11-Jan-2006
 */
package uk.org.ponder.rsf.test.sitemap;

import java.util.HashMap;

import uk.org.ponder.rsac.test.AbstractRSACTests;
import uk.org.ponder.rsf.viewstate.BasicViewParametersParser;
import uk.org.ponder.rsf.viewstate.EntityCentredViewParameters;

public class TestSiteMap extends AbstractRSACTests {
  public void testParseECVP() {
    BasicViewParametersParser bvpp = (BasicViewParametersParser) applicationContext.getBean("viewParametersParser");
    HashMap attrmap = new HashMap();
    attrmap.put("flowtoken", "ec38f0");
    EntityCentredViewParameters ecvp = (EntityCentredViewParameters) bvpp.parse("/recipe/3652/", attrmap);
    System.out.println("ECVP for entity " + ecvp.entity.ID + " of type " + ecvp.entity.entityname);
  }

  protected String[] getConfigLocations() {
    return new String[] {
        "classpath:conf/rsf-config.xml", "classpath:conf/blank-applicationContext.xml",
        "classpath:uk/org/ponder/rsf/test/sitemap/sitemap-context.xml"
    };
  }

  public String[] getRequestConfigLocations() {
    return new String[] {
        "classpath:conf/rsf-requestscope-config.xml",
        "classpath:conf/blank-requestContext.xml"};
  }
}

