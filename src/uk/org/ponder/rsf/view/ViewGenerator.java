/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.view;

import java.util.List;

import org.springframework.beans.factory.FactoryBean;

import uk.org.ponder.rsf.util.ComponentDumper;
import uk.org.ponder.rsf.util.ViewCollection;
import uk.org.ponder.streamutil.PrintOutputStream;
import uk.org.ponder.streamutil.PrintStreamPOS;
import uk.org.ponder.util.UniversalRuntimeException;
import uk.org.ponder.webapputil.ViewParameters;

public class ViewGenerator implements FactoryBean {
  private ViewCollection viewcollection;
  private View view;
  private ComponentChecker checker;
  private ViewParameters viewparams;
 
  public Object getObject() throws Exception {
    if (view == null) {
      view = generateView(viewparams, checker);
      PrintOutputStream dumppos = new PrintStreamPOS(System.out);
      ComponentDumper.dumpContainer(view.viewroot, 0, dumppos);
    }
    return view;
  }

  public void setViewCollection(ViewCollection viewcollection) {
    this.viewcollection = viewcollection;
  }
  
  public void setComponentChecker(ComponentChecker checker) {
    this.checker = checker;
  }
  
  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }
  
  /**
   * Returns the UIViewRoot for the view created by the View instance matching
   * the view ID. Any potentially recoverable errors are caught and a redirect
   * is issued onto a default page. If the error appears unrecoverable, simply
   * render an error page.
   */
  private View generateView(ViewParameters viewparams, ComponentChecker checker) {
    View view = new View();
    List producers = viewcollection.getProducers(viewparams.viewID);

    if (producers != null) {
      for (int i = 0; i < producers.size(); ++i) {
        ComponentProducer producer = (ComponentProducer) producers.get(i);

        producer.fillComponents(view.viewroot, viewparams, checker);
      }
      return view;
    }
    else {
      throw new UniversalRuntimeException(
          "Request intercepted for unknown view " + viewparams.viewID);
    }
  }
  
  public Class getObjectType() {
    return View.class;
  }

  public boolean isSingleton() {
    return true;
  }

}
