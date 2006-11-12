/*
 * Created on 25 Jul 2006
 */
package uk.org.ponder.rsf.state.guards;

/** "Guards" the read or write access of a particular EL path, by allowing logic
 * supplied in a second bean to execute relative to the access - either before (PRE),
 * after (POST) or "around" (AROUND) it.  
 * 
 * Note that the scheduling of "POST" guards may be delayed until some later time
 * in request processing - do not rely on these for "synchronous" guarding,
 * although their execution *is* guaranteed.
 * 
 * NB - only POST/WRITE guards are currently supported.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class BeanGuard {
  public static final String READ = "READ";
  public static final String WRITE = "WRITE";
  
  public static final String PRE = "PRE";
  public static final String POST = "POST";
  public static final String AROUND = "AROUND";
  
  private String guardedpath;
  private Object guard;
  private String mode;
  private String timing;
  private String guardEL;
  private String guardMethod;
  private String guardProperty;
  
  /** Sets the guard mode - either READ or WRITE **/
  public void setGuardMode(String mode) {
    this.mode = mode;
  }
  
  public String getGuardMode() {
    return mode;
  }
  
  /** Sets the guard timing - either PRE, POST or AROUND.
   * WRITE guards default to POST, READ guards default to PRE.
   * A guard object of type RunnableInvoker will foce mode to AROUND.
   *  **/
  
  public void setGuardTiming(String timing) {
    this.timing = timing;
  }
  
  public String getGuardTiming() {
    return timing;
  }
  
  public void setGuardedPath(String guardedpath) {
    this.guardedpath = guardedpath;
  }
  
  public String getGuardedPath() {
    return guardedpath;
  }
  
  
  public void setGuard(Object guard) {
    this.guard = guard;
  }
  
  public Object getGuard() {
    return guard;
  }
  /** An EL expression from which the base guard can be determined **/
  public void setGuardEL(String guardEL) {
    this.guardEL = guardEL;
  }
  
  public String getGuardEL() {
    return guardEL;
  }
  /** A method to be invoked on a POJO-style validating bean.
   * If "guardEL" or "guard" is not null, 
   * guardMethod consists of just the method name. 
   * If both guardEL and guard are null, this should be a "long path" of which
   * the section to the last component is an EL representing guardEL. 
   * If this value and guardProperty is null, assumed to be a 
   * non-POJO validator (e.g. Spring Validator).
   */
  public void setGuardMethod(String guardMethod) {
    this.guardMethod = guardMethod;
  }

  public String getGuardMethod() {
    return guardMethod;
  }
  /** A property name on which the guarded object will be set onto the
   * guard. If both guardEL and guard are null, this should be a "long path"
   * of which the section to the last component is a EL representing guard EL.
   */
  public void setGuardProperty(String guardProperty) {
    this.guardProperty = guardProperty;
  }
  
  public String getGuardProperty() {
    return guardProperty;
  }
}
