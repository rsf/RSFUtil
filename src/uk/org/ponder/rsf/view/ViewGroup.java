/*
 * Created on 11 Sep 2007
 */
package uk.org.ponder.rsf.view;

/** Specifies a collection of views within the application. Current predicates
 * are by content type or by ViewID. Predicates are comma-separated lists - 
 * a predicate with the first character equal to <code>!</code> represents
 * the negation. For example contentType may be set to <code>HTML, HTML_FRAGMENT</code>
 * or alternatively <code>!AJAX</code>. Similarly viewList may be a list of viewIDs
 * or the negation of a list.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class ViewGroup {
  private String contentTypeSpec;  
  private String viewList;
  
  public String getContentTypeSpec() {
    return contentTypeSpec;
  }

  public void setContentTypeSpec(String contentTypeSpec) {
    this.contentTypeSpec = contentTypeSpec;
  }

  public String getViewList() {
    return viewList;
  }

  public void setViewList(String viewList) {
    this.viewList = viewList;
  }
}
