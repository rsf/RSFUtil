/*
 * Created on 8 Nov 2006
 */
package uk.org.ponder.rsf.builtin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ponder.conversion.StaticLeafParser;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UIIDStrategyDecorator;
import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.content.ContentTypeReporter;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class UVBProducer implements ViewComponentProducer, ContentTypeReporter, 
  NavigationCaseReporter {
  public static final String VIEW_ID = "UVBview";
//  private BeanDestroyer beanDestroyer;
  private StaticLeafParser leafparser;
  private UVBBean uvbbean;
  private TargettedMessageList tml;
  private MessageLocator messageLocator;

  public void setLeafParser(StaticLeafParser leafparser) {
    this.leafparser = leafparser;
  }

  public void setMessageLocator(MessageLocator messageLocator) {
    this.messageLocator = messageLocator;
  }
/**
  public void setBeanDestroyer(BeanDestroyer beanDestroyer) {
    this.beanDestroyer = beanDestroyer;
  }
**/
  public String getViewID() {
    return VIEW_ID;
  }

  public void setUVBBean(UVBBean uvbbean) {
    this.uvbbean = uvbbean;
  }

  public void setTargettedMessageList(TargettedMessageList tml) {
    this.tml = tml;
  }

  public void fillComponents(UIContainer tofill, ViewParameters viewparamso,
      ComponentChecker checker) {
    if (uvbbean.paths != null && uvbbean.values != null) {
      for (int i = 0; i < uvbbean.paths.length; ++i) {
        String path = uvbbean.paths[i];
        Object bean = uvbbean.values[i];
        UIOutput out = UIOutput.make(tofill, ":" + i, bean == null? "" : leafparser
            .render(bean));
        out.decorators = new DecoratorList(new UIIDStrategyDecorator(path));
      }
    }
    for (int i = 0; i < tml.size(); ++i) {
      TargettedMessage message = tml.messageAt(i);
      String rendered = messageLocator.getMessage(message.messagecodes,
          message.args);
      UIOutput out = UIOutput.make(tofill, "tml:" + i, rendered);
      Map attrmap = new HashMap();
      attrmap.put("target", message.targetid);
      attrmap.put("severity",
          message.severity == TargettedMessage.SEVERITY_ERROR ? "error"
              : "info");
      out.decorators = new DecoratorList(new UIFreeAttributeDecorator(attrmap));
      out.decorators.add(UIIDStrategyDecorator.ID_FULL);
    }
//    beanDestroyer.destroy();
  }

  public String getContentType() {
    return ContentTypeInfoRegistry.AJAX;
  }

  public List reportNavigationCases() {
    List togo = new ArrayList();
    togo.add(new NavigationCase(null, null, ARIResult.FLOW_ONESTEP));
    return togo;
  }
  
}
