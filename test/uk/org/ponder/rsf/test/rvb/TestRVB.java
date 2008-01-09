/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.rvb;

import uk.org.ponder.arrayutil.ArrayUtil;
import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.rsf.bare.junit.PlainRSFTests;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.util.RSFUtil;
import uk.org.ponder.rsf.viewstate.EntityCentredViewParameters;

// Test for composite Resulting View Bindings issue, reported in forums at
// http://ponder.org.uk/rsf/posts/list/8.page

public class TestRVB extends PlainRSFTests {
  public String[] getRequestConfigLocations() {
    return (String[]) ArrayUtil.append(super.getRequestConfigLocations(),
        "classpath:uk/org/ponder/rsf/test/rvb/rvb-request-context.xml"
        );
  }
  
  public void testResultingViewBindings() {
    UIForm dummyform = new UIForm();
    RSFUtil.addResultingViewBinding(dummyform, "entity.ID", "idholder.id");
    UICommand command = UICommand.make(dummyform, "mycommand", "idholder.act");
    
    BeanLocator result = getRequestLauncher().submitForm(dummyform, command);
    
    ARIResult ariresult = (ARIResult) result.locateBean("ARIResultConcrete");
    assertTrue(ariresult.resultingView instanceof EntityCentredViewParameters);
    EntityCentredViewParameters ecvp = (EntityCentredViewParameters) ariresult.resultingView;
    assertEquals(ecvp.entity.ID, IDHolder.NEW_ID);
  }
}
