/*
 * Created on Dec 14, 2004
 */
package uk.org.ponder.rsf.viewstate;

import uk.org.ponder.util.FieldHash;

/**
 * A blank ViewParameters object for cases where a concrete dummy implementation
 * is required.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class BlankViewParameters extends ViewParameters {

  public FieldHash getFieldHash() {
    return new FieldHash(BlankViewParameters.class);
  }
  public void clearActionState() {
    // TODO Auto-generated method stub
  }

  public void clearParams() {
    // TODO Auto-generated method stub
  }
  public void parsePathInfo(String pathinfo) {
    // TODO Auto-generated method stub
    
  }
  public String toPathInfo() {
    // TODO Auto-generated method stub
    return null;
  }

}