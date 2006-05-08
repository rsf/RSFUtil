/*
 * Created on 12-Jan-2006
 */
package uk.org.ponder.rsf.viewstate;

import uk.org.ponder.conversion.LeafObjectParser;

/** A LeafObjectParser rendition of the ViewParametersParser bean. This parser
 * is registered with the StaticLeafParser in the current mapping context, 
 * so that ViewParameters may be parsed in static contexts (in particular in
 * XML components definition files)
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class ViewParamsLeafParser implements LeafObjectParser {
  
  private ViewParametersParser parser;
  private ViewParamsMapper vpmapper;

  public void setViewParametersParser(ViewParametersParser parser) {
    this.parser = parser;
  }

  public void setViewParamsMapper(ViewParamsMapper vpmapper) {
    this.vpmapper = vpmapper;
  }
  
  public Object parse(String toparse) {
    return ViewParamUtil.parse(parser, toparse);
  }

  public String render(Object torender) {
    return vpmapper.toHTTPRequest((ViewParameters) torender);
  }

  public Object copy(Object tocopy) {
    ViewParameters viewparams = (ViewParameters) tocopy;
    return viewparams.copyBase();
  }

}
