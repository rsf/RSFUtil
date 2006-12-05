/*
 * Created on Dec 2, 2006
 */
package uk.org.ponder.rsf.state.entity;


public interface EntityHandler {
  public Object get(Object key);
  public void save(Object tosave);
  public boolean delete(Object key);
}