/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.util;

import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.ListableBeanFactory;

import uk.org.ponder.saxalizer.XMLProvider;

/** Locates standard RSF beans by name or type. 
 * "Friendly", developer-facing beans would prefer not to force their
 * (usually very standard) RSF dependencies to be specified on every use,
 * causing unnecessary verbiage in configuration files. These "friendly beans",
 * if they have already gone to the length of implementing FactoryBean and the
 * like, may as well go the whole hog and implement ApplicationContextAware
 * and use this class. Admittedly somewhat anti-Springlike, but the users will
 * thank you... 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class StandardBeanFinder {
  public static class SBFEntry {
    String standardname;
    Class clazz;
    SBFEntry(String standardname, Class clazz) {
      this.standardname = standardname;
      this.clazz = clazz;
    }
  }
  
  public static SBFEntry[] entries = {
    new SBFEntry("xmlprovider", XMLProvider.class)
  };
  
  public static Object findBean(Class clazz, ListableBeanFactory lbf) {
    SBFEntry entry = null;
    for (int i = 0; i < entries.length; ++ i) {
      if (entries[i].clazz == clazz) entry = entries[i];
    }
    if (lbf.containsBean(entry.standardname)) {
      return lbf.getBean(entry.standardname, entry.clazz);
    }
    Map allbeans = lbf.getBeansOfType(clazz, false, true);
    Iterator beanit = allbeans.values().iterator();
    return beanit.next();
  }
}
