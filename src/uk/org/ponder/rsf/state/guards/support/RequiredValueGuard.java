/*
 * Created on 1 Dec 2006
 */
package uk.org.ponder.rsf.state.guards.support;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RequiredValueGuard implements Validator {
  private String code = "Value is required";
  
  public void setCode(String code) {
    this.code = code;
  }

  public boolean supports(Class clazz) {
    return true;
  }

  public void validate(Object obj, Errors errors) {
    if (obj == null || obj.equals("")) {
      errors.reject(code);
    }
  }

}
