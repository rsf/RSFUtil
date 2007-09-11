/*
 * Created on 17-Sep-2006
 */
package uk.org.ponder.rsf.view.support;

import java.util.List;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.producers.NullaryProducer;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ComponentProducer;
import uk.org.ponder.rsf.view.ViewNotFoundException;
import uk.org.ponder.rsf.view.ViewResolver;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.UniversalRuntimeException;

/** Accretes together all ViewProducers proper, for the application,
 * and aggregates them into this single producer which fills the components
 * specific for this view.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */ 
public class ViewCollector implements NullaryProducer {

  private ViewResolver viewresolver;
  private ComponentChecker checker;
  private ViewParameters viewparams;

  public void setViewResolver(ViewResolver viewlocator) {
    this.viewresolver = viewlocator;
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
  public void fillComponents(UIContainer tofill) {

    List producers = viewresolver.getProducers(viewparams.viewID);

    if (producers != null) {
      for (int i = 0; i < producers.size(); ++i) {
        ComponentProducer producer = (ComponentProducer) producers.get(i);

        producer.fillComponents(tofill, viewparams, checker);
      }

    }
    else {
      throw UniversalRuntimeException.accumulate(new ViewNotFoundException(),
          "Request intercepted for unknown view " + viewparams.viewID);
    }

  }
}
