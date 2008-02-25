/*
 * Created on 14-Feb-2006
 */
package uk.org.ponder.rsf.state.entity.support;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.beanutil.BeanPredicateModel;
import uk.org.ponder.beanutil.PathUtil;
import uk.org.ponder.mapping.DARReshaper;
import uk.org.ponder.mapping.DataAlterationRequest;
import uk.org.ponder.rsf.state.entity.EntityNameInferrer;
import uk.org.ponder.saxalizer.AccessMethod;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;
import uk.org.ponder.saxalizer.support.MethodAnalyser;
import uk.org.ponder.util.Logger;

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
  private BeanPredicateModel addressibleBeanModel;

  public void setBeanModelAlterer(BeanModelAlterer bma) {
    this.bma = bma;
  }
  
  public void setAddressibleBeanModel(BeanPredicateModel addressibleBeanModel) {
    this.addressibleBeanModel = addressibleBeanModel;
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
      String cutback = PathUtil.getToTailPath(toshape.path);
      // cutback may be null! so examine methods of cutback2. This MUST
      // be a concrete object!!
      String cutback2 = PathUtil.getToTailPath(cutback);
      String membername = PathUtil.getTailPath(cutback);
      Object lastentity = bma.getBeanValue(cutback2, rbl, addressibleBeanModel);
      MethodAnalyser ma = mappingcontext.getAnalyser(lastentity.getClass());
      AccessMethod sam = ma.getAccessMethod(membername);
      String entityname = eni.getEntityName(sam.getDeclaredType());
      if (entityname == null) {
        String message = "ID Defunnelling reshaper could not infer entity name for entity of type "
          + sam.getDeclaredType() + " - make sure to supply an EntityNameInferrer for this type";
        Logger.log.warn(message);
        throw new IllegalArgumentException(message);
      }
      Object newentity = null;
      if (toshape.data != null) {
      //    data has already been conformed in type to "oldvalue" and so is at least scalar
        String newentitypath = PathUtil.buildPath(entityname, (String)toshape.data);
        newentity = bma.getBeanValue(newentitypath, rbl, addressibleBeanModel);
      }
      DataAlterationRequest togo = new DataAlterationRequest(cutback, newentity);
      return togo;
    }
    else {
      return toshape;
    }
  }

}
