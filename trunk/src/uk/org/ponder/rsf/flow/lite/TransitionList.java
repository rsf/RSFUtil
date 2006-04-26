/*
 * Created on Nov 22, 2005
 */
package uk.org.ponder.rsf.flow.lite;

import uk.org.ponder.arrayutil.TypedListWrapper;

public class TransitionList extends TypedListWrapper {
  public Class getWrappedType() {
    return Transition.class;
  }

  public Transition transitionAt(int i) {
    return (Transition) wrapped.get(i);
  }
  
  public Transition transitionOn(String on) {
    Transition wildcard = null;
    for (int i = 0; i < wrapped.size(); ++ i) {
      Transition trans = transitionAt(i);
      if (trans.on.equals(on)) return trans;
      if (trans.on.equals(Transition.WILDCARD_ON)) wildcard = trans;
    }
    return wildcard;
  }
}
