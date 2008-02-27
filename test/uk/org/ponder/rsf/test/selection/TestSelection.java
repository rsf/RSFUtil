/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.selection;

import uk.org.ponder.rsf.bare.ActionResponse;
import uk.org.ponder.rsf.bare.RenderResponse;
import uk.org.ponder.rsf.bare.RequestLauncher;
import uk.org.ponder.rsf.bare.junit.MultipleRSFTests;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Test for general operation of EL context, including fieldGetter, IDDefunnelingReshaper,
 * UISelect edge cases
 */

public class TestSelection extends MultipleRSFTests {
  
  public TestSelection() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/selection/selection-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/selection/selection-application-context.xml");
  }
  
  private void setCategorySize(RequestLauncher launch, int size) {
    CategoriesAll all = (CategoriesAll) launch.getRSACBeanLocator().getBeanLocator().locateBean("&categories-all");
    all.setSize(1);
  }
  
  private void testSubmit(int initsize, Integer initselection, String userselection) {
    RequestLauncher launch1 = getRequestLauncher();
    setCategorySize(launch1, initsize);
    RenderResponse response = launch1.renderView(
        new CategoryViewParams(RequestLauncher.TEST_VIEW, initselection));
    
    UIForm form = (UIForm) response.viewWrapper.queryComponent(new UIForm());
    UISelect selection = (UISelect) response.viewWrapper.queryComponent(new UISelect());
    
    selection.selection.updateValue(userselection);
    
    ActionResponse response2 = getRequestLauncher().submitForm(form, null);
    Recipe recipe = (Recipe) response2.requestContext.locateBean("recipe");
    
    assertNull("Request expected without error", 
        ((ViewParameters)response2.ARIResult.resultingView).errortoken);
    
    boolean differ = !(initselection == null && userselection == null)
      && (initselection == null ^ userselection == null) 
      || !userselection.equals(initselection.toString());
    if (differ) {
      assertEquals(recipe.category.id, userselection);
      assertNotNull(recipe.category.name);
    }
    else {
      assertNull(recipe);    
    }
  }
  
  public void testSelection() {
    testSubmit(1, null, "1");
    testSubmit(1, new Integer(1), "1");
    }
}
