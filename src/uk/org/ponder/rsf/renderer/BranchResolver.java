/*
 * Created on Nov 14, 2005
 */
package uk.org.ponder.rsf.renderer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.template.XMLLumpList;
import uk.org.ponder.rsf.template.XMLLumpMMap;
import uk.org.ponder.rsf.util.SplitID;
import uk.org.ponder.rsf.view.ViewRoot;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * Performs a first "light" pass of the template and component tree to resolve
 * references by UIBranchContainer components to the correct tag targets.
 * 
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
    boolean debug = false;
    Level oldlevel = null;
    if (basecontainer instanceof ViewRoot) {
      debug = ((ViewRoot) basecontainer).debug;
    }
    if (debug) {
      oldlevel = Logger.log.getLevel();
      Logger.log.setLevel(Level.DEBUG);
    }
    try {
      BranchResolver resolver = new BranchResolver(globalmap);
      resolver.branchmap.put(basecontainer, parentlump);
      resolver.resolveRecurse(basecontainer, parentlump);
      return resolver.branchmap;
    }
    finally {
      if (debug) {
        Logger.log.setLevel(oldlevel);
      }
    }
  }

  private void resolveRecurse(UIContainer basecontainer, XMLLump parentlump) {
    UIComponent[] flatchildren = basecontainer.flatChildren();
    for (int i = 0; i < flatchildren.length; ++i) {
      if (flatchildren[i] instanceof UIContainer) {
        UIContainer branch = (UIContainer) flatchildren[i];
        // ups! Do not resolve here if does not actually occur in parentlump.
        XMLLump resolved = resolveCall(parentlump, branch);
        if (Logger.log.isDebugEnabled()) {
          Logger.log.debug("Resolving call for component "
              + branch.getClass().getName() + " fullID " + branch.getFullID());
          if (resolved == null) {
            Logger.log.debug("No target found!");
          }
          else {
            Logger.log.debug(resolved.toDebugString());
          }
          // Logger.log.info("for component with ID " + child.ID + " to ");
          // System.out.println(debugLump(resolved));
        }
        if (resolved != null) {
          branchmap.put(branch, resolved);
          resolveRecurse(branch, resolved);
        }
      }
    }
  }

  private XMLLump resolveCall(XMLLump sourcescope, UIContainer child) {
    String searchID = child instanceof UIJointContainer ? ((UIJointContainer) child).jointID
        : child.ID;
    SplitID split = new SplitID(searchID);
    String defprefix = split.prefix + SplitID.SEPARATOR;
    BestMatch bestmatch = new BestMatch();
    if (Logger.log.isDebugEnabled()) {
      Logger.log.debug("Resolving call for ID " + searchID + " from container "
          + child.debugChildren());
    }
    // first get lumps in THIS SCOPE with EXACTLY MATCHING ID.
    XMLLumpList scopelumps = sourcescope.downmap.headsForID(searchID);
    passDeficit(bestmatch, child, scopelumps);
    if (bestmatch.deficit == 0)
      return bestmatch.bestlump;
    if (!defprefix.equals(child.ID)) {
      XMLLumpList scopedeflumps = sourcescope.downmap.headsForID(defprefix);
      passDeficit(bestmatch, child, scopedeflumps);
      if (bestmatch.deficit == 0)
        return bestmatch.bestlump;
    }
    // only enable global resolution if it is a branch
    if (child instanceof UIBranchContainer) {
      XMLLumpList globallumps = globalmap.headsForID(searchID);
      passDeficit(bestmatch, child, globallumps);
      if (bestmatch.deficit == 0)
        return bestmatch.bestlump;
      if (!defprefix.equals(searchID)) {
        XMLLumpList globaldeflumps = globalmap.headsForID(defprefix);
        passDeficit(bestmatch, child, globaldeflumps);
      }
    }
    return bestmatch.bestlump;

  }

  private void passDeficit(BestMatch bestmatch, UIContainer container,
      XMLLumpList tocheck) {
    if (tocheck == null)
      return;
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
  private int evalDeficit(UIContainer container, XMLLump lump) {
    if (lump.downmap == null) {
      throw UniversalRuntimeException
          .accumulate(
              new IllegalArgumentException(),
              "Error in template file: "
                  + lump.toDebugString()
                  + " with id "
                  + container.getFullID()
                  + " is the target of branch resolution but does not have branch ID");
    }
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
    if (Logger.log.isDebugEnabled()) {
      Logger.log.debug("Call to " + lump.toDebugString() + " deficit "
          + deficit);
    }
    return deficit;
  }

}
