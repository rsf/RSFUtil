/*
 * Created on 13-Feb-2006
 */
package uk.org.ponder.rsf.flow.jsfnav;

import java.util.List;
import java.util.Map;

import uk.org.ponder.reflect.ReflectiveCache;

/** Maintains an application-wide pool of navigation cases reported by views. This
 * communicates with ViewProducers to receive any local definitions of NavigationCases
 * that the views have generated, in order to ease the common case of single-step
 * POSTS.
 * <p>Note that for efficiency reasons, this class operate VERY SPECIFIC SEMANTICS
 * on the contained objects. 
 * <p>Firstly, it will apply object identity by reference
 * handle on the NavigationCase lists that it is supplied with - in the most
 * common case that the navigation cases have not changed since the last time
 * the ViewProducer was executed, this should be signalled by returning the
 * SELFSAME list handle to this class, which will then be able to avoid blocking
 * the ConcurrentHashMap it contains by issuing a write.
 * <p>Secondly, and as a consequence, NavigationCase lists, once supplied to,
 * or after delivered from this class, must NEVER be modified!! To change the
 * stored lists, supply a different handle.
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public class NavigationCasePooler implements NavigationCaseReceiver {

  private Map pooledmap;

  public void setReflectiveCache(ReflectiveCache reflectivecache) {
    pooledmap = reflectivecache.getConcurrentMap(1);
  }
  
  public void reportNavigationCase(String viewid, List navigationCases) {
    
    if (navigationCases != null) {
      Object oldcases = pooledmap.get(viewid);
      if (navigationCases != oldcases) {
        pooledmap.put(viewid, navigationCases);
      }
    }
  }

  public Map getPooledMap() {
    return pooledmap;
  }

}
