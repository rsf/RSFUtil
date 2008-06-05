/*
 * Created on 5 Jun 2008
 */
package uk.org.ponder.rsf.processor;

/** A special class of exception representing that the "exterior environment"
 * should not attempt cleanup in the case of a passing exception but to throw
 * it in in the raw.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class ForcibleException extends RuntimeException {

}
