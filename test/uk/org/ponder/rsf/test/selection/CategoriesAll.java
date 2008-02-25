/*
 * Created on 25 Feb 2008
 */
package uk.org.ponder.rsf.test.selection;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;

public class CategoriesAll implements FactoryBean {

  int size;
  private CategoryFactory factory;
  public void setSize(int size) {
    this.size = size;    
  }
  
  public void setFactory(CategoryFactory factory) {
    this.factory = factory;
  }
  
  public Object getObject() throws Exception {
    List togo = new ArrayList();
    for (int i = 0; i < size; ++ i) {
     togo.add(factory.findCategory(i));
    }
    return togo;
  }

  public Class getObjectType() {
    return List.class;
  }

  public boolean isSingleton() {
    return false;
  }

}
