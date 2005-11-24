/*
 * Created on Nov 24, 2005
 */
package uk.org.ponder.rsf.view;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.util.XMLFactoryBean;

public class XMLComponentFactoryBean extends XMLFactoryBean {

  public Class getObjectType() {
    return UIBranchContainer.class;
  }

}
