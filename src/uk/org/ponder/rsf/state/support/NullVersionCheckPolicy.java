/*
 * Created on Nov 12, 2005
 */
package uk.org.ponder.rsf.state.support;

import uk.org.ponder.rsf.request.SubmittedValueEntry;
import uk.org.ponder.rsf.state.VersionCheckPolicy;

public class NullVersionCheckPolicy implements VersionCheckPolicy {

  public void checkOldVersion(SubmittedValueEntry sve) {
  }

}
