/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.flow.lite;

public class ActionState extends State {
  public Action action;
  public TransitionList transitions = new TransitionList();
  public void init() {
    if (action.method == null) {
      action.method = id;
    }
  }
}
