/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.view;

import java.util.List;

import uk.org.ponder.rsf.util.ComponentDumper;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.streamutil.write.StringPOS;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/** Invokes the list of ComponentProducers in the supplied ViewCollection to
 * populate the view tree for this request. A request-scope bean, but is 
 * currently invoked manually.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class ViewGenerator {
  private ViewResolver viewresolver;
  private View view;
  private ComponentChecker checker;
  private ViewParameters viewparams;
 // This method is called manually from GetHandler.
  public View getView() {
    if (view == null) {
      view = generateView(viewparams, checker);
      if (Logger.log.isDebugEnabled()) {
        StringPOS dumppos = new StringPOS();
        dumppos.println("View component dump:");
        ComponentDumper.dumpContainer(view.viewroot, 0, dumppos);
        Logger.log.debug(dumppos);
      }
    }
    return view;
  }

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
  private View generateView(ViewParameters viewparams, ComponentChecker checker) {
    View view = new View();
    List producers = viewresolver.getProducers(viewparams.viewID);

    if (producers != null) {
      for (int i = 0; i < producers.size(); ++i) {
        ComponentProducer producer = (ComponentProducer) producers.get(i);

        producer.fillComponents(view.viewroot, viewparams, checker);
      }
      return view;
    }
    else {
      throw UniversalRuntimeException.accumulate(new ViewNotFoundException(), 
          "Request intercepted for unknown view " + viewparams.viewID);
    }
  }

}
