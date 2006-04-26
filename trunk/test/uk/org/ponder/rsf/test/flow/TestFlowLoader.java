/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.test.flow;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.saxalizer.XMLProvider;
import uk.org.ponder.streamutil.write.PrintStreamPOS;

public class TestFlowLoader {
  public static void main(String[] args) {
//    SAXalizerMappingContext smc = new SAXalizerMappingContext();
//    ReflectiveCache rcache = new JDKReflectiveCache();
//    smc.setReflectiveCache(rcache);
//    RSFMappingLoader rml = new RSFMappingLoader();
//    SAXalXMLProvider xmlprovider = new SAXalXMLProvider(smc);
//    
//    rml.loadStandardMappings(xmlprovider);
//    XmlFlowFactoryBean xffb = new XmlFlowFactoryBean();
//    
    FileSystemXmlApplicationContext fsxac = 
      new FileSystemXmlApplicationContext("classpath:uk/org/ponder/rsf/test/minicontext.xml");
//    xffb.setXMLProvider(xmlprovider);
//    xffb.setLocation("classpath:uk/org/ponder/rsf/test/flow/numberGuess-flow.xml");
     
    try {
//      Flow flow = (Flow) fsxac.getBean("numberGuessFlowDef");
//      System.out.println(flow.id);
      UIBranchContainer root = (UIBranchContainer) fsxac.getBean("viewtree");
      PrintStreamPOS pos = new PrintStreamPOS(System.out);
      XMLProvider xmlp = (XMLProvider) fsxac.getBean("XMLProvider");
      System.out.println(xmlp.toString(root));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
