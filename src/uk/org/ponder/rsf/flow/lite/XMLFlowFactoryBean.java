/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.flow.lite;

import uk.org.ponder.rsf.util.XMLFactoryBean;

public class XMLFlowFactoryBean extends XMLFactoryBean {
  public Class getObjectType() {
    return Flow.class;
  }
}
