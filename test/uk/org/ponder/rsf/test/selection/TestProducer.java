/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.selection;

import uk.org.ponder.rsf.bare.RequestLauncher;
import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIOutputMany;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class TestProducer implements ViewComponentProducer {

  public String getViewID() {
    return RequestLauncher.TEST_VIEW;
  }

  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
    UIForm form = UIForm.make(tofill, "form");
    UISelect category = UISelect.make(form, "recipe-category");
    category.optionlist = UIOutputMany.make("#{categories-all}", "#{fieldGetter.id}");
    category.optionnames = UIOutputMany.make("#{categories-all}", "#{fieldGetter.name}");
    category.selection = UIInput.make("recipe.category.id");
    category.selection.darreshaper = new ELReference("#{id-defunnel}");
  }

}
