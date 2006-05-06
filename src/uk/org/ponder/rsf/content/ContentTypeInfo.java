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
  /** The name of the content/request type - e.g. HTML for html, AJAX for
   * (XML) AJAX, XUL for XUL etc - {@link ContentTypeInfoRegistry} for examples.
   */
  public String typename;
  /** The extension, omitting period "." to be used when looking for templates
   * in the filesystem.
   */
  public String fileextension;
  /** The XML declaration to be served at the head of rendered content **/
  public String declaration;
  /** The value for the "ContentType" header served over HTTP for requests
   * of this type.
   */
  public String contentTypeHeader;
  
  
  public ContentTypeInfo() {}
  

  public ContentTypeInfo(String typename, String fileextension, String declaration, String contentTypeHeader) {
    this.typename = typename;
    this.fileextension = fileextension;
    this.declaration = declaration;
    this.contentTypeHeader = contentTypeHeader;
  }
  
  /** Pea proxying method **/
  public ContentTypeInfo get() {
    return this;
  }
}
