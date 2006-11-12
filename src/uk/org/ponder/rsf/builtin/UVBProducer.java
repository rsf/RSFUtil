/*
 * Created on 8 Nov 2006
 */
package uk.org.ponder.rsf.builtin;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.conversion.StaticLeafParser;
import uk.org.ponder.errorutil.MessageLocator;
import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UIIDStrategyDecorator;
import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.content.ContentTypeReporter;
import uk.org.ponder.rsf.state.scope.BeanDestroyer;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class UVBProducer implements ViewComponentProducer, ContentTypeReporter {
  public static final String VIEW_ID = "UVBview";
  private BeanDestroyer beanDestroyer;
  private StaticLeafParser leafparser;
  private UVBBean uvbbean;
  private TargettedMessageList tml;
  private MessageLocator messageLocator;

  public void setMessageLocator(MessageLocator messageLocator) {
    this.messageLocator = messageLocator;
  }

  public void setBeanDestroyer(BeanDestroyer beanDestroyer) {
    this.beanDestroyer = beanDestroyer;
  }

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
    for (int i = 0; i < uvbbean.paths.length; ++i) {
      String path = uvbbean.paths[i];
      Object bean = uvbbean.values[i];
      UIOutput out = UIOutput.make(tofill, ":" + path, leafparser.render(bean));
      out.decorators = new DecoratorList(UIIDStrategyDecorator.ID_FULL);
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
    }
    beanDestroyer.destroy();
  }

  public String getContentType() {
    return ContentTypeInfoRegistry.AJAX;
  }

}
