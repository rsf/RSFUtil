/*
 * Created on 13-Feb-2006
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Map;

public interface ViewParamsReceiver {
  public void setViewParamsExemplar(String viewid, ViewParameters vpexemplar);
  public void setViewParametersMap(Map map);
  public void setDefaultView(String viewid);
}
