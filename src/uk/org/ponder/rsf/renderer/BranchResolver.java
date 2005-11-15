/*
 * Created on Nov 14, 2005
 */
package uk.org.ponder.rsf.renderer;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.template.XMLLumpList;
import uk.org.ponder.rsf.template.XMLLumpMMap;
import uk.org.ponder.rsf.util.SplitID;
import uk.org.ponder.util.Logger;

/** Performs a first "light" pass of the template and component tree to
 * resolve 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class BranchResolver {
  private HashMap branchmap = new HashMap();

  private XMLLumpMMap globalmap;

  private static class BestMatch {
    public XMLLump bestlump;
    public int deficit = Integer.MAX_VALUE;
  }

  public BranchResolver(XMLLumpMMap globalmap) {
    this.globalmap = globalmap;
  }

  /** Returns a map of UIBranchContainer to XMLLump */
  public static Map resolveBranches(XMLLumpMMap globalmap,
      UIBranchContainer basecontainer, XMLLump parentlump) {
    BranchResolver resolver = new BranchResolver(globalmap);
    resolver.resolveRecurse(basecontainer, parentlump);
    return resolver.branchmap;
  }
  
  private void resolveRecurse(UIBranchContainer basecontainer,
      XMLLump parentlump) {
    UIComponent[] flatchildren = basecontainer.flatChildren();
    for (int i = 0; i < flatchildren.length; ++i) {
      if (flatchildren[i] instanceof UIBranchContainer) {
        UIBranchContainer branch = (UIBranchContainer) flatchildren[i];
        // ups! Do not resolve here if does not actually occur in parentlump.
        XMLLump resolved = resolveCall(parentlump, branch);
        if (Logger.log.isInfoEnabled()) {
          System.out.println("Resolved call for component "
              + branch.getClass().getName() + " fullID " + branch.getFullID()
              + " to ");
          // Logger.log.info("for component with ID " + child.ID + " to ");
          // System.out.println(debugLump(resolved));
        }
        branchmap.put(branch, resolved);
        resolveRecurse(branch, resolved);
      }
    }
  }
 
  private XMLLump resolveCall(XMLLump sourcescope, UIBranchContainer child) {
    SplitID split = new SplitID(child.ID);
    String defprefix = split.prefix + SplitID.SEPARATOR;
    BestMatch bestmatch = new BestMatch();
    if (Logger.log.isInfoEnabled()) {
      Logger.log.info("Resolving call from container " + child.debugChildren());
    }
    // first get lumps in THIS SCOPE with EXACTLY MATCHING ID.
    XMLLumpList scopelumps = sourcescope.downmap.headsForID(child.ID);
    passDeficit(bestmatch, child, scopelumps);
    if (bestmatch.deficit == 0)
      return bestmatch.bestlump;
    if (!defprefix.equals(child.ID)) {
      XMLLumpList scopedeflumps = sourcescope.downmap.headsForID(defprefix);
      passDeficit(bestmatch, child, scopedeflumps);
      if (bestmatch.deficit == 0)
        return bestmatch.bestlump;
    }
    XMLLumpList globallumps = globalmap.headsForID(child.ID);
    passDeficit(bestmatch, child, globallumps);
    if (bestmatch.deficit == 0)
      return bestmatch.bestlump;
    if (!defprefix.equals(child.ID)) {
      XMLLumpList globaldeflumps = globalmap.headsForID(defprefix);
      passDeficit(bestmatch, child, globaldeflumps);
    }
    return bestmatch.bestlump;

  }

  private void passDeficit(BestMatch bestmatch, UIBranchContainer container,
      XMLLumpList tocheck) {
    for (int i = 0; i < tocheck.size(); ++i) {
      XMLLump lump = tocheck.lumpAt(i);
      int deficit = evalDeficit(container, lump);
      if (deficit < bestmatch.deficit) {
        bestmatch.deficit = deficit;
        bestmatch.bestlump = lump;
        if (deficit == 0)
          return;
      }
    }
  }

  private static final int DEFICIT_PRIORITY = 1000001;
  private static final int REPETITIVE_DEFICIT_PRIORITY = 1001;

  // plus one, since a deficit indicates one of the concrete lump children
  // must count as an extra unficit.
  HashMap doneprefix = new HashMap(8);

  // the "deficit" is the number of (requested) children issued by the producer
  // that are not found within children of the target lump.
  // A match is either an exact match in prefix and suffix, or else a "default
  // match" produced by looking for a "default" member in the template with the
  // name "prefix:" for the issued component prefix.
  private int evalDeficit(UIBranchContainer container, XMLLump lump) {
    int deficit = 0;
    UIComponent[] children = container.flatChildren();
    doneprefix.clear();
    for (int i = 0; i < children.length; ++i) {
      UIComponent child = children[i];
      String prefix = SplitID.getPrefixColon(child.ID);
      boolean matches = lump.downmap.hasID(child.ID);
      if (matches) {
        if (prefix != null)
          doneprefix.put(prefix, Boolean.TRUE);
        continue;
      }
      int penalty = DEFICIT_PRIORITY;
      if (prefix != null) {
        boolean matchesdef = lump.downmap.hasID(prefix);
        if (matchesdef) {
          doneprefix.put(prefix, Boolean.TRUE);
          continue;
        }
        if (doneprefix.containsKey(prefix)) {
          penalty = REPETITIVE_DEFICIT_PRIORITY;
        }
      }
      deficit += penalty;
    }
    deficit += lump.downmap.numConcretes() - children.length;
    // think about also penalising "unficit" - children appearing in template
    // that are NOT ISSUED by producer. This is a balance between resolution
    // cost here (extra hashmap) and cost of searching for each template child
    // later. There may also be some layout issues.
    if (Logger.log.isInfoEnabled()) {
      Logger.log.info("Call to lump index " + lump.lumpindex + " line "
          + lump.line + " column " + lump.column + " deficit " + deficit);
    }
    return deficit;
  }

}
