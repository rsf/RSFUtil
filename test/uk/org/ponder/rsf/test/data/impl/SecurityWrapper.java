/*
 * Created on 27 Jul 2006
 */
package uk.org.ponder.rsf.test.data.impl;


import uk.org.ponder.rsf.test.data.beans.LogonBean;
import uk.org.ponder.rsf.test.data.producers.LogonProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.RunnableInvoker;

/**
 * A single centralised "Security Wrapper" that will let us apply our security
 * policy across the entire application from a single bean.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class SecurityWrapper implements RunnableInvoker {
  private ViewParameters viewparams;
  private LogonBean logonbean;

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }

  public void setLogonBean(LogonBean logonbean) {
    this.logonbean = logonbean;
  }

  public void invokeRunnable(Runnable towrap) {
    if (logonbean.name == null
        && !(LogonProducer.VIEW_ID.equals(viewparams.viewID))) {
      throw new SecurityException("Cannot view page " + viewparams.viewID
          + " while not logged on");
    }
    towrap.run();
  }
}
