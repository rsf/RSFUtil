/*
 * Created on Dec 2, 2006
 */
package uk.org.ponder.rsf.state.entity.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.beanutil.entity.NewEntityEntry;
import uk.org.ponder.beanutil.entity.NewEntityReceiver;
import uk.org.ponder.rsf.state.entity.EntityHandler;
import uk.org.ponder.rsf.state.entity.EntityMapper;
import uk.org.ponder.rsf.state.entity.ObstinateEntityBeanLocator;
import uk.org.ponder.rsf.state.entity.UpdatableEntityHandler;
import uk.org.ponder.util.UniversalRuntimeException;

public class BasicObstinateEBL implements ObstinateEntityBeanLocator {
  private Map delivered = new HashMap();
  private EntityHandler entityHandler;
  private EntityMapper entityMapper;
  private NewEntityReceiver entityreceiver;

  public void setEntityMapper(EntityMapper entityMapper) {
    this.entityMapper = entityMapper;
  }

  public void setEntityHandler(EntityHandler entityHandler) {
    this.entityHandler = entityHandler;
  }

  public void setNewEntityReceiver(NewEntityReceiver entityreceiver) {
    this.entityreceiver = entityreceiver;
  }

  public boolean remove(String beanname) {
    Object beanid = entityMapper.parseID(beanname);
    try {
      return entityHandler.delete(beanid);
    }
    finally {
      delivered.remove(beanname);
    }
  }

  private void registerNewEntity(String id, Object newent) {
    if (entityreceiver != null) {
      entityreceiver.receiveNewEntity(new NewEntityEntry(newent, id, this));
    }
    delivered.put(id, newent);
  }

  public void set(String beanname, Object toset) {
    if (beanname.startsWith(BeanUtil.NEW_ENTITY_PREFIX)) {
      registerNewEntity(beanname, toset);
    }
    else {
      if (entityHandler instanceof UpdatableEntityHandler) {
        try {
          ((UpdatableEntityHandler) entityHandler).update(toset);
        }
        catch (Exception e) {
          throw UniversalRuntimeException.accumulate(e,
              "Error updating bean with id " + beanname + " of "
                  + entityMapper.getEntityClass());
        }
      }
      delivered.put(beanname, toset);
    }
  }

  public void endCycle() {
    // This should really be part of AlterationWrapper
  }

  public Object locateBean(String path) {
    Object togo = null;
    // Assume this is even FASTER than Hibernate 1st-level cache :P
    togo = delivered.get(path);
    if (togo == null) {
      if (path.startsWith(BeanUtil.NEW_ENTITY_PREFIX)) {
        togo = entityMapper.instantiate();
        registerNewEntity(path, togo);
      }
      else {
        try {
          Object beanid = entityMapper.parseID(path);
          togo = entityHandler.get(beanid);
        }
        catch (Exception e) {
          throw UniversalRuntimeException.accumulate(e,
              "Error getting bean with ID " + path + " of "
                  + entityMapper.getEntityClass());
        }
        if (togo == null) {
          throw new NoSuchBeanDefinitionException(path, "No entity of "
              + entityMapper.getEntityClass() + " with ID " + path
              + " could be found");
        }
      }
      delivered.put(path, togo);

    }
    return togo;
  }

  /** Returns an iterator of PREVIOUSLY DELIVERED beans */
  public Iterator iterator() {
    return delivered.keySet().iterator();
  }

}
