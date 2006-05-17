/*
 * Created on 07-May-2006
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Map;

import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.saxalizer.MethodAnalyser;
import uk.org.ponder.saxalizer.SAXAccessMethod;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;
import uk.org.ponder.stringutil.StringList;

/**
 * Manages inferred information relating to mapping of ViewParameters objects
 * onto URL structure. <b>NEVER</b> place RSF at a shared classloader level or
 * else this implementation will leak. The performance implications of
 * WeakHashMap are too awful to allow any other approach - ViewParameters are
 * parsed <b>extremely</b> frequently during a typical request.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */

public class ViewParamsMappingInfoManager {
  // a map from Class to ViewParamsMapInfo
  private Map viewparamsmap;
  private SAXalizerMappingContext mappingcontext;

  public void setReflectiveCache(ReflectiveCache reflectivecache) {
    viewparamsmap = reflectivecache.getConcurrentMap(1);
  }

  public void setSAXalizerMappingContext(SAXalizerMappingContext mappingcontext) {
    this.mappingcontext = mappingcontext;
  }
  
  public ViewParamsMapInfo getMappingInfo(ViewParameters viewparams) {
    Class clazz = viewparams.getClass();
    ViewParamsMapInfo togo = (ViewParamsMapInfo) viewparamsmap.get(clazz);
    if (togo == null) {
      togo = computeMappingInfo(viewparams);
      viewparamsmap.put(clazz, togo);
    }
    return togo;
  }

  private ViewParamsMapInfo computeMappingInfo(ViewParameters viewparams) {

    StringList attnames = new StringList();
    StringList paths = new StringList();

    String spec = viewparams.getParseSpec();
    String[] fields = spec.split(",");
    for (int i = 0; i < fields.length; ++i) {
      String field = fields[i].trim();
      int colpos = field.indexOf(':');
      if (colpos == -1) {
        // specification without a colon has attribute name agreeing with field name
        attnames.add(field);
        paths.add(field);
      }
      else {
        String path = field.substring(0, colpos);
        String attrname = field.substring(colpos + 1);
        if (path.endsWith(".*")) {
          appendLeaves(viewparams.getClass(), path.substring(0, path.length()-2), 
              attrname, attnames, paths);
        }
        else {
          attnames.add(attrname);
          attnames.add(path);
        }
      }
    }
    ViewParamsMapInfo togo = new ViewParamsMapInfo();
    togo.attrnames = attnames.toStringArray();
    togo.paths = paths.toStringArray();
    return togo;
  }

  private void appendLeaves(Class rootclass, String pathroot, 
      String attrprefix, StringList attnames, StringList paths) {
    String[] components = BeanUtil.splitEL(pathroot);
    
    Class moveclass = rootclass;
    for (int i = 0; i < components.length; ++ i) {
      MethodAnalyser ma = mappingcontext.getAnalyser(moveclass);
      String component = components[i];
      SAXAccessMethod method = ma.getAccessMethod(component);
      if (method == null || !(method.canGet() && method.canSet())) {
        throw new IllegalArgumentException("Unable to find writeable property for path component " + 
            component + " at " + moveclass);
      }
      moveclass = method.getAccessedType();
    }
    MethodAnalyser ma = mappingcontext.getAnalyser(moveclass);
    for (int i = 0; i < ma.allgetters.length; ++ i) {
      SAXAccessMethod method = ma.allgetters[i];
      if (method.canGet() && method.canSet() && mappingcontext.saxleafparser.isLeafType(method.getAccessedType())) {
        paths.add(BeanUtil.composeEL(pathroot, method.tagname));
        attnames.add(attrprefix + method.tagname);
      }
    }
  }

}
