/*
 * Created on 11 Sep 2008
 */
package uk.org.ponder.rsf.test.data.producers;

import uk.org.ponder.arrayutil.MapUtil;
import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.view.DataView;
import uk.org.ponder.rsf.viewstate.RawViewParameters;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ParamsDataView implements DataView {
  public static final String VIEW_ID = "paramdata";
  
  public Object getData(ViewParameters viewparams) {
    return MapUtil.make("selfView", new SimpleViewParameters(VIEW_ID), 
          "contextView", new RawViewParameters("$context/templates/images/image.jpg"));
  }

  public String getContentType() {
    return ContentTypeInfoRegistry.JSON;
  }

  public String getViewID() {
    return VIEW_ID;
  }

}
