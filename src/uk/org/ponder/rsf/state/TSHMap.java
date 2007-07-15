/*
 * Created on 15 Jul 2007
 */
package uk.org.ponder.rsf.state;


import uk.org.ponder.util.ObstinateMap;

/** An adaptor from the RSF "TokenStateHolder" interface for those who would
 * prefer a JDK map. The supplied keys must be Strings.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class TSHMap extends ObstinateMap {
  private TokenStateHolder tsh;

  public TSHMap(TokenStateHolder tsh) {
    this.tsh = tsh;
  }

  public boolean containsKey(Object key) {
    return get(key) != null;
  }

  public Object get(Object key) {
    return tsh.getTokenState((String) key);
  }

  public Object put(Object key, Object value) {
    Object togo = get(key);
    tsh.putTokenState((String) key, value);
    return togo;
  }
  
  public Object remove(Object key) {
    Object togo = get(key);
    tsh.clearTokenState((String) key);
    return togo;
  }
}
