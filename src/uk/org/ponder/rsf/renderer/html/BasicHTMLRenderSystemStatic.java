/*
 * Created on Nov 14, 2005
 */
package uk.org.ponder.rsf.renderer.html;

import java.util.Map;

import uk.org.ponder.rsf.renderer.RenderSystemStatic;
import uk.org.ponder.rsf.renderer.RenderUtil;
import uk.org.ponder.rsf.request.FossilizedConverter;
import uk.org.ponder.rsf.request.SubmittedValueEntry;

public class BasicHTMLRenderSystemStatic implements RenderSystemStatic {

  public void normalizeRequestMap(Map requestparams) {
    String key = RenderUtil.findCommandParams(requestparams);
    if (key != null) {
      String params = key.substring(FossilizedConverter.COMMAND_LINK_PARAMETERS.length());
      RenderUtil.unpackCommandLink(params, requestparams);
      requestparams.remove(key);
    }
  }
  
  public void fixupUIType(SubmittedValueEntry sve) {
    if (sve.oldvalue instanceof Boolean) {
      if (sve.newvalue == null) sve.newvalue = Boolean.FALSE;
    }
    else if (sve.oldvalue instanceof String[]) {
      if (sve.newvalue == null) sve.newvalue = new String[]{};
    }
  }

}
