/*
 * Created on Aug 11, 2005
 */
package uk.org.ponder.rsf.view;

import uk.org.ponder.rsf.viewstate.ViewParameters;

public abstract class ViewComponentProducer extends ComponentProducer {
  public abstract String getViewID();
  public abstract void fillDefaultParameters(ViewParameters defaultparameters);
}
