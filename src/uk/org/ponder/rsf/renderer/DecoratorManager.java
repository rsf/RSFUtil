/*
 * Created on May 17, 2006
 */
package uk.org.ponder.rsf.renderer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIDecorator;
import uk.org.ponder.rsf.content.ContentTypeInfo;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.Logger;

public class DecoratorManager implements ApplicationContextAware {
  // decorators index by decorator class - has subindex by content type.
  // if no match on exact type, go first for "" and then finally for any found.
  private Map byClass = new HashMap();

  private ContentTypeInfo contenttypeinfo;
  
  public void setContentTypeInfo(ContentTypeInfo contenttypeinfo) {
    this.contenttypeinfo = contenttypeinfo;
  }
  
  public void decorate(DecoratorList decorators, String tagname, Map attrmap) {
    if (decorators == null) return;
    String requestcontent = contenttypeinfo.get().typename;
    for (int i = 0; i < decorators.size(); ++ i) {
      UIDecorator dec = decorators.decoratorAt(i);
      DecoratorRenderer renderer = lookupRenderer(dec, requestcontent);
      if (renderer == null) {
        Logger.log.warn("Unable to find renderer for decorator " + dec.getClass() 
            + " and content type " + requestcontent);
      }
      else {
        renderer.modifyAttributes(dec, tagname, attrmap);
      }
    }
  }
  
  private DecoratorRenderer lookupRenderer(UIDecorator dec, String requestcontent) {
    Map typemap = (Map) byClass.get(dec.getClass());
    if (typemap == null || typemap.isEmpty()) {
      return null;
    }
    DecoratorRenderer togo = (DecoratorRenderer) typemap.get(requestcontent);
    if (togo == null) {
      togo = (DecoratorRenderer) typemap.get("");
    }
    if (togo == null) {
      togo = (DecoratorRenderer) typemap.values().iterator().next();
    }
    return togo;
  }

  
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    String decorators[] = applicationContext.getBeanNamesForType(DecoratorRenderer.class, false, false);
    for (int i = 0; i < decorators.length; ++ i) {
      DecoratorRenderer renderer = (DecoratorRenderer) applicationContext.getBean(decorators[i]);
      
      Class declass = renderer.getRenderedType();
      Map typemap = (Map) byClass.get(declass);
      if (typemap == null) {
        typemap = new HashMap();
        byClass.put(declass, typemap);
      }
      
      StringList contents = StringList.fromString(renderer.getContentTypes());
      
      for (int j = 0; j < contents.size(); ++ j) {
        String content = contents.stringAt(j).trim();
        if (content.length() > 0) {
          typemap.put(content, renderer);
        }
      }
      
      if (contents.size() == 0) {
        typemap.put("", renderer);
      }
    
    }
    
  }
}
