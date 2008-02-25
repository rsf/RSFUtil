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
  
  public void testSelection() {
    RequestLauncher launch1 = getRequestLauncher();
    setCategorySize(launch1, 1);
    RenderResponse response = launch1.renderView();
    UIForm form = (UIForm) response.viewWrapper.queryComponent(new UIForm());
    UISelect selection = (UISelect) response.viewWrapper.queryComponent(new UISelect());
    
    selection.selection.updateValue("1");
    
    ActionResponse response2 = getRequestLauncher().submitForm(form, null);
    Recipe recipe = (Recipe) response2.requestContext.locateBean("recipe");
    
    assertNull("Request expected without error", 
        ((ViewParameters)response2.ARIResult.resultingView).errortoken);
    
    assertEquals(recipe.category.id, "1");
    assertNotNull(recipe.category.name);
    
    }
}
