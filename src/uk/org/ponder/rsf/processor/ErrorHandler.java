/*
 * Created on 14 Aug 2006
 */
package uk.org.ponder.rsf.processor;

public interface ErrorHandler {

  public Object handleError(Object actionresult, Exception exception);

}