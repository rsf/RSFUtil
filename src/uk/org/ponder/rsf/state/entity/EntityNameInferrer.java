/*
 * Created on 14-Feb-2006
 */
package uk.org.ponder.rsf.state.entity;

/** Returns the "name" (in general managing OTP bean, probably of type 
 * BeanLocator) of an Entity, given its Class.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */ 

public interface EntityNameInferrer {
  /** Return the name of the entity for the supplied class, or <code>null</code>
   * if this inferrer does not hold it.
   */
  public String getEntityName(Class entityclazz);
}
