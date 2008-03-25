/*
 * Created on 22 Jul 2006
 */
package uk.org.ponder.rsf.test.data.impl;

import uk.org.ponder.rsf.test.data.api.LogonService;
import uk.org.ponder.rsf.test.data.beans.LogonBean;

public class BasicLogonService implements LogonService {

  public boolean processLogon(LogonBean logon) {
    return ("Edward".equals(logon.name) && "Longshanks".equals(logon.password));
  }

}
