/*
 * Created on 11-Jan-2006
 */
package uk.org.ponder.rsf.test.sitemap;

import java.util.HashMap;

import uk.org.ponder.rsf.test.PlainRSFTests;
import uk.org.ponder.rsf.viewstate.BasicViewParametersParser;
import uk.org.ponder.rsf.viewstate.EntityCentredViewParameters;

public class TestSiteMap extends PlainRSFTests {
  public void testParseECVP() {
    BasicViewParametersParser bvpp = (BasicViewParametersParser) applicationContext.getBean("viewParametersParser");
    HashMap attrmap = new HashMap();
    attrmap.put("flowtoken", "ec38f0");
    EntityCentredViewParameters ecvp = (EntityCentredViewParameters) bvpp.parse("/recipe/3652/", attrmap);
    System.out.println("ECVP for entity " + ecvp.entity.ID + " of type " + ecvp.entity.entityname);
  }

}

