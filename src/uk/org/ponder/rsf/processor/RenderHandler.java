/*
 * Created on 15-Dec-2005
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.streamutil.write.PrintOutputStream;

/**
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public interface RenderHandler {
  public abstract void handle(PrintOutputStream pos);
}