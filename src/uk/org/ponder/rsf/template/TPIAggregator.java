/*
 * Created on 20 Aug 2006
 */
package uk.org.ponder.rsf.template;

import java.util.ArrayList;
import java.util.List;

import uk.org.ponder.rsf.content.ContentTypeInfo;

// We don't make this a FactoryBean since it may return null.
public class TPIAggregator {

  private ContentTypeInfo contenttypeinfo;
  private List tpis;

  public void setTemplateParseInterceptors(List tpis) {
    this.tpis = tpis;
  }

  public void setContentTypeInfoProxy(ContentTypeInfo contenttypeinfo) {
    this.contenttypeinfo = contenttypeinfo;
  }

  public List getFilteredTPIs() {
    if (tpis == null) return null;
    
    List togo = new ArrayList();
    ContentTypeInfo requestinfo = contenttypeinfo.get();
    for (int i = 0; i < tpis.size(); ++ i) {
      TemplateParseInterceptor tpi = 
        (TemplateParseInterceptor) tpis.get(i);
      if (!(tpi instanceof NullTemplateParseInterceptor)) {
        if (tpi instanceof ContentTypedTPI) {
          String[] actives = ((ContentTypedTPI)tpi).getInterceptedContentTypes();
          for (int j = 0; j < actives.length; ++ j) {
            if (requestinfo.typename.equals(actives[j])) {
              togo.add(tpi);
              break;
            }
          }
        }
        else { // add non ContentTyped TPIs unconditionally
          togo.add(tpi);
        }
      }
    }
    return togo.size() == 0? null : togo;
  }
}
