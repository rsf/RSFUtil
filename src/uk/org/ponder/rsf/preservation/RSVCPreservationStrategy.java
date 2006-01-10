/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.preservation;

import org.springframework.beans.factory.BeanNameAware;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.rsf.request.RequestSubmittedValueCache;
import uk.org.ponder.rsf.request.SubmittedValueEntry;
import uk.org.ponder.rsf.state.ExpiredFlowException;
import uk.org.ponder.rsf.state.RSVCApplier;
import uk.org.ponder.rsf.state.TokenStateHolder;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

// This seems itself to be a request-scope bean.
public class RSVCPreservationStrategy implements StatePreservationStrategy,
  BeanNameAware{

  private TokenStateHolder holder;
  private StringList beannames;

  private RSVCApplier rsvcapplier;
  private RequestSubmittedValueCache requestrsvc;
  private String ourbeanname;
    
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
    String token = ourbeanname + tokenid;
    // OK. assume anything ALREADY there for this token needs continued 
    //preserving, with accretion.
    RequestSubmittedValueCache tokenstate = 
      (RequestSubmittedValueCache) holder.getTokenState(ourbeanname + tokenid);
    if (tokenstate == null) {
      tokenstate = new RequestSubmittedValueCache();
    }
    int entries = requestrsvc.entries.size();
    boolean[] done = new boolean[entries];
    // the initial set of "dependent EL" is the set of bean names we were 
    // asked to persist.
    StringList dependentel = beannames.copy();
    for (int i = 0; i < dependentel.size(); ++ i) {
      String dep = dependentel.stringAt(i);
      
      for (int j = 0; j < entries; ++ j) {
        if (done[j]) continue;
        SubmittedValueEntry sve = requestrsvc.entryAt(j);
        if (sve.valuebinding.startsWith(dep)) {
          tokenstate.addEntry(sve);
          Logger.log.info("RSVCPres saving SVE valuebinding " + sve.valuebinding + " newvalue " + sve.newvalue);
          done[j] = true;
          // "funnel outward" dependencies to everything (hopefully just transit
          // beans) that have been used to initialise preserving beans.
          if (sve.isEL) {
            String el = (String) sve.newvalue;
            dependentel.add(el);
          }
        }
      }
    }
    holder.putTokenState(token, tokenstate);
    Logger.log.info("RSVCPres saved " + tokenstate.entries.size() + " entries to token " + token);
  }

  public void restore(WriteableBeanLocator target, String tokenid) {
    String token = ourbeanname + tokenid;
    RequestSubmittedValueCache tokenstate = 
      (RequestSubmittedValueCache) holder.getTokenState(token);
    if (tokenstate == null) {
      throw UniversalRuntimeException.accumulate(new ExpiredFlowException(), 
          "Client requested restoration of expired flow state with ID " + tokenid
          +" which has expired");
    }
    else {
      Logger.log.info("RSVCPres recovered " + tokenstate.entries.size() + " entries from token " + token);
      rsvcapplier.applyValues(tokenstate);
    }
  }

  public void clear(String tokenid) {
    holder.clearTokenState(ourbeanname + tokenid);    
  }

  public void setBeanName(String name) {
    this.ourbeanname = name;
  }

}
