/*
 * Created on 22 Jul 2006
 */
package uk.org.ponder.rsf.test.data.beans;

import uk.org.ponder.rsf.test.data.api.LogonService;
import uk.org.ponder.util.UniversalRuntimeException;

/** Processes the logon procedure, checking the details via LogonService,
 * and clearing up the fields after success or failure.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class LogonActionBean {
  private LogonBean logon;
  private LogonService logonservice;
  
  public void setLogonService(LogonService logonservice) {
    this.logonservice = logonservice;
  }

  public void setLogon(LogonBean logon) {
    this.logon = logon;
  }
  
  public void logon() {
    boolean success = false;
    String failmess = "Logon failed for user " + logon.name;
    try {
      success = logonservice.processLogon(logon);
    }
    catch (Exception e) {
      logonFailed();
      throw UniversalRuntimeException.accumulate(e, failmess);
    }
    finally {
      logon.password = null;
    }
    if (!success) {
      logonFailed();
      throw new SecurityException(failmess);
    }
  }

  private void logonFailed() {
    logon.name = null;
  }

}
