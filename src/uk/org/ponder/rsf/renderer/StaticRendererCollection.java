/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.renderer;

import java.util.HashMap;
import java.util.List;

import uk.org.ponder.util.Copiable;

public class StaticRendererCollection implements Copiable {
  // a hashmap of either Strings or Component classes 
  private HashMap renderers = new HashMap();
  public void addSCR(StaticComponentRenderer renderer) {
    renderers.put(renderer.getName(), renderer);
  }
  
  public StaticComponentRenderer getSCR(String name) {
    return (StaticComponentRenderer) renderers.get(name);
  }
  
  public void setStaticRenderers(List rendererlist) {
    for (int i = 0; i < rendererlist.size(); ++ i) {
      Object entry = rendererlist.get(i);
      if (entry instanceof StaticComponentRenderer) {
        addSCR((StaticComponentRenderer) entry);
      }
      else {
        renderers.putAll(((StaticRendererCollection)entry).renderers);
      }
    }
  }
  
  public Object copy() {
    StaticRendererCollection togo = new StaticRendererCollection();
    togo.renderers.putAll(renderers);
    return togo;
  }
}
