/*
 * Created on 25 Jul 2006
 */
package uk.org.ponder.rsf.state.guards;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.beanutil.PathUtil;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.mapping.BeanInvalidationModel;
import uk.org.ponder.springutil.errors.SpringErrorConverter;

public class BeanGuardProcessor implements ApplicationContextAware {

  private BeanGuard[] guards;
  private BeanModelAlterer darapplier;
  
  public void setApplicationContext(ApplicationContext applicationContext) {
    String[] guardnames = applicationContext.getBeanNamesForType(BeanGuard.class, false, false);
    guards = new BeanGuard[guardnames.length];
    for (int i = 0; i < guardnames.length; ++ i) {
      guards[i] = (BeanGuard) applicationContext.getBean(guardnames[i]);
    }  
  }
  
  public void setBeanModelAlterer(BeanModelAlterer darapplier) {
    this.darapplier = darapplier;
  }
  
  public void processPostGuards(BeanInvalidationModel bim, TargettedMessageList errors, 
      WriteableBeanLocator rbl) {
    for (int i = 0; i < guards.length; ++ i) {
      BeanGuard guarddef = guards[i];
      String mode = guarddef.getGuardMode();
      String timing = guarddef.getGuardTiming();
      String guardedpath = guarddef.getGuardedPath();
      String guardmethod = guarddef.getGuardMethod();
      String guardEL = guarddef.getGuardEL();
      String guardproperty = guarddef.getGuardProperty();
      if (guardEL != null && guardmethod != null) {
        guardmethod = PathUtil.composePath(guardEL, guardmethod);
      }
      if (mode.equals(BeanGuard.WRITE) && 
          timing == null || timing.equals(BeanGuard.POST)) {
        // for each POST-WRITE guard for an invalidated path, execute it.
        String match = bim.invalidPathMatch(guardedpath); 
        if (match != null) {
          Object guard = guarddef.getGuard();
          if (guardEL != null) {
            guard = darapplier.getBeanValue(guardEL, rbl);
          }
          Object guarded = darapplier.getBeanValue(match, rbl);
          try {
            if (guardmethod != null) {
              darapplier.invokeBeanMethod(guardmethod, rbl);
//              darapplier.setBeanValue(guardprop, guard, guarded, errors);
            }
            else if (guard instanceof Validator) {
              Validator guardv = (Validator) guard;
              Errors temperrors = new BindException(guarded, guardedpath);
              // TODO: We could try to store this excess info somewhere.
              guardv.validate(guarded, temperrors);
              SpringErrorConverter.appendErrors(errors, temperrors);
            }
          }
          catch (Exception e) {
            TargettedMessage message = new TargettedMessage(e.getMessage(), e, match);
            errors.addMessage(message);
          }
        }
      }
    }
  }

  
}
