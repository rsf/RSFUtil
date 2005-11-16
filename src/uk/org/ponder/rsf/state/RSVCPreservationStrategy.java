/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.state;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.Logger;

// This seems itself to be a request-scope bean.
public class RSVCPreservationStrategy implements StatePreservationStrategy {

  private TokenStateHolder holder;
  private StringList beannames;

  private RSVCApplier rsvcapplier;
  private RequestSubmittedValueCache requestrsvc;
  
  private static class RSVCTokenState extends TokenState {
    RequestSubmittedValueCache rsvc = new RequestSubmittedValueCache();
  }
  
  public void setPreservingBeans(StringList beannames) {
    this.beannames = beannames;
  }
  
  public void setTokenStateHolder(TokenStateHolder holder) {
    this.holder = holder;
  }
  
  public void setRequestRSVC(RequestSubmittedValueCache requestrsvc) {
    this.requestrsvc = requestrsvc;
  }

  public void setRSVCApplier(RSVCApplier rsvcapplier) {
    this.rsvcapplier = rsvcapplier;
  }
  // This is called at the END of the request cycle, assuming there is an
  // ongoing request.
  public void preserve(BeanLocator source, String tokenid) {
    // OK. assume anything ALREADY there for this token needs continued 
    //preserving, with accretion.
    RSVCTokenState tokenstate = (RSVCTokenState) holder.getTokenState(tokenid);
    if (tokenstate != null) {
      tokenstate = new RSVCTokenState();
      tokenstate.tokenID = tokenid;
    }
    int entries = requestrsvc.entries.size();
    StringList dependentel = beannames.copy();
    for (int i = entries - 1; i >= 0; -- i) {
      SubmittedValueEntry sve = requestrsvc.entryAt(i);
      for (int j = 0; j < dependentel.size(); ++ i) {
        if (sve.valuebinding.startsWith(dependentel.stringAt(j))) {
          tokenstate.rsvc.addEntry(sve);
        }
        // "funnel outward" dependencies to everything (hopefully just transit
        // beans) that have been used to initialise preserving beans.
        if (sve.isEL) {
          String el = BeanUtil.stripEL((String) sve.newvalue);
          dependentel.add(el);
        }
      }
     
    }
    holder.putTokenState(tokenstate);
  }

  public void restore(WriteableBeanLocator target, String tokenid) {
    RSVCTokenState tokenstate = (RSVCTokenState) holder.getTokenState(tokenid);
    if (tokenstate == null) {
      Logger.log.warn("Client requested restoration of expired flow state with ID " + tokenid);
    }
    else {
      rsvcapplier.applyValues(tokenstate.rsvc);
    }
  }

  public void clear(String tokenid) {
    holder.clearTokenState(tokenid);    
  }

}
