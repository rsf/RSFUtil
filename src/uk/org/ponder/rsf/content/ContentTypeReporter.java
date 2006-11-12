/*
 * Created on 8 Nov 2006
 */
package uk.org.ponder.rsf.content;

/** Allows a view to report the content type used for its rendering **/

public interface ContentTypeReporter {
  /** Return a String key which can be used to index a {@link ContenTypeInfo} 
   * entry as returned from the {@link ContentTypeInfoFactory}.
   */
  public String getContentType();
}
