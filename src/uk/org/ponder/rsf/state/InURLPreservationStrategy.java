/*
 * Created on Nov 29, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.stringutil.StringList;

/**
 * Stores token state inside the navigation URL. Note that the tokenID used here
 * (as with clientFormTSH) is irrelevant since the state ID is implicitly
 * specified by the client. The state is going to come back anyway, no point
 * trying to specify it!
 * <p>
 * This TSH should be used with some care, since the storage it offers needs to
 * be scoped manually - it is essentially "immortal" unless the TSH client
 * cleans up properly.
 * <p>
 * Currently this TSH only knows how to store state in attributes. If you want
 * to store it in the trunk URL, probably safer to define a new ViewParameters.
 * <p> 
 * BETWEEN a POST REPONSE and the GET RENDERING, this state is indeed stored in
 * the URL. However, after rendering, it is stored in a client field, as indeed
 * any query parameters are that expect to be submitted for a POST.
 * <p> Soon we will implement ClientFormPreservationStrategy, which will keep 
 * the information in a short-lived flow token (a la errortoken) in the gap, and
 * then in a client form, perhaps in some form of compressed bulk.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
// TODO: split this into request-scope and app-scope portions.
public class InURLPreservationStrategy implements StatePreservationStrategy {
  private String prefix = "";
  private Map requestmap;
  private Map iupsspecs = new HashMap();
  private BeanModelAlterer bma;
  private ParameterList outgoingparams;

  public static class IUPSMapping {
    public static final String IUPS_TERMINATOR = "|";
    public String urlkey;
    public String beanpath;
    public IUPSMapping(String spec) {
      int splitpos = spec.indexOf(IUPS_TERMINATOR);
      urlkey = spec.substring(0, splitpos);
      beanpath = spec.substring(splitpos + IUPS_TERMINATOR.length());
    }
  }

  /**
   * Sets the URL prefix to be used in order to locate parameters from the
   * request map.
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public void setBeanModelAlterer (BeanModelAlterer bma) {
    this.bma = bma;
  }

  public void setPreservingBeanSpecs(StringList specs) {
    for (int i = 0; i < specs.size(); ++ i) {
      String spec = specs.stringAt(i);
      IUPSMapping mapping = new IUPSMapping(spec);
      iupsspecs.put(prefix + mapping.urlkey, mapping);
    }
  }
  
  /**
   * Sets the parameter map for the current request. This is a map of String to
   * String[]. It should not matter whether this is a normalized or unnormalized
   * map, since InURLTSH parameters should be entirely orthogonal to RSF
   * parameters.
   */
  public void setRequestMap(Map requestmap) {
    this.requestmap = requestmap;
  }

  public void setOutgoingParams(ParameterList outgoingparams) {
    this.outgoingparams = outgoingparams;
  }

  public void preserve(BeanLocator source, String tokenid) {
    outgoingparams.clear();
    for (Iterator specit = iupsspecs.values().iterator(); specit.hasNext();) {
      IUPSMapping spec = (IUPSMapping) specit.next();
      String converted = (String) bma.getFlattenedValue(spec.beanpath, source,
          String.class);
      outgoingparams.add(new UIParameter(spec.urlkey, converted));
    }
  }

  public void restore(WriteableBeanLocator target, String tokenid) {
    for (Iterator keyit = requestmap.keySet().iterator(); keyit.hasNext();) {
      String key = (String) keyit.next();      
      IUPSMapping spec = (IUPSMapping) iupsspecs.get(key);
      if (spec != null) {
        String[] value = (String[]) requestmap.get(key);
        bma.setBeanValue(spec.beanpath, target, value);
        if (value != null) {
          outgoingparams.add(new UIParameter(key, value[0]));
        }
      }
    }
  }

  public void clear(String tokenid) {
    outgoingparams.clear();
  }


}
