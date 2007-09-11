/*
 * Created on 11 Sep 2007
 */
package uk.org.ponder.rsf.view.support;

import uk.org.ponder.rsf.content.ContentTypeResolver;
import uk.org.ponder.rsf.view.ViewGroup;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** The service for matching a ViewGroup predicate against a particular
 * ViewParameters.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class ViewGroupResolver {
  private ContentTypeResolver contentTypeResolver;
  
  public void setContentTypeResolver(ContentTypeResolver contentTypeResolver) {
    this.contentTypeResolver = contentTypeResolver;
  }

  public boolean isMatch(ViewGroup group, ViewParameters viewparams) {
    ParsedPredicate parsedcont = ParsedPredicate.parse(group.getContentTypeSpec());
    ParsedPredicate parsedview = ParsedPredicate.parse(group.getViewList());
    
    if (!(applyFilter(parsedview, viewparams.viewID))) return false;
  
    String contenttype = contentTypeResolver.resolveContentType(viewparams);
    if (!(applyFilter(parsedcont, contenttype))) return false;
    
    return true;
  }

  private boolean applyFilter(ParsedPredicate predicate, String match) {
    if (predicate.elements != null && predicate.elements.length > 0) {
      for (int i = 0; i < predicate.elements.length; ++ i) {
        if (!(predicate.elements[i].equals(match) ^ predicate.positive)) 
          return true;
      }
      return false;
    }
    return true;
  }
  
}
