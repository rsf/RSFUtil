/*
 * Created on Dec 2, 2006
 */
package uk.org.ponder.rsf.state.entity;

public interface EntityMapper {
  public Object parseID(String IDString);
  public Object instantiate();
  public Class getEntityClass();
}
