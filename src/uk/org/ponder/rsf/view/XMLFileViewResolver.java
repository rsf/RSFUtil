/*
 * Created on Nov 25, 2005
 */
package uk.org.ponder.rsf.view;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.expander.TemplateExpander;
import uk.org.ponder.saxalizer.XMLProvider;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/** A view resolver which will return a component producer specified in
 * a static XML file. Will attempt to look up the supplied viewID, after
 * prefixing and postfixing, as a Spring resource.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class XMLFileViewResolver implements ViewResolver, ApplicationContextAware {
  public static final int NO_CACHE = -1;
  public static final String DEFAULT_EXTENSION = ".xml";
  private StringList viewnames;
  private String basepath;
  private ApplicationContext context;
  private Map views = new HashMap();
  private String extension = DEFAULT_EXTENSION;
  private List defaultviews = null;
  private TemplateExpander templateexpander;
  private XMLProvider xmlprovider;
  private int cachesecs = NO_CACHE;

  /**
   * Sets the default extension (including period) that will be suffixed to a
   * viewId in order to generate the resource path which will be loaded.
   */
  public void setDefaultExtension(String extension) {
    this.extension = extension;
  }

  /**
   * Sets a collection of component producers which will execute for every view.
   */
  public void setDefaultViews(List defaults) {
    this.defaultviews = defaults;
  }

  /**
   * Set the base directory in which component files will be sought. This must
   * contain both trailing slash and leading slash. This may designate any
   * Spring-resolvable resource path (will be handed to application context).
   */

  public void setBasePath(String basepath) {
    this.basepath = basepath;
  }

  /**
   * Sets a list of view names which this resolver will attempt to resolve. If
   * this list is not set, resolver will attempt to resolve any view. If the
   * list is set and a request is received for a view not listed in it,
   * getProducers() will return null rather than throwing an exception, enabling
   * chaining of resolvers.
   */
  public void setViewNames(StringList viewnames) {
    this.viewnames = viewnames;
  }

  public void setXMLProvider(XMLProvider xmlprovider) {
    this.xmlprovider = xmlprovider;
  }

  public void setTemplateExpander(TemplateExpander templateexpander) {
    this.templateexpander = templateexpander;
  }
  
  public void setCacheSeconds(int cachesecs) {
    this.cachesecs = cachesecs;
  }

  private XMLViewComponentProducer tryLoadProducer(String fullpath,
      String viewId) {
    XMLViewComponentProducer togo = null;
    if (Logger.log.isInfoEnabled()) {
      Logger.log.info("Loading view template from " + fullpath);
    }
    Resource res = context.getResource(fullpath);
    if (res.exists()) {
      try {
        InputStream is = res.getInputStream();

        UIBranchContainer container = (UIBranchContainer) xmlprovider.readXML(
            UIBranchContainer.class, is);
        togo = new XMLViewComponentProducer();
        togo.setTemplateExpander(templateexpander);
        togo.setViewID(viewId);
        togo.setTemplateContainer(container);
        views.put(viewId, togo);
      }
      catch (Exception e) {
        throw UniversalRuntimeException.accumulate(e,
            "Error loading view components from view with path " + fullpath);
      }
    }
    return togo;
  }

  public List getProducers(String viewId) {
    XMLViewComponentProducer producer = (XMLViewComponentProducer) views
        .get(viewId);
    if (producer == null || cachesecs >= 0) {
      if (viewnames == null || viewnames.contains(viewId)) {
        String fullpath = basepath + viewId + extension;
        if (isCacheStale(fullpath, producer, cachesecs)) {
          producer = tryLoadProducer(fullpath, viewId);
        }
      }
    }
    if (producer != null) {
      ArrayList togo = new ArrayList();
      if (defaultviews != null) {
        togo.addAll(defaultviews);
      }
      togo.add(producer);
      return togo;
    }
    return null;
  }

  private static boolean isCacheStale(String fullpath, XMLViewComponentProducer producer, int cachesecs) {
    if (producer == null) return true;
    long now = System.currentTimeMillis();
    boolean isstale = false;
    
    if (now > producer.lastchecked + cachesecs * 1000) {
      File f = new File(fullpath);
      long modtime = f.lastModified();
      if (modtime > producer.modtime) {
        producer.modtime = modtime;
        isstale = true;
      }
    }
    producer.lastchecked = now;
    return isstale;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.context = applicationContext;
  }
}
