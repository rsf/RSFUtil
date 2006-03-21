/*
 * Created on Sep 19, 2005
 */
package uk.org.ponder.rsf.templateresolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.rsf.template.XMLViewTemplate;
import uk.org.ponder.rsf.view.ViewTemplate;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.springutil.CachingInputStreamSource;
import uk.org.ponder.util.UniversalRuntimeException;
import uk.org.ponder.webapputil.ConsumerInfo;

/**
 * A basic template resolver that simply takes the viewID from the
 * ViewParameters object and postpends it onto a base directory.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class BasicTemplateResolver implements TemplateResolver,
    ApplicationContextAware {
  private String basedir;
  public String suffix = ".html";
  private int cachesecs;
  public static final String CONSUMERTYPE_SEPARATOR = "-";

  /**
   * Set the base directory in which template files will be sought. This must
   * contain both trailing slash and leading slash.
   */
  public void setBaseDirectory(String basedir) {
    this.basedir = basedir;
  }

  public String getBaseDirectory() {
    return basedir;
  }

  /** Set the lag in seconds at which the filesystem will be polled for changes
   * in the view template. If this value is 0 or the resource is not a 
   * filesystem resource, it will always be reloaded.
   */
  public void setCacheSeconds(int cachesecs) {
    this.cachesecs = cachesecs;
  }

  // this is a map of viewID onto template file.
  private HashMap templates = new HashMap();
  private ApplicationContext context;
  private ConsumerInfo ciproxy;
  private CachingInputStreamSource cachingiis;

  public void setConsumerInfo(ConsumerInfo ci) {
    this.ciproxy = ci;
  }

  private InputStream tryLoadTemplate(String fullpath, boolean lastditch) {
    InputStream is;
    try {
      is = cachingiis.openStream(fullpath);
      if (is == null) {
        throw new FileNotFoundException();
      }
      return is;
    }
    catch (IOException e) {
      if (lastditch) {
        throw UniversalRuntimeException.accumulate(e,
            "Cannot load template file from path " + fullpath);
      }
    }
    return null;
  }

  public ViewTemplate locateTemplate(ViewParameters viewparams) {
    String viewpath = viewparams.viewID;
    String consumerprefix = null;
    ConsumerInfo ci = ciproxy.get();
    if (ci.consumertype != null) {
      consumerprefix = ci.consumertype + CONSUMERTYPE_SEPARATOR;
      viewpath = consumerprefix + viewparams.viewID;
    }
    ViewTemplate template = null;
    
    // this logic to be regularised into a planned list of try/fallback.
    String fullpath = basedir + viewpath + suffix;
    InputStream is = tryLoadTemplate(fullpath, consumerprefix == null);
    if (is == null && consumerprefix != null) {
      fullpath = basedir + viewparams.viewID + suffix;
      is = tryLoadTemplate(fullpath, true);
    }
    
    
    if (is == CachingInputStreamSource.UP_TO_DATE) {
      template = (ViewTemplate) templates.get(viewpath);
    }
    if (template == null) { 
      try {
     // possibly the reason is it had a parse error last time, which may have been corrected
        if (is == CachingInputStreamSource.UP_TO_DATE) {
          is = cachingiis.getNonCachingResolver().openStream(fullpath);
        }
        template = new XMLViewTemplate();
        template.parse(is);
        template.setRelativePath(basedir.substring(1));
        templates.put(viewpath, template);
      }
      catch (Exception e) {
        throw UniversalRuntimeException.accumulate(e,
            "Error parsing view template file " + fullpath);
      }
    }
    return template;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.context = applicationContext;
    cachingiis = new CachingInputStreamSource(applicationContext, cachesecs);
  }

}
