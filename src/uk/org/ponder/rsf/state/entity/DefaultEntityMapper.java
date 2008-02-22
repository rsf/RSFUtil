/*
 * Created on Dec 2, 2006
 */
package uk.org.ponder.rsf.state.entity;

import java.io.Serializable;

import uk.org.ponder.conversion.GeneralLeafParser;
import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;
import uk.org.ponder.util.ObjectFactory;
import uk.org.ponder.util.UniversalRuntimeException;

public class DefaultEntityMapper implements EntityMapper {
  private Class idclazz;
  private ReflectiveCache reflectivecache;
  private GeneralLeafParser parser;
  private Class entityclazz;
  private ObjectFactory objectFactory;
  
  public void setEntityClass(Class entityclazz) {
    this.entityclazz = entityclazz;
  }
  
  public void setIDClass(Class idclazz) {
    this.idclazz = idclazz;
  }
  
  public void setObjectFactory(ObjectFactory objectFactory) {
    this.objectFactory = objectFactory;
  }
  
  public void setMappingContext(SAXalizerMappingContext mappingcontext) {
    this.reflectivecache = mappingcontext.getReflectiveCache();
    this.parser = mappingcontext.saxleafparser;
  }
  public Object instantiate() {
    if (objectFactory == null) {
      return reflectivecache.construct(entityclazz);
    }
    else {
      return objectFactory.getObject();
    }
  }

  public Object parseID(String IDString) {
    try {
      Serializable beanid = (Serializable) parser.parse(idclazz, IDString);
      return beanid;
    }
    catch (Exception e) {
      throw UniversalRuntimeException.accumulate(e, "Error parsing ID " + IDString + 
          " as " + idclazz);
    }
  }

  public Class getEntityClass() {
    return entityclazz;
  }
}
