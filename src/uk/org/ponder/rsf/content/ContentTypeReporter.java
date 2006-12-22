/*
 * Created on 8 Nov 2006
 */
package uk.org.ponder.rsf.content;

/** Allows a view to report the content type used for its rendering **/

public interface ContentTypeReporter {
  /** Return a String key which can be used to index a {@link ContentTypeInfo} 
   * entry as returned from the {@link ContentTypeInfoFactory}. Sample type 
   * keys may be found in {@link ContentTypeInfoRegistry}.
   */
  public String getContentType();
}
