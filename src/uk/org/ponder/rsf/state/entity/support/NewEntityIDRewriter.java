/*
 * Created on 10-Jan-2006
 */
package uk.org.ponder.rsf.state.entity.support;

import uk.org.ponder.beanutil.entity.EntityID;
import uk.org.ponder.beanutil.entity.EntityIDRewriter;
import uk.org.ponder.saxalizer.MethodAnalyser;
import uk.org.ponder.saxalizer.SAXAccessMethod;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;

public class NewEntityIDRewriter {
  
  public static void rewriteEntityIDs(Object target, SAXalizerMappingContext smc, 
      EntityIDRewriter idprocessor) {
    MethodAnalyser ma = smc.getAnalyser(target.getClass());
    for (int i = 0; i < ma.allgetters.length; ++i) {
      SAXAccessMethod getter = ma.allgetters[i];
      if (EntityID.class.isAssignableFrom(getter.getDeclaredType())) {
        EntityID entityid = (EntityID) getter.getChildObject(target);
        idprocessor.postCommit(entityid);
      }
    }
  }
}
