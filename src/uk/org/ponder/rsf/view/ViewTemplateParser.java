/*
 * Created on 13 Sep 2006
 */
package uk.org.ponder.rsf.view;

import java.io.InputStream;

public interface ViewTemplateParser {
  ViewTemplate parse(InputStream is);
}
