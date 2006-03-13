/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.flow.lite;

import uk.org.ponder.springutil.XMLFactoryBean;

/** A factory bean for producing "FlowLite" Flow definition objects.
 * Principally exists to call the init() method on the product.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class XMLFlowFactoryBean extends XMLFactoryBean {
  public XMLFlowFactoryBean() {
    setObjectType(Flow.class);
  }
  
  public Object getObject() throws Exception {
    Flow togo = (Flow) super.getObject();
    togo.init();
    return togo;
  }
}
