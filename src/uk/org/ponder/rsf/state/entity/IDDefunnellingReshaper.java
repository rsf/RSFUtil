/*
 * Created on 14-Feb-2006
 */
package uk.org.ponder.rsf.state.entity;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.mapping.DARReshaper;
import uk.org.ponder.mapping.DataAlterationRequest;
import uk.org.ponder.saxalizer.MethodAnalyser;
import uk.org.ponder.saxalizer.SAXAccessMethod;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;

/** A "DataAlterationRequest" reshaper that does the work of reprocessing
 * a DAR after decoding from request, from one that simply assigns object
 * IDs to one that assigns entire objects. In this form, it is the natural
 * inverse to the map of Entities onto their IDs, as performed by the request
 * bean #{fieldGetter.id}.
 * <p>For example, this will reshape a DAR applying an ADD of the value "id2" to
 * the path #{Recipe.1.category.id} into an ADD of the object with EL #{Category.id2}
 * to the path #{Recipe.1.category}.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class IDDefunnellingReshaper implements DARReshaper {

  private BeanLocator rbl;
  private BeanModelAlterer bma;
  private EntityNameInferrer eni;
  private SAXalizerMappingContext mappingcontext;

  public void setBeanModelAlterer(BeanModelAlterer bma) {
    this.bma = bma;
  }
  
  public void setRequestBeanLocator(BeanLocator rbl) {
    this.rbl = rbl;
  }
  
  public void setEntityNameInferrer(EntityNameInferrer eni) {
    this.eni = eni;
  }
  
  public void setMappingContext(SAXalizerMappingContext mappingcontext) {
    this.mappingcontext = mappingcontext;
  }
  
  public DataAlterationRequest reshapeDAR(DataAlterationRequest toshape) {
    if (toshape.type.equals(DataAlterationRequest.ADD)) {
      String cutback = BeanUtil.getContainingPath(toshape.path);
      // cutback may be null! so examine methods of cutback2. This MUST
      // be a concrete object!!
      String cutback2 = BeanUtil.getContainingPath(cutback);
      String membername = BeanUtil.getTail(cutback);
      Object lastentity = bma.getBeanValue(cutback2, rbl);
      MethodAnalyser ma = mappingcontext.getAnalyser(lastentity.getClass());
      SAXAccessMethod sam = ma.getAccessMethod(membername);
      String entityname = eni.getEntityName(sam.getDeclaredType());
      Object newentity = null;
      if (toshape.data != null) {
      //    data has already been conformed in type to "oldvalue" and so is at least scalar
        String newentitypath = BeanUtil.composeEL(entityname, (String)toshape.data);
        newentity = bma.getBeanValue(newentitypath, rbl);
      }
      DataAlterationRequest togo = new DataAlterationRequest(cutback, newentity);
      return togo;
    }
    else {
      return toshape;
    }
  }

}
