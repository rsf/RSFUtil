/*
 * Created on 16 Oct 2006
 */
package uk.org.ponder.rsf.servlet;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import uk.org.ponder.rsf.viewstate.ContextURLProvider;
import uk.org.ponder.util.UniversalRuntimeException;

public class ServletContextCUP implements ContextURLProvider,
    ApplicationContextAware {
  public static final String TEST_RESOURCE = "/WEB-INF/web.xml";
  String contextURL;

  public String getContextBaseURL() {
   return contextURL;
  }

  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    WebApplicationContext wac = (WebApplicationContext) applicationContext;
    contextURL = computeContextName(wac.getServletContext());
  }

  public static String computeContextName(ServletContext context) {
    try {
      URL weburl = context.getResource(TEST_RESOURCE);
      String weburls = weburl.toExternalForm();
      // plus one to include trailing slash
      int backhack = 1 + weburls.length() - TEST_RESOURCE.length();
      int protpos = weburls.indexOf(":");
      if (protpos == -1 ) {
        throw new MalformedURLException(
            "Could not find protocol in URL " + weburls);
      }
      ++ protpos;
      while (true) {
        if (weburls.charAt(protpos) == '/') ++ protpos;
        else break;
      }
      int endhostpos = weburls.indexOf('/', protpos + 3);
      if (endhostpos == -1) {
        throw new MalformedURLException(
            "Could not find host and protocol in URL " + weburls);
      }
      return weburls.substring(endhostpos, backhack);
    }
    catch (Exception e) {
      throw UniversalRuntimeException.accumulate(e,
          "Error computing context name");
    }
  }

}
