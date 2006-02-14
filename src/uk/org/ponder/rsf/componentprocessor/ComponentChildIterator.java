/*
 * Created on 14-Feb-2006
 */
package uk.org.ponder.rsf.componentprocessor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.beanutil.IterableBeanLocator;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.saxalizer.MethodAnalyser;
import uk.org.ponder.saxalizer.SAXAccessMethod;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;

/** Constructs an iteration over any children of an "composite component"
 * that are also components, by means of reflection.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class ComponentChildIterator implements IterableBeanLocator {
  private Map children = new HashMap();
  
  public ComponentChildIterator(UIComponent parent, SAXalizerMappingContext mappingcontext) {
    MethodAnalyser ma = mappingcontext.getAnalyser(parent.getClass());
    for (int i = 0; i < ma.allgetters.length; ++ i) {
      SAXAccessMethod sam = ma.allgetters[i];
      if (sam.tagname.equals("parent")) continue;
      if (UIComponent.class.isAssignableFrom(sam.getDeclaredType())) {
        Object child = sam.getChildObject(parent);
        if (child != null) {
          children.put(sam.tagname, child);
        }
      }
    }
  }
  
  public Iterator iterator() {
    return children.keySet().iterator();
  }

  public Object locateBean(String path) {
    return children.get(path);
  }
}
