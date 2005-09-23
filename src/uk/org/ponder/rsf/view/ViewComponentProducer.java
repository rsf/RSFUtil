/*
 * Created on Aug 11, 2005
 */
package uk.org.ponder.rsf.util;

import uk.org.ponder.webapputil.ViewParameters;

public abstract class ViewComponentProducer extends ComponentProducer {
  public abstract String getViewID();
  public abstract void fillDefaultParameters(ViewParameters defaultparameters);
}
