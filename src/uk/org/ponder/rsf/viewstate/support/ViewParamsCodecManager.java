/*
 * Created on 23 Jul 2007
 */
package uk.org.ponder.rsf.viewstate.support;

import java.util.List;
import java.util.Map;

import uk.org.ponder.rsf.viewstate.RawURLState;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsCodec;
import uk.org.ponder.rsf.viewstate.ViewParamsMapInfo;

public class ViewParamsCodecManager implements ViewParamsCodec {
  private List codecs;

  public void setViewParamsCodecs(List codecs) {
    this.codecs = codecs;
  }
  
  public ViewParamsCodec getCodec(ViewParameters viewparams) {
    for (int i = 0; i < codecs.size(); ++ i) {
      ViewParamsCodec codec = (ViewParamsCodec) codecs.get(i);
      if (codec.isSupported(viewparams)) return codec;
    }
    return null;
  }

  public boolean isSupported(ViewParameters viewparams) {
    return true;
  }

  public boolean parseViewParams(ViewParameters target, RawURLState rawstate,
      Map unusedParams) {
    return getCodec(target).parseViewParams(target, rawstate, unusedParams);
  }

  public RawURLState renderViewParams(ViewParameters toparse) {
    return getCodec(toparse).renderViewParams(toparse);
  }

  public ViewParamsMapInfo getMappingInfo(ViewParameters viewparams) {
    return getCodec(viewparams).getMappingInfo(viewparams);
  }
 
}
