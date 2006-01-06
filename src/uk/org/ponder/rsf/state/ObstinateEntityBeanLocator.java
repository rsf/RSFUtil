/*
 * Created on 06-Jan-2006
 */
package uk.org.ponder.rsf.state;

import uk.org.ponder.beanutil.IterableBeanLocator;
import uk.org.ponder.beanutil.WriteableBeanLocator;

/** A request-scope container managing a set of entities, probably managed
 * by some form of ORM solution. Note that the semantics of ALL the inherited
 * methods are somewhat specific.
 * <p> get(String ID) - The ID is interpreted as the rendered form of an underlying
 * database ID, UNLESS it begins with the special prefix BeanUtil.NEW_ENTITY_PREFIX.
 * If it is not a new entity, the actual ID type will be parsed, and the existing 
 * persistent entity of the type handled by this locator will be returned. If the
 * ID *does* contain the new entity prefix, a new, default-constructed 
 * entity of the correct type will be returned. Any newly-constructed entities of this sort will
 * be made persistent by the final actions of the AlterationWrapper active for
 * this request, which cooperates with all OEBL's currently serving, to harvest
 * their new entities. As object IDs are assigned by this process, 
 * the EntityIDAssignmentListeners registered with this locator will be informed,
 * to allow correct propagation of object references out of the current context.
 * 
 * <p> set(String ID, Object newobj) The object is assumed to be somehow "detached"
 * or "non-persistent". The user takes responsibility for not supplying an object
 * in an incorrect persistence state. If the ID begins with the NEW_ENTITY_PREFIX,
 * the new object had BETTER not have any persistent role, since it will be 
 * interpreted as a currently "transient" entity (in Hibernate-speak) that will be
 * subject to being saved by the same semantics as entities auto-created by get().
 * If the ID is not a "new" one, the supplied object will assumed to be "detached"
 * and will be freshened to the currently active session.
 * 
 * <p> iterator() This is the source of the "obstinacy" of the BeanLocator. The
 * iterator() method will ONLY dispense objects that have previously been
 * referenced through set() and get(), and NOT the entire collection (table) 
 * backing the locator. This method is principally expected to be used by
 * a StatePreservationStrategy at the terminus of a request. For servicing 
 * "genuine" user-level queries, use a dedicated query "DAO".
 * 
 * <p> remove(String ID) The object referenced by the supplied (persistent) ID
 * will be "unlinked". I.e., it will be removed both from persistent state,
 * and from the "delivered" list of objects constituting the iterable collection.
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public interface ObstinateEntityBeanLocator extends WriteableBeanLocator, 
    IterableBeanLocator {
  /** Adds a listener to be notified of the persistent IDs allocated to
   * objects by an end-request commit notified by the AlterationWrapper.
   * This class is expected to have a private relationship with the current
   * AlterationWrapper established through registerPreCommitAction();
   * @param eialistener The listener to be added.
   */
  public void addEntityIDListener(EntityIDAssignmentListener eialistener);
}
