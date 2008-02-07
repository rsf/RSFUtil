/*
 * Created on 6 Feb 2008
 */
package uk.org.ponder.rsf.test.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.org.ponder.conversion.LeafObjectParser;
import uk.org.ponder.util.UniversalRuntimeException;

public class DateConverter implements LeafObjectParser {

  private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
  
  public Object copy(Object tocopy) {
    return new Date(((Date)tocopy).getTime());
  }

  public Object parse(String toparse) {
    try {
      return format.parseObject(toparse);
    }
    catch (ParseException e) {
      throw UniversalRuntimeException.accumulate(e, "Error parsing date " + toparse);
    }
  }

  public String render(Object torender) {
    return format.format(torender);
  }

}
