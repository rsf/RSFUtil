/*
 * Created on 11-Jan-2006
 */
package uk.org.ponder.rsf.test.sitemap;

import java.util.HashMap;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import uk.org.ponder.rsf.viewstate.BasicViewParametersParser;
import uk.org.ponder.rsf.viewstate.EntityCentredViewParameters;

public class TestSiteMap {
  public static void main(String[] args) {

  FileSystemXmlApplicationContext fsxac = 
    new FileSystemXmlApplicationContext("classpath:uk/org/ponder/rsf/test/minicontext.xml");
   
  try {
    BasicViewParametersParser bvpp = (BasicViewParametersParser) fsxac.getBean("viewParametersParser");
    HashMap attrmap = new HashMap();
    attrmap.put("flowtoken", "ec38f0");
    EntityCentredViewParameters ecvp = (EntityCentredViewParameters) bvpp.parse("/recipe/3652/", attrmap);
    System.out.println("ECVP for entity " + ecvp.entity.ID + " of type " + ecvp.entity.entityname);
  }
  catch (Exception e) {
    e.printStackTrace();
  }
}
}

