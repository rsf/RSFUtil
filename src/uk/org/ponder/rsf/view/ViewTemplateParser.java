/*
 * Created on 13 Sep 2006
 */
package uk.org.ponder.rsf.view;

import java.io.InputStream;

/** Parses an RSF (IKAT) view template from a stream representation. Currently
 * the only concrete implementation is {@link XMLViewTemplateParser},
 * producing {@link XMLViewTemplate} objects. */

public interface ViewTemplateParser {
  ViewTemplate parse(InputStream is);
}
