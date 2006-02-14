/*
 * Created on Nov 12, 2005
 */
package uk.org.ponder.rsf.state;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.mapping.DARList;
import uk.org.ponder.mapping.DARReshaper;
import uk.org.ponder.mapping.DataAlterationRequest;
import uk.org.ponder.rsf.request.RequestSubmittedValueCache;
import uk.org.ponder.rsf.request.SubmittedValueEntry;
import uk.org.ponder.rsf.uitype.UIType;
import uk.org.ponder.rsf.uitype.UITypes;
import uk.org.ponder.util.Logger;

public class RSVCApplier {
  private VersionCheckPolicy versioncheckpolicy;
  private BeanModelAlterer darapplier;
  private BeanLocator rbl;

  public void setBeanModelAlterer(BeanModelAlterer darapplier) {
    this.darapplier = darapplier;
  }

  public void setVersionCheckPolicy(VersionCheckPolicy versioncheckpolicy) {
    this.versioncheckpolicy = versioncheckpolicy;
  }

  // this will be used to locate request-scope beans.

  public void setRootBeanLocator(BeanLocator rbl) {
    this.rbl = rbl;
  }

  public void applyValues(RequestSubmittedValueCache rsvc) {
    // TODO: There is scope for a lot of policy here - mainly version checking.
    // Define a VersionCheckPolicy that will compare oldvalue to the model
    // value.
    DARList toapply = new DARList();
    for (int i = 0; i < rsvc.entries.size(); ++i) {
      SubmittedValueEntry sve = rsvc.entryAt(i);
      if (sve.componentid != null) { // it is a component binding.
        if (sve.oldvalue != null && sve.valuebinding != null) {
          versioncheckpolicy.checkOldVersion(sve); // will blow on error

          UIType type = UITypes.forObject(sve.oldvalue);
          try {
            // TODO: why did we need to hack the value flat like this - should
            // have been taken care of by FixupNewValue in PostDecoder
            Object flattened = darapplier.getFlattenedValue("", sve.newvalue,
                sve.oldvalue.getClass(), null);
            // cull the change from touching the model.
            if (type.valueUnchanged(sve.oldvalue, flattened))
              continue;
          }
          catch (Exception e) {
            Logger.log.warn("Error flattening value" + sve.newvalue + " into "
                + sve.oldvalue.getClass(), e);
          }
        }
      }
      DataAlterationRequest dar = null;
      if (sve.isdeletion) {
        dar = new DataAlterationRequest(sve.valuebinding, sve.newvalue,
            DataAlterationRequest.DELETE);
      }
      else {
        Object newvalue = sve.newvalue;
        if (sve.isEL) {
          newvalue = darapplier.getBeanValue((String) sve.newvalue, rbl);
        }
        dar = new DataAlterationRequest(sve.valuebinding, newvalue);
      }
      if (sve.reshaperbinding != null) {
        DARReshaper reshaper = (DARReshaper) rbl.locateBean(sve.reshaperbinding);
        dar = reshaper.reshapeDAR(dar);
      }
      toapply.add(dar);
    }
    TargettedMessageList errors = ThreadErrorState.getErrorState().errors;
    darapplier.applyAlterations(rbl, toapply, errors);
  }

  public Object invokeAction(String actionbinding) {
    return darapplier.invokeBeanMethod(actionbinding, rbl);
  }
}
