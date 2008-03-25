/*
 * Created on 24 Mar 2008
 */
package uk.org.ponder.rsf.test.data.producers;

import uk.org.ponder.arrayutil.MapUtil;
import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.test.data.beans.LogonBean;
import uk.org.ponder.rsf.view.DataView;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class UserDataView implements DataView {
  public static final String VIEW_ID = "userdata";
  
  public void setLogonBean(LogonBean logonBean) {
    this.logonBean = logonBean;
  }

  public String getViewID() {
    return VIEW_ID; 
  }
 
  private LogonBean logonBean;
  
  public Object getData(ViewParameters viewparams) {
    return MapUtil.make("userid", logonBean.name, "displayName", "Edward Longshanks");
  }

  public String getContentType() {
    return ContentTypeInfoRegistry.JSON;
  }

}
