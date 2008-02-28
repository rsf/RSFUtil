/*
 * Created on 28 Feb 2008
 */
package uk.org.ponder.rsf.test.selection.producers;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.test.selection.Category;
import uk.org.ponder.rsf.test.selection.CategoryFactory;
import uk.org.ponder.rsf.test.selection.params.CategoryViewParams;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class TestNullProducer implements ViewComponentProducer, ViewParamsReporter {
  public static final String VIEW_ID = "testnull";

  public String getViewID() {
    return VIEW_ID;
  }

  private CategoryFactory categoryFactory;

  public void setCategoryFactory(CategoryFactory categoryFactory) {
    this.categoryFactory = categoryFactory;
  }
  
  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
    CategoryViewParams vcp = (CategoryViewParams) viewparams;
    Category category1 = categoryFactory.findCategory(1);
    Category category2 = categoryFactory.findCategory(2);
    
    String[] names = new String[] {category1.name, category2.name, "null"};
    String[] values = new String[] {category1.id, category2.id, null};
    
    UIForm form = UIForm.make(tofill, "form");
    UISelect.make(form, "select", values, names, "recipe.category.id", 
        vcp.id == null? null : vcp.id.toString()).setIDDefunnel();
  }

  public ViewParameters getViewParameters() {
    return new CategoryViewParams();
  }


}
