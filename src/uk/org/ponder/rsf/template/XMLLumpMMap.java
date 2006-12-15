/*
 * Created on Aug 17, 2005
 */
package uk.org.ponder.rsf.template;

import java.util.HashMap;
import java.util.Iterator;

import uk.org.ponder.stringutil.CharWrap;

public class XMLLumpMMap {
  private HashMap idtolumps = new HashMap(8);
  
  public String getHeadsDebug() {
    CharWrap message = new CharWrap();
    message.append("Heads: (");
    boolean first = true;
    for (Iterator keys = idtolumps.keySet().iterator(); keys.hasNext();) {
      if (!first) {
        message.append(", ");
      }
      message.append((String)keys.next());
      first = false;
    }
    message.append(")");
    return message.toString();
  }
  
  public XMLLumpList headsForID(String ID) {
    XMLLumpList togo = (XMLLumpList) idtolumps.get(ID);
    return togo;
  }
  
  public XMLLumpList headsForIDEnsure(String ID) {
    XMLLumpList togo = (XMLLumpList) idtolumps.get(ID);
    if (togo == null) {
      togo = new XMLLumpList();
      idtolumps.put(ID, togo);
    }
    return togo;
  }

  public boolean hasID(String ID) {
    return idtolumps.get(ID) != null;
  }
  
  /** Returns an iterator of the String values of the IDs represented here.
   */
  public Iterator iterator() {
    return idtolumps.keySet().iterator();
  }
  
  private int concretes = 0;

  public int numConcretes() {
    return concretes;
  }
  
  public void addLump(String ID, XMLLump lump) {
    XMLLumpList list = headsForIDEnsure(ID);
    list.add(lump);
    if (ID.indexOf(XMLLump.TRANSITION_SEPARATOR) == -1) {
      ++ concretes;
    }
  }
  
  public void addSingle(String key, XMLLump lump) {
    idtolumps.put(key, lump);
  }
  public XMLLump getSingle(String key) {
    return (XMLLump) idtolumps.get(key);
  }
  
  public static final String FINAL_SUFFIX = "*final";
  
  public static final boolean isSpecial(String totest) {
    return totest.endsWith(FINAL_SUFFIX) || totest.indexOf(XMLLump.TRANSITION_SEPARATOR) != -1;
  }
  public void setFinal(String ID, XMLLump lump) {
    idtolumps.put(ID + FINAL_SUFFIX, lump);
  }
  public XMLLump getFinal(String ID) {
    return (XMLLump) idtolumps.get(ID + FINAL_SUFFIX);
  }

  public void aggregate(XMLLumpMMap toaccrete) {
    for (Iterator it = toaccrete.iterator(); it.hasNext();) {
      String key = (String) it.next();
      XMLLumpList list = toaccrete.headsForID(key);
      for (int i = 0; i < list.size(); ++ i) {
        addLump(key, list.lumpAt(i));
      }
    }
  }
  
}
