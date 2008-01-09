/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.bare;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import uk.org.ponder.arrayutil.ArrayUtil;
import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.rsf.componentprocessor.BindingFixer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.rsf.components.UIParameterHolder;
import uk.org.ponder.rsf.request.EarlyRequestParser;

/**
 * A point of control for a "standard" RSF request cycle in a test environment,
 * which allows easy control over the apparently submitted bindings and URL
 * information. These are dispensed from the {@link PlainRSFTests} class which
 * is assumed to be the base class for the test class being written. Each
 * RequestLauncher object corresponds to a single RSAC cycle.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class RequestLauncher implements EarlyRequestParser {

  public static final String TEST_VIEW = "test";

  private Map multipartMap;
  private String pathInfo;
  private Map requestMap;
  private String requestType;
  private RSACBeanLocator rsacbl;
  private ApplicationContext context;

  private boolean singleshot;

  public Map getMultipartMap() {
    return multipartMap;
  }

  public void setMultipartMap(Map multipartMap) {
    this.multipartMap = multipartMap;
  }

  public String getPathInfo() {
    return pathInfo;
  }

  public void setPathInfo(String pathInfo) {
    this.pathInfo = pathInfo;
  }

  public Map getRequestMap() {
    return requestMap;
  }

  public void setRequestMap(Map requestMap) {
    this.requestMap = requestMap;
  }

  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public String getEnvironmentType() {
    return EarlyRequestParser.TEST_ENVIRONMENT;
  }

  public void addParameter(String key, String value) {
    String[] values = (String[]) requestMap.get(key);
    if (values == null) {
      requestMap.put(key, new String[] { value });
    }
    else {
      requestMap.put(key, ArrayUtil.append(values, value));
    }
  }

  private void processAndAccrete(UIParameterHolder holder) {
    BindingFixer fixer = (BindingFixer) context.getBean("bindingFixer");

    fixer.processComponent(holder);

    for (int i = 0; i < holder.parameters.size(); ++i) {
      UIParameter param = holder.parameters.parameterAt(i);
      addParameter(param.name, param.value);
    }
  }

  /**
   * Submit the supplied form, as if via the specified command, returning the
   * "final state" of the request context at cycle end (note that any disposable
   * beans will at this point be in the disposed condition).
   * 
   * @param form A form to be submitted to form this test cycle's input.
   * @param command The form command which it is to appear the form was
   *            submitted by. This may be <code>null</code>
   * @return A BeanLocator holding the final state of the request context.
   * 
   */
  public BeanLocator submitForm(UIForm form, UICommand command) {
    processAndAccrete(form);

    if (command != null) {
      processAndAccrete(command);
    }

    BeanLocator togo = null;
    if (!singleshot) {
      rsacbl.startRequest();
    }
    try {
      rsacbl.getBeanLocator().locateBean("rootHandlerBean");
      togo = rsacbl.getDeadBeanLocator();
    }
    finally {
      if (!singleshot) {
        rsacbl.endRequest();
      }
    }
    return togo;
  }

  public RSACBeanLocator getRSACBeanLocator() {
    return rsacbl;
  }

  public RequestLauncher(ApplicationContext context, RSACBeanLocator rsacbl,
      boolean singleshot) {
    this.rsacbl = rsacbl;
    this.context = context;
    this.singleshot = singleshot;
    multipartMap = new HashMap();
    pathInfo = "/" + TEST_VIEW;
    requestMap = new HashMap();
    requestType = EarlyRequestParser.RENDER_REQUEST;
  }
}
