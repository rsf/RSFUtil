/*
 * Created on May 5, 2006
 */
package uk.org.ponder.rsf.content;

/** A "pea" holding information about a particular content type that may
 * be used during a request cycle.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class ContentTypeInfo {
  /** A constant representing the standard "full" ID allocation strategy, 
   * derived from the full ID of a component. Appropriate for HTML and similar
   * dialects.
   */
  public static final String ID_FULL = "full";
  /** Suppress generation of the "id" attribute. Appropriate for some types
   * of pure XML response.
   */
  public static final String ID_NONE = "none";
  /** Pass through the rsf:id attribute unchanged. Appropriate for the
   * (so far untried) strategy of using RSF to render RSF templates.
   */ 
  public static final String ID_RSF = "RSF";
  
  /** The name of the content/request type - e.g. HTML for html, AJAX for
   * (XML) AJAX, XUL for XUL etc - {@link ContentTypeInfoRegistry} for examples.
   */
  public String typename;
  /** The extension, omitting period ".", to be used when looking for templates
   * in the filesystem.
   */
  public String fileextension;
  /** The XML declaration to be served at the head of rendered content **/
  public String declaration;
  /** The value for the "ContentType" header served over HTTP for requests
   * of this type.
   */
  public String contentTypeHeader;
  
  /** The strategy to be used for generating the values of "ID" elements - either
   * ID_FULL, ID_NONE, or ID_RSF
   */
  public String IDStrategy;
  
  /** Whether the content type supports "direct" Action -> Render redirects.
   */
  public boolean directRedirects = true;
  
  public ContentTypeInfo() {}

  public ContentTypeInfo(String typename, String fileextension, String declaration, String contentTypeHeader) {
    this.typename = typename;
    this.fileextension = fileextension;
    this.declaration = declaration;
    this.contentTypeHeader = contentTypeHeader;
    this.IDStrategy = ID_NONE;
  }
  
  public ContentTypeInfo(String typename, String fileextension, String declaration, String contentTypeHeader, 
      String IDStrategy, boolean directRedirects) {
    this.typename = typename;
    this.fileextension = fileextension;
    this.declaration = declaration;
    this.contentTypeHeader = contentTypeHeader;
    this.IDStrategy = IDStrategy;
    this.directRedirects = directRedirects;
  }
  
  /** Pea proxying method **/
  public ContentTypeInfo get() {
    return this;
  }
}
