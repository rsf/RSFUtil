/*
 * Created on 7 Dec 2007
 */
package uk.org.ponder.rsf.test.rvb;

import org.springframework.beans.factory.FactoryBean;

import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.request.RequestSubmittedValueCache;
import uk.org.ponder.rsf.request.SubmittedValueEntry;
import uk.org.ponder.rsf.util.RSFUtil;

public class RVBRSVCFactory implements FactoryBean {

  public Object getObject() throws Exception {
    RequestSubmittedValueCache rsvc = new RequestSubmittedValueCache();
    UIForm dummyform = new UIForm();
    RSFUtil.addResultingViewBinding(dummyform, "entity.ID", "idholder.id");
    UIELBinding binding = (UIELBinding) dummyform.parameters.get(0);
    SubmittedValueEntry sve = new SubmittedValueEntry();
    
    // TODO Auto-generated method stub
    return null;
  }

  public Class getObjectType() {
    return RequestSubmittedValueCache.class;
  }

  public boolean isSingleton() {
    return true;
  }

}
