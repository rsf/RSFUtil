/*
 * Created on 22 Jun 2007
 */
package uk.org.ponder.rsf.test.dar;

import uk.org.ponder.arrayutil.ArrayUtil;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.mapping.DARApplier;
import uk.org.ponder.mapping.DataAlterationRequest;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.test.PlainRSFTests;

public class TestDARApplier extends PlainRSFTests {
  
  private DARTestBean testApplication(Object value) {
    WriteableBeanLocator rbl = getRSACBeanLocator().getBeanLocator();
    DataAlterationRequest dar = new DataAlterationRequest("DARTestBean.values", value);
    DARApplier darapplier = (DARApplier) applicationContext.getBean("DARApplier");
    TargettedMessageList tml = new TargettedMessageList();
    darapplier.applyAlteration(rbl, dar, tml, null);
    
    DARTestBean togo = (DARTestBean) rbl.locateBean("DARTestBean");
    return togo;
  }
  
  public void testStringVector() {    
    DARTestBean testBean = testApplication("value");
    assertNotNull(testBean.values);
    assertEquals(testBean.values.length, 1);
    assertEquals(testBean.values[0], "value");
    
    testBean = testApplication(new String[] {"value1", "value2"});
    assertNotNull(testBean.values);
    assertEquals(testBean.values.length, 2); 
  }

  
  public String[] getRequestConfigLocations() {
    return (String[]) ArrayUtil.append(super.getRequestConfigLocations(),
        "classpath:uk/org/ponder/rsf/test/dar/dar-request-context.xml"
        );
  }
}
