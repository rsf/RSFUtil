/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.view;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Level;

import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReceiver;
import uk.org.ponder.rsf.producers.NullaryProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.saxalizer.XMLProvider;
import uk.org.ponder.streamutil.write.StringPOS;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * Invokes the list of ComponentProducers in the supplied ViewCollection to
 * populate the view tree for this request. A request-scope bean, but is
 * currently invoked manually (from RSFRenderHandler).
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class ViewGenerator {
  private ViewParameters viewParamsProxy;
  private NavigationCaseReceiver navreceiver;
  private XMLProvider xmlprovider;
  private NullaryProducer pageproducer;

  public void setViewParamsProxy(ViewParameters viewParameters) {
    this.viewParamsProxy = viewParameters;
  }

  public void setNavigationCaseReceiver(NavigationCaseReceiver navreceiver) {
    this.navreceiver = navreceiver;
  }

  public void setPageProducer(NullaryProducer pageproducer) {
    this.pageproducer = pageproducer;
  }

  public void setXMLProvider(XMLProvider xmlprovider) {
    this.xmlprovider = xmlprovider;
  }

  // This method is called manually from GetHandler.
  public View generateView() {
    View view = new View();
    pageproducer.fillComponents(view.viewroot);
    if (view.viewroot.navigationCases != null) {
      ViewParameters params = (ViewParameters) viewParamsProxy.get();
      navreceiver.receiveNavigationCases(params.viewID,
          view.viewroot.navigationCases);
    }
    if (Logger.log.isDebugEnabled() || view.viewroot.debug) {
      StringPOS dumppos = new StringPOS();
      dumppos.println("View component dump:");
      try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        xmlprovider.writeXML(view.viewroot, baos);
        dumppos.println(new String(baos.toByteArray(), "UTF-8"));
        Logger.log.log(view.viewroot.debug ? Level.ERROR
            : Level.DEBUG, dumppos);
      }
      catch (Exception e) {
        throw UniversalRuntimeException.accumulate(e,
            "Error dumping component tree for debug: ");
      }
    }
    return view;
  }

}
