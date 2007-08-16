/*
 * Created on 1 Dec 2006
 */
package uk.org.ponder.rsf.state.guards.support;


public class RequiredValueGuard {
  private String code = "value.required";
  
  public void setCode(String code) {
    this.code = code;
  }

  public void setValue(Object obj) {
    if (obj == null || obj.equals("")) {
      throw new IllegalArgumentException(code);
    }
  }

}
