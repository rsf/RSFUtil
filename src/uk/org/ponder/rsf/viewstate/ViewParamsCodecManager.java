/*
 * Created on 23 Jul 2007
 */
package uk.org.ponder.rsf.viewstate;

import java.util.List;
import java.util.Map;

public class ViewParamsCodecManager implements ViewParamsCodec {
  private List codecs;

  public void setViewParamsCodecs(List codecs) {
    this.codecs = codecs;
  }
  
  public boolean parseViewParams(ViewParameters target, RawURLState rawstate, Map unusedParams) {
    for (int i = 0; i < codecs.size(); ++ i) {
      ViewParamsCodec codec = (ViewParamsCodec) codecs.get(i);
      boolean result = codec.parseViewParams(target, rawstate, null);
      if (result) return true;
    }
    return false;
  }

  public RawURLState renderViewParams(ViewParameters toparse) {
    for (int i = 0; i < codecs.size(); ++ i) {
      ViewParamsCodec codec = (ViewParamsCodec) codecs.get(i);
      RawURLState togo = codec.renderViewParams(toparse);
      if (togo != null) return togo;
    }
    return null;
  }

}
