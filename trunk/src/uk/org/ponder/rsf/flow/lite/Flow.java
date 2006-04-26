/*
 * Created on Nov 21, 2005
 */
package uk.org.ponder.rsf.flow.lite;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Flow {
  public String id;
  public String startstate;
  // A map from State ID to state.
  private Map states = new HashMap();
  
  public void addState(State toadd) {
    states.put(toadd.id, toadd);
  }
  
  public State stateFor(String stateid) {
    return (State) states.get(stateid);
  }
  
  public Collection getStates() {
    return Collections.unmodifiableCollection(states.values());
  }
  
  public void init() {
    for (Iterator it = getStates().iterator(); it.hasNext(); ) {
      State state = (State) it.next();
      state.init();
    }
    FlowUtil.validateFlow(this);
  }
  
}
