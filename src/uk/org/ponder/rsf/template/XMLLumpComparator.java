/*
 * Created on 14 Dec 2006
 */
package uk.org.ponder.rsf.template;

import java.util.Comparator;

/** Compares two XMLLumps for their relative order of appareance in a template */

public class XMLLumpComparator implements Comparator {
  private static final Comparator instance = new XMLLumpComparator();
  public static final Comparator instance() {
    return instance;
  }
  public int compare(Object o1, Object o2) {
    return ((XMLLump)o1).lumpindex - ((XMLLump)o2).lumpindex;
  }
}
