/*
 * Created on 22 Jul 2006
 */
package uk.org.ponder.rsf.test.data.api;

import uk.org.ponder.rsf.test.data.beans.LogonBean;


/** An API to a service which will process a logon (by checking against a
 * password database, for example).
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface LogonService {
  /**
   * @return <code>true</code> if the logon was successful.
   */
  public boolean processLogon(LogonBean logon);
}
