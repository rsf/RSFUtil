/*
 * Created on Nov 24, 2005
 */
package uk.org.ponder.rsf.view;

import uk.org.ponder.rsf.util.XMLFactoryBean;

/** A bean which will load an XML-specified components tree into a
 * "proto-container" UIBranchContainer component root.
 * <p>Disused - used only in test.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class XMLComponentFactoryBean extends XMLFactoryBean {
  public XMLComponentFactoryBean() {
    setObjectType(ViewRoot.class);
  }
}
