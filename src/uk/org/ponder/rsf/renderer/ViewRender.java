/*
 * Created on Aug 7, 2005
 */
package uk.org.ponder.rsf.renderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIIKATContainer;
import uk.org.ponder.rsf.components.UIOutputMultiline;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.template.XMLLumpList;
import uk.org.ponder.rsf.template.XMLLumpMMap;
import uk.org.ponder.rsf.template.XMLViewTemplate;
import uk.org.ponder.rsf.util.SplitID;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.rsf.view.ViewTemplate;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * Encapsulates the request-specific process of rendering a view - 
 * a request-scope bean containing the implementation of the IKAT rendering
 * algorithm. 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ViewRender {

  private XMLViewTemplate template;
  private XMLLump[] lumps;
  private XMLLump rootlump;
  private XMLLumpMMap globalmap;
  private View view;
  private RenderSystem renderer;
  private PrintOutputStream pos;

  // a map of HTMLLumps to StringList of messages due to be delivered to
  // that component when it is reached (registered with a FORID prefix)
  // NB - this will not work, rethink message architecture.
  private Map messagetargets;

  public void setViewTemplate(ViewTemplate viewtemplateo) {
    // TODO: hack for now - will we have other kinds of template?
    XMLViewTemplate viewtemplate = (XMLViewTemplate) viewtemplateo;
    this.template = viewtemplate;
    this.lumps = template.lumps;
    this.rootlump = template.rootlump;
    this.globalmap = template.globalmap;
  }
   
  public void setView(View view) {
    this.view = view;
  }
  
  public void setRenderSystem(RenderSystem renderer) {
    this.renderer = renderer;
  }

  public void render(PrintOutputStream pos) {
    messagetargets = new HashMap();
    // need to fix this - can we actually do it statically?
    // INPUT will be a list of messages, each with a component target.
    // in order to resolve these, in every scope we actually HIT, we must know
    // whether any FORID components we see is the right one for it.
    // Clearly it is the right one if it matches exactly, and if nothing matches
    // exactly a general FORID:.
    // but the problem is which INSTANCE of the component will get the message?
    // these will be targetted on FULL IDs, i.e. a full path. So as well piddle
    // along scopes, we will hopefully be able to reconstruct once we get to the
    // right instance of it for the message, since a UIComponent will have
    // the right fullID, just at the moment we have it in our hands, and have
    // matched it with a proper HTMLLump. Question is, might this be TOO LATE
    // to have figured out the HTMLLump that has the message in!!! It might
    // have gone past!! So, once we BEGIN a scope, we will have to be able to
    // GUESS whether there will be message components coming up with a
    // particular
    // fullID. We hope that fullIDs will nest correctly (NB now EXTRA condition
    // on fullIDs), so we can look at the fullID of the Container that
    // corresponds
    // to the base lump for this scope. If everything EXCEPT the last part
    // matches for something we have a message queued up for, we can get geared
    // up
    // to snagging the FORID component as it goes past. So all we need to have
    // noted for each SCOPE is WHICH FORIDs will be directed onto the global
    // target in that scope, and which will be directed precisely.
    // This only leaves the issue of WHICH GLOBAL FORID will be used!!!
    // hopefully ONLY ONE will be met with in the entire template. Well, not
    // quite!
    // Targetted messages will ONLY be delivered to the form which gave rise to
    // a particular submission. Of course this may include repeated components
    // itself, try not to bother with this for now. But it will have its OWN
    // global FORID that should override the external global FORID on the whole
    // page.
    // for (Iterator forit = view.IDtomessage.keySet().iterator();
    // forit.hasNext();) {
    // String forid = (String) forit.next();
    // String message = (String) view.IDtomessage.get(forid);
    // HTMLLump headlump = template.foridtocomponent.get(FORID_PREFIX);
    // HTMLLumpList forlumps = template.headsForID(HTMLLump.FORID_PREFIX);
    // for (int i = 0; i < forlumps.size(); ++ i) {
    // addMessageTarget(forlumps.lumpAt(i), message);
    // }
    //      
    // }
    pos.print(renderer.getDeclaration());
    this.pos = pos;
    renderRecurse(view.viewroot, rootlump, lumps[1]);
  }

  private void addMessageTarget(XMLLump forlump, String message) {
    StringList messages = (StringList) messagetargets.get(forlump);
    if (messages == null) {
      messages = new StringList();
      messagetargets.put(forlump, messages);
    }
    messages.add(message);
  }

  private void renderRecurse(UIIKATContainer basecontainer, XMLLump parentlump,
      XMLLump baselump) {

    int renderindex = baselump.lumpindex;
    int basedepth = parentlump.nestingdepth;

    while (true) {
      // continue scanning along this template section until we either each
      // the last lump, or the recursion level.
      renderindex = RenderUtil.dumpScan(lumps, renderindex, basedepth, pos);
      if (renderindex == lumps.length)
        break;
      XMLLump lump = lumps[renderindex];
      if (lump.nestingdepth < basedepth)
        break;

      String id = lump.rsfID;
      if (id != null && id.indexOf(SplitID.SEPARATOR) != -1) {
        // we have entered a repetitive domain, by diagnosis of the template.
        // Seek in the component tree for the child list that must be here
        // at this component, and process them in order, looking them up in
        // the forward map, which must ALSO be here.
        String prefix = SplitID.getPrefix(id);
        List children = basecontainer.getComponents(prefix);
        // these are all children with the same prefix, which will be rendered
        // synchronously.
        if (children != null) {
          for (int i = 0; i < children.size(); ++i) {
            UIComponent child = (UIComponent) children.get(i);
//            Logger.log.info("Resolved call from ");
//            debugLump(lump);
            XMLLump targetlump = resolveCall(parentlump, child);
            if (Logger.log.isInfoEnabled()) {
            System.out.println("Resolved call for component " + child.getClass().getName() + " fullID " + child.getFullID() + " to ");
//            Logger.log.info("for component with ID " + child.ID + " to ");
            System.out.println(debugLump(targetlump));
            }
            // NB - leaf components may now be containers.
            if (child.getClass() == UIIKATContainer.class) {
              XMLLump firstchild = lumps[targetlump.open_end.lumpindex + 1];
              dumpContainerHead(targetlump, firstchild);
              renderRecurse((UIIKATContainer) child, targetlump, firstchild);
            }
            else {
              int renderend = renderer.renderComponent(child, lumps,
                  targetlump.lumpindex, pos);
              if (i != children.size() - 1) {
                // at this point, magically locate any "glue" that matches the
                // transition
                // from this component to the next in the template, and scan
                // along
                // until we reach the next component with a matching id prefix.
                // NB transition matching is not implemented and may never be.
                RenderUtil.dumpScan(lumps, renderend, basedepth, pos);
                // we discard any index reached by this dump, continuing the
                // controlled sequence as long as there are any children.
                // given we are in the middle of a sequence here, we expect to
                // see nothing perverse like components or forms, at most static
                // things (needing rewriting?)
                // TODO: split of beginning logic from renderComponent that
                // deals
                // with static rewriting, and somehow fix this call to dumpScan
                // so that it can invoke it. Not urgent, we currently only have
                // the TINIEST text forming repetition glue.
              }
            }

          }
        }
        else {
          if (Logger.log.isInfoEnabled()) {
          Logger.log.info("No component with prefix " + prefix
              + " found for domain at template lump " + renderindex +", skipping");
          }
        }
        // at this point, magically locate the "postamble" from lump, and
        // reset the index.
//        Logger.log.info("Stack returned: skipping domain from ");
//        debugLump(lump);
        XMLLump finallump = parentlump.downmap.getFinal(prefix);
        XMLLump closefinal = finallump.close_tag;
        renderindex = closefinal.lumpindex + 1;
//        Logger.log.info("to ");
//        debugLump(lumps[renderindex]);
      }
      else {
        // it is a single, irrepitable component - just render it, and skip
        // on, or skip completely if there is no peer in the component tree.
        UIComponent component = null;
        if (id != null) {
          if (id.startsWith(XMLLump.FORID_PREFIX)) {
            StringList messages = (StringList) messagetargets.get(lump);
            component = getMessageComponent(messages);
          }
          else {
            component = basecontainer.getComponent(id);
          }
        }
        // if we find a leaf component, render it.
        renderindex = renderer.renderComponent(component, lumps, renderindex,
            pos);
      } // end if unrepeatable component.
    }
  }

  private void dumpContainerHead(XMLLump targetlump, XMLLump firstchild) {
    // broken out as a method in case we want to censor rsf:id attributes one
    // day.
    RenderUtil.dumpTillLump(lumps, targetlump.lumpindex, firstchild.lumpindex,
        pos);
  }

  private String debugLump(XMLLump debug) {
    CharWrap message = new CharWrap();
    message.append("Lump index " + debug.lumpindex + " (line " + debug.line + " column " + debug.column + ") ");
    int frontpoint = debug.lumpindex - 5;
    if (frontpoint < 0)
      frontpoint = 0;
    int endpoint = debug.lumpindex + 5;
    if (frontpoint > lumps.length)
      frontpoint = lumps.length;
    for (int i = frontpoint; i < endpoint; ++i) {
      if (i == debug.lumpindex) {
        message.append("(*)");
      }
      XMLLump lump = lumps[i];
      message.append(lump.buffer, lump.start, lump.length);
    }
    if (debug.downmap != null) {
      message.append("\nDownmap here: ").append(debug.downmap.getHeadsDebug());
    }
    return message.toString();
  }

  private XMLLump resolveCall(XMLLump sourcescope, UIComponent child) {
    SplitID split = new SplitID(child.ID);
    if (child instanceof UIIKATContainer) {
      String defprefix = split.prefix + SplitID.SEPARATOR;
      BestMatch bestmatch = new BestMatch();
      UIIKATContainer childc = (UIIKATContainer) child;
      if (Logger.log.isInfoEnabled()) {
        Logger.log.info("Resolving call from container "
            + childc.debugChildren());
      }
      // first get lumps in THIS SCOPE with EXACTLY MATCHING ID.
      XMLLumpList scopelumps = sourcescope.downmap.headsForID(child.ID);
      passDeficit(bestmatch, childc, scopelumps);
      if (bestmatch.deficit == 0)
        return bestmatch.bestlump;
      if (!defprefix.equals(child.ID)) {
        XMLLumpList scopedeflumps = sourcescope.downmap.headsForID(defprefix);
        passDeficit(bestmatch, childc, scopedeflumps);
        if (bestmatch.deficit == 0)
          return bestmatch.bestlump;
      }
      XMLLumpList globallumps = globalmap.headsForID(child.ID);
      passDeficit(bestmatch, childc, globallumps);
      if (bestmatch.deficit == 0)
        return bestmatch.bestlump;
      if (!defprefix.equals(child.ID)) {
        XMLLumpList globaldeflumps = globalmap.headsForID(defprefix);
        passDeficit(bestmatch, childc, globaldeflumps);
      }
      return bestmatch.bestlump;
    }
    else {
      // if child is not a container, there can be no lookahead in resolution,
      // and it must resolve to a component in THIS container which either
      // matches exactly or in prefix.
      XMLLumpList headlumps = sourcescope.downmap.headsForID(child.ID);
      if (headlumps.size() == 0) {
        headlumps = sourcescope.downmap.headsForID(split.prefix + SplitID.SEPARATOR);
        if (headlumps.size() == 0) {
        throw UniversalRuntimeException.accumulate(new IOException(),
            "Error in template file: component with ID " + child.ID
                + " not found");
        }
      }
      return headlumps.lumpAt(0);
    }
  }

  private void passDeficit(BestMatch bestmatch, UIIKATContainer container,
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
  private int evalDeficit(UIIKATContainer container, XMLLump lump) {
    int deficit = 0;
    UIComponent[] children = container.flatChildren();
    doneprefix.clear();
    for (int i = 0; i < children.length; ++i) {
      UIComponent child = children[i];
      String prefix = SplitID.getPrefixColon(child.ID);
      boolean matches = lump.downmap.hasID(child.ID);
      if (matches) {
        if (prefix != null) doneprefix.put(prefix, Boolean.TRUE);
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
      Logger.log.info("Call to lump index " + lump.lumpindex + " line " + lump.line + " column " + lump.column + " deficit "
        + deficit);
    }
    return deficit;
  }

  private UIComponent getMessageComponent(StringList messages) {
    if (messages == null)
      return null;
    UIOutputMultiline togo = new UIOutputMultiline();
    togo.setValue(messages);
    // hack, awaiting repetitive message domains.
    // URGH! This is actually a UIOutputMultiLine!!! Hack eluded!!
    return togo;
  }

  private static class BestMatch {
    public XMLLump bestlump;
    public int deficit = Integer.MAX_VALUE;
  }

}
