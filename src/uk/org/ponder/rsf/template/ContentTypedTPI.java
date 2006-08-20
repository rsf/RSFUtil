/*
 * Created on 20 Aug 2006
 */
package uk.org.ponder.rsf.template;

/** A TemplateParseInterceptor that acts only for a nominated set of 
 * content types (as held in the ContentTypeInfo for the current request)
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface ContentTypedTPI extends TemplateParseInterceptor {
  /** Returns an array of content types for which this interceptor is to act.
   */
  public String[] getInterceptedContentTypes();
}
