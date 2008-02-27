/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.rvb;

/** A bean mimicking a form of "persistent entity", which on request start
 * has an empty id field, which is only set partway through the action cycle
 * by invocation of the "act()" method, which has the semantics of a persistent
 * save call. 
 */

public class IDHolder {
  public static final String NEW_ID = "new ID";
  public String id;
 
  public void act() {
    id = NEW_ID;
  }
}
