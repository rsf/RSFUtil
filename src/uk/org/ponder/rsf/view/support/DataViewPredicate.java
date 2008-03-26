/*
 * Created on 26 Mar 2008
 */
package uk.org.ponder.rsf.view.support;

import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParametersPredicate;

public class DataViewPredicate implements ViewParametersPredicate {

  private DataViewCollector dataViewCollector;

  public void setDataViewCollector(DataViewCollector dataViewCollector) {
    this.dataViewCollector = dataViewCollector;
  }
  
  public boolean accept(ViewParameters tocheck) {
    return dataViewCollector.hasView(tocheck.viewID);
  }

}
