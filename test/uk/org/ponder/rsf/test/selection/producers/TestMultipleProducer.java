/*
 * Created on 28 Feb 2008
 */
package uk.org.ponder.rsf.test.selection.producers;

import uk.org.ponder.conversion.VectorCapableParser;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.test.selection.params.MultipleViewParams;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class TestMultipleProducer implements ViewComponentProducer, ViewParamsReporter {
  public static final String VIEW_ID = "testmultiple";

  public void setVectorCapableParser(VectorCapableParser vectorCapableParser) {
    this.vectorCapableParser = vectorCapableParser;
  }

  public String getViewID() {
    return VIEW_ID;
  }

  private VectorCapableParser vectorCapableParser;
  
  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
    
    MultipleViewParams mvp = (MultipleViewParams) viewparams;
    
    UIForm form = UIForm.make(tofill, "form");
    
    String[] options = new String[mvp.selsize];
    for (int i = 0; i < options.length; ++ i) {
      options[i] = i == 2? null : Integer.toString(i);
    }
    String[] selection = new String[mvp.selected.length];
    vectorCapableParser.render(mvp.selected, selection, null);
    
    UISelect.makeMultiple(form, "select", options, options, 
        mvp.primitive? "intBean.primitive" : "intBean.reference", selection);
    
  }

  public ViewParameters getViewParameters() {
    return new MultipleViewParams();
  }

  
}
