/*
 * Created on Aug 17, 2005
 */
package uk.org.ponder.rsf.html;

import java.util.ArrayList;

public class XMLLumpList extends ArrayList {
  public XMLLump lumpAt(int i) {
    return (XMLLump) get(i);
  }
  public XMLLump top() {
    int size = size();
    return size == 0? null : lumpAt(size - 1);
  }
}
