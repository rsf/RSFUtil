/*
 * Created on 20 Aug 2007
 */
package uk.org.ponder.rsf.renderer.html;

import java.util.Map;

import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.template.ContentTypedTPI;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.util.SplitID;

/** In order to main a consistent designer-facing semantic, HTML form tags
 * must be written without colon, since they do not cause branching. However,
 * the interpretation to the template parser of the colon tag is to form
 * a containment scope for resolution, which also agrees with the component
 * tree since UIForm is a container. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class FormRelabellingTPI implements ContentTypedTPI {

  public String[] getInterceptedContentTypes() {
    return new String[] { ContentTypeInfoRegistry.HTML,
        ContentTypeInfoRegistry.HTML_FRAGMENT };
  }

  public void adjustAttributes(String tag, Map attributes) {
    String id = (String) attributes.get(XMLLump.ID_ATTRIBUTE);
    if (tag.equals("form") && id != null) {
      if (!SplitID.isSplit(id)) {
        attributes.put(XMLLump.ID_ATTRIBUTE, id + SplitID.SEPARATOR);
      }
    }
  }
}
