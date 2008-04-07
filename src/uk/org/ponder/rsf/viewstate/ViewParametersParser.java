/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Map;

/** Parses the specification of view state (as specified by a URL) out of the
 * slightly processed raw materials of a String "pathinfo" and a Map of request
 * parameters. These may canonically have arised from the methods of the same
 * name in HttpServletRequest - or they may not.
 * <p>
 * The design of this class is an experiment in the new "pea programming".
 * The fields represent "input parameters" and the method body represents the
 * "result". Since in order to be reused, instances of this class need to be
 * cloned, these input fields need to be readable by the cloner which we would
 * not like to be restricted to be slow.
 * <p> Oh well, never mind - we went back with the MethodInvokingBeanFactory
 * solution again in the end.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface ViewParametersParser {
  public ViewParameters parse(String[] pathinfo, Map requestmap);
}