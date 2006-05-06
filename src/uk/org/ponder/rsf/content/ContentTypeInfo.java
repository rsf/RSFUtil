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
   * (XML) AJAX, XUL for XUL etc.
   */
  public String name;
  /** The extension, omitting period "." to be used when looking for templates
   * in the filesystem.
   */
  public String fileextension;
  /** The XML declaration to be served at the head of rendered content **/
  public String declaration;
  /** The value for the "ContentType" header served over HTTP for requests
   * of this type.
   */
  public String contentType;
  
  
  public ContentTypeInfo() {}
  

  public ContentTypeInfo(String name, String fileextension, String declaration, String contentType) {
    this.name = name;
    this.fileextension = fileextension;
    this.declaration = declaration;
    this.contentType = contentType;
  }
  
  /** Pea proxying method **/
  public ContentTypeInfo get() {
    return this;
  }
}
