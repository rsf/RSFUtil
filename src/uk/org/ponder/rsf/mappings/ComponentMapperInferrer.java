/*
 * Created on Nov 24, 2005
 */
package uk.org.ponder.rsf.mappings;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.saxalizer.SAXAccessMethodSpec;
import uk.org.ponder.saxalizer.mapping.SAXalizerMapperEntry;
import uk.org.ponder.saxalizer.mapping.SAXalizerMapperInferrer;

public class ComponentMapperInferrer implements SAXalizerMapperInferrer {
  
  private SAXalizerMapperInferrer upchain;
  
  public void setChainedInferrer(SAXalizerMapperInferrer target) {
    upchain = target;
  }
  
  private SAXalizerMapperEntry filterComponentEntry(Class clazz, SAXalizerMapperEntry entry) {
    for (int i = entry.size() - 1; i >= 0; -- i) {
      SAXAccessMethodSpec spec = entry.specAt(i);
      if ("getFullID".equals(spec.getmethodname) ||
          "parent".equals(spec.fieldname))
        entry.remove(i);
      else if ("ID".equals(spec.fieldname)) {
        spec.xmlform = SAXAccessMethodSpec.XML_ATTRIBUTE;
        spec.xmlname = "id";
      }
    }
    return entry;
  }
  
  public SAXalizerMapperEntry inferEntry(Class clazz, SAXalizerMapperEntry entry) {
    SAXalizerMapperEntry togo = upchain.inferEntry(clazz, entry);
    if (UIComponent.class.isAssignableFrom(clazz)) {
      return filterComponentEntry(clazz, togo);
    }
    return togo;
  }


  public boolean isDefaultInferrible(Class clazz) {
    if (UIComponent.class.isAssignableFrom(clazz)) return true;
    else return upchain.isDefaultInferrible(clazz);
  }


  public void setDefaultInferrible(Class clazz) {
    upchain.setDefaultInferrible(clazz);
  }


}
