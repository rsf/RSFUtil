/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.bare;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import uk.org.ponder.arrayutil.ArrayUtil;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.beanutil.support.ConcreteWBL;
import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.rsf.bare.junit.PlainRSFTests;
import uk.org.ponder.rsf.componentprocessor.BindingFixer;
import uk.org.ponder.rsf.componentprocessor.ComponentChildIterator;
import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.rsf.components.UIParameterHolder;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.renderer.ViewRender;
import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;

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
  private SAXalizerMappingContext smc;

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
  
  private void processAndAccrete(UIParameterHolder form, UICommand command) {
    BindingFixer fixer = (BindingFixer) context.getBean("bindingFixer");

    ComponentChildIterator cci = new ComponentChildIterator(form, smc);
    while (cci.hasMoreComponents()) {
      UIComponent component = cci.nextComponent();
      if (component instanceof UIParameterHolder) {
        UIParameterHolder holder = (UIParameterHolder) component;
        fixer.processComponent(holder);
        
        if (!(component instanceof UICommand) || component == command) {
          for (int i = 0; i < holder.parameters.size(); ++i) {
            UIParameter param = holder.parameters.parameterAt(i);
            addParameter(param.name, param.value);
          }          
        }
      }
      if (component instanceof UIBound) {
        UIBound bound = (UIBound) component;
        if (bound.willinput) {
          addParameter(bound.submittingname, bound.acquireValue().toString());
          addParameter(bound.fossilizedbinding.name, bound.fossilizedbinding.value);
        }
      }
    }
  }

  /** Perform a rendering of the (default) test view **/
  public RenderResponse renderView() {
    return renderView(new SimpleViewParameters(TEST_VIEW));
  }
  
  /** Perform a rendering of the specified view 
   * @param torender The ViewParameters specifying the view to be rendered.
   * @return A {@link RenderResponse} record holding a summary of the final
   * context state after action processing.
   * **/
  public RenderResponse renderView(ViewParameters torender) {
    RenderResponse togo = new RenderResponse();
    try {
      WriteableBeanLocator context = rsacbl.getBeanLocator();
      context.set("parsedViewParameters", torender);
      context.locateBean("rootHandlerBean");
      
      togo.requestContext = rsacbl.getDeadBeanLocator();
      ViewRender viewRender = (ViewRender) context.locateBean("viewRender"); 
   
      togo.viewWrapper = new ViewWrapper(viewRender.getView().viewroot, smc);
      BareRootHandlerBean brhb = (BareRootHandlerBean) context.locateBean("rootHandlerBean");
      togo.markup = brhb.getMarkup();
    }
    finally {
      if (!singleshot) {
        rsacbl.endRequest();
      }
    }
    return togo;
    
  }
  
  /**
   * Submit the supplied form, as if via the specified command, returning the
   * "final state" of the request context at cycle end (note that any disposable
   * beans will at this point be in the disposed condition).
   * 
   * @param form A form to be submitted to form this test cycle's input.
   * @param command The form command which it is to appear the form was
   *            submitted by. This may be <code>null</code>
   * @return An {@link ActionResponse} record holding a summary of the final
   * context state after action processing.
   */
  public ActionResponse submitForm(UIForm form, UICommand command) {
    processAndAccrete(form, command);
    
    setRequestType(EarlyRequestParser.ACTION_REQUEST);

    ActionResponse togo = new ActionResponse();

    try {
      rsacbl.getBeanLocator().locateBean("rootHandlerBean");
      togo.requestContext = rsacbl.getDeadBeanLocator();
      togo.ARIResult = (ARIResult) rsacbl.getBeanLocator().locateBean("ARIResultConcrete");
    }
    finally {
      if (!singleshot) {
        ConcreteWBL saved = new ConcreteWBL();
        for (Iterator names = togo.requestContext.iterator(); names.hasNext();) {
          String name = (String) names.next();
          saved.set(name, togo.requestContext.locateBean(name));
        }
        // this will trash all request beans
        rsacbl.endRequest();
        togo.requestContext = saved;
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
    smc = (SAXalizerMappingContext) context.getBean("ELMappingContext");
  }
}
