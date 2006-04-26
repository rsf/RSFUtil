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
  private RenderHandler renderhandler;
  private ActionHandler actionhandler;
  public void setActionHandler(RSFActionHandler actionhandler) {
    this.actionhandler = actionhandler;
  }
  public void setRenderHandler(RenderHandler renderhandler) {
    this.renderhandler = renderhandler;
  }
  
  public RenderHandler getRenderHandler() {
    return renderhandler;
  }
  public ActionHandler getActionHandler() {
    return actionhandler;
  }
}
