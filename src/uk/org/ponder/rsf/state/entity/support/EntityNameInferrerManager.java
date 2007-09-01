/*
 * Created on 9 Aug 2007
 */
package uk.org.ponder.rsf.state.entity.support;

import java.util.List;

import uk.org.ponder.rsf.state.entity.EntityNameInferrer;

public class EntityNameInferrerManager implements EntityNameInferrer {
  private List inferrers;
  public void setInferrers(List inferrers) {
    this.inferrers = inferrers;
  }
  public String getEntityName(Class entityclazz) {
    if (inferrers == null) return null;
    for (int i = 0; i < inferrers.size(); ++ i) {
      EntityNameInferrer inferrer = (EntityNameInferrer) inferrers.get(i);
      String name = inferrer.getEntityName(entityclazz);
      if (name != null) return name;
    }
    return null;
  }

}
