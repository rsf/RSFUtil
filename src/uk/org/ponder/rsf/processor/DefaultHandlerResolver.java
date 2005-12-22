/*
 * Created on 15-Dec-2005
 */
package uk.org.ponder.rsf.processor;

/**
 * A request-scope bean performing default resolution of ActionHandler and
 * RenderHandler onto RSFActionHandler and RSFRenderHandler respectively.
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class DefaultHandlerResolver {
  private RSFRenderHandlerImpl renderhandler;
  private RSFActionHandler actionhandler;
  public void setRSFActionHandlerImpl(RSFActionHandler actionhandler) {
    this.actionhandler = actionhandler;
  }
  public void setRSFRenderHandler(RSFRenderHandlerImpl renderhandler) {
    this.renderhandler = renderhandler;
  }
  
  public RenderHandler getRenderHandler() {
    return renderhandler;
  }
  public ActionHandler getActionHandler() {
    return actionhandler;
  }
}
