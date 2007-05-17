/*
 * Created on 13 Sep 2006
 */
package uk.org.ponder.rsf.view;

import java.io.InputStream;

/**
 * Parses an RSF (IKAT) view template from a stream representation. Currently
 * the only concrete implementation is
 * {@link uk.org.ponder.rsf.template.XMLViewTemplateParser}, producing
 * {@link uk.org.ponder.rsf.template.XMLViewTemplate} objects.
 */

public interface ViewTemplateParser {
  ViewTemplate parse(InputStream is);
}
