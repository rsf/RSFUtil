/*
 * Created on Sep 19, 2005
 */
package uk.org.ponder.rsf.templateresolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import uk.org.ponder.rsf.template.XMLViewTemplate;
import uk.org.ponder.rsf.view.ViewTemplate;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;
import uk.org.ponder.webapputil.ConsumerRequestInfo;

/**
 * A basic template resolver that simply takes the viewID from the
 * ViewParameters object and postpends it onto a base directory.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class BasicTemplateResolver implements TemplateResolver, ApplicationContextAware {
  private String basedir;
  public String suffix = ".xhtml";
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

  // this is a map of viewID onto template file.
  HashMap templates = new HashMap();
  private ApplicationContext context;
  
  private InputStream tryLoadTemplate(String fullpath, boolean lastditch) {
    Resource templateresource = context.getResource(fullpath);
    Logger.log.info("Trying to load view template from path " + fullpath);
    InputStream is;
    try {
      is = templateresource.getInputStream();
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
    ConsumerRequestInfo cri = ConsumerRequestInfo.getConsumerRequestInfo();
    if (cri != null) {
      consumerprefix = cri.ci.consumertype + CONSUMERTYPE_SEPARATOR; 
      viewpath = consumerprefix + viewparams.viewID; 
    }
    ViewTemplate template = (ViewTemplate) templates.get(viewpath);
    if (template == null) {
      String fullpath = basedir + viewpath + suffix;
      InputStream is = tryLoadTemplate(fullpath, consumerprefix == null);
      if (is == null && consumerprefix != null) {
        fullpath = basedir + viewparams.viewID + suffix;
        is = tryLoadTemplate(fullpath, true);
      }
      template = new XMLViewTemplate();
      template.parse(is);
      template.setRelativePath(basedir.substring(1));
      templates.put(viewpath, template);
    }
    return template;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.context = applicationContext;
  }

}
