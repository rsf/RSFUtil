/*
 * Created on Aug 7, 2005
 */
package uk.org.ponder.rsf.renderer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.content.ContentTypeInfo;
import uk.org.ponder.rsf.renderer.decorator.DecoratorManager;
import uk.org.ponder.rsf.template.XMLCompositeViewTemplate;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.template.XMLLumpList;
import uk.org.ponder.rsf.template.XMLLumpMMap;
import uk.org.ponder.rsf.template.XMLViewTemplate;
import uk.org.ponder.rsf.util.RSFUtil;
import uk.org.ponder.rsf.util.SplitID;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.rsf.view.ViewTemplate;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.util.Logger;
import uk.org.ponder.xml.XMLUtil;
import uk.org.ponder.xml.XMLWriter;

/**
 * Encapsulates the request-specific process of rendering a view - a
 * request-scope bean containing the implementation of the IKAT rendering
 * algorithm.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ViewRender {
  private XMLViewTemplate roott;
  private XMLLumpMMap globalmap;

  private View view;
  private RenderSystem renderer;
  private PrintOutputStream pos;
  private XMLWriter xmlw;

  // a map of UIBranchContainer to XMLLump
  private Map branchmap;
  private XMLLumpMMap collected = new XMLLumpMMap();

  private XMLLump messagelump;

  private TargettedMessageList messagelist;
  // a map of HTMLLumps to StringList of messages due to be delivered to
  // that component when it is reached (registered with a FORID prefix)
  // NB - this will not work, rethink message architecture.
  // hah. NOW it will work! Only 2 months to fix that.
  private MessageTargetMap messagetargets;
  private MessageRenderer messagerenderer;
  private String globalmessagetarget;
  private boolean rendereddeadletters;
  private ContentTypeInfo contenttypeinfo;
  private IDAssigner IDassigner;
  private DecoratorManager decoratormanager;
  private boolean debugrender;
  private RenderSystemContext rsc;

  public void setViewTemplate(ViewTemplate viewtemplateo) {
    if (viewtemplateo instanceof XMLCompositeViewTemplate) {
      XMLCompositeViewTemplate viewtemplate = (XMLCompositeViewTemplate) viewtemplateo;
      roott = viewtemplate.roottemplate;
      globalmap = viewtemplate.globalmap;
      collected.aggregate(viewtemplate.mustcollectmap);
    }
    else {
      roott = (XMLViewTemplate) viewtemplateo;
      globalmap = roott.globalmap;
    }

  }

  public void setView(View view) {
    this.view = view;
  }

  public void setRenderSystem(RenderSystem renderer) {
    this.renderer = renderer;
  }

  public void setContentTypeInfo(ContentTypeInfo contenttypeinfo) {
    this.contenttypeinfo = contenttypeinfo;
  }

  public void setMessages(TargettedMessageList messages) {
    this.messagelist = messages;
  }

  public void setGlobalMessageTarget(String globalmessagetarget) {
    this.globalmessagetarget = globalmessagetarget;
  }

  public void setMessageRenderer(MessageRenderer messagerenderer) {
    this.messagerenderer = messagerenderer;
  }

  public void setDecoratorManager(DecoratorManager decoratormanager) {
    this.decoratormanager = decoratormanager;
  }

  public void setDebugRender(boolean debugrender) {
    this.debugrender = debugrender;
  }

  private void collectContributions() {
    Set seenset = new HashSet();
    for (Iterator lumpit = branchmap.values().iterator(); lumpit.hasNext();) {
      XMLLump headlump = (XMLLump) lumpit.next();
      if (!seenset.contains(headlump.parent)) {
        collected.aggregate(headlump.parent.collectmap);
        seenset.add(headlump.parent);
      }
    }
  }

  private void debugGlobalTargets() {
    renderer.renderDebugMessage(rsc,
        "All global branch targets in resolution set:");
    for (Iterator globalit = globalmap.iterator(); globalit.hasNext();) {
      String key = (String) globalit.next();
      if (key.indexOf(':') != -1) {
        renderer.renderDebugMessage(rsc, "Branch key " + key);
        XMLLumpList res = globalmap.headsForID(key);
        for (int i = 0; i < res.size(); ++i) {
          renderer.renderDebugMessage(rsc, "\t" + res.lumpAt(i).toString());
        }
      }
    }
    renderer.renderDebugMessage(rsc, "");
  }

  public void render(PrintOutputStream pos) {
    IDassigner = new IDAssigner(debugrender ? ContentTypeInfo.ID_FORCE
        : contenttypeinfo.IDStrategy);
    UIBranchContainer messagecomponent = UIBranchContainer.make(view.viewroot,
        MessageTargetter.RSF_MESSAGES);
    branchmap = BranchResolver.resolveBranches(globalmap, view.viewroot,
        roott.rootlump);
    view.viewroot.remove(messagecomponent);
    messagelump = (XMLLump) branchmap.get(messagecomponent);
    collectContributions();
    messagetargets = MessageTargetter.targetMessages(branchmap, view,
        messagelist, globalmessagetarget);
    String declaration = contenttypeinfo.get().declaration;
    if (declaration != null)
      pos.print(declaration);
    this.pos = pos;
    this.xmlw = new XMLWriter(pos);
    rsc = new RenderSystemContext(debugrender, view, pos, xmlw, IDassigner,
        collected);
    rendereddeadletters = false;
    if (debugrender) {
      debugGlobalTargets();
    }
    renderRecurse(view.viewroot, roott.rootlump,
        roott.lumps[roott.roottagindex]);
  }

  private void renderContainer(UIContainer child, XMLLump targetlump) {
    // may have jumped template file
    XMLViewTemplate t2 = targetlump.parent;
    XMLLump firstchild = t2.lumps[targetlump.open_end.lumpindex + 1];
    if (child instanceof UIBranchContainer) {
      dumpBranchHead((UIBranchContainer) child, targetlump);
    }
    else {
      renderer.renderComponent(rsc, child, targetlump);
    }
    renderRecurse(child, targetlump, firstchild);
  }

  private void renderRecurse(UIContainer basecontainer,
      XMLLump parentlump, XMLLump baselump) {

    int renderindex = baselump.lumpindex;
    int basedepth = parentlump.nestingdepth;
    XMLViewTemplate tl = parentlump.parent;
    Set rendered = null;
    if (debugrender) {
      rendered = new HashSet();
    }

    while (true) {
      // continue scanning along this template section until we either each
      // the last lump, or the recursion level.
      renderindex = RenderUtil.dumpScan(tl.lumps, renderindex, basedepth, pos,
          true, false);
      if (renderindex == tl.lumps.length)
        break;
      XMLLump lump = tl.lumps[renderindex];
      if (lump.nestingdepth < basedepth)
        break;

      String id = lump.rsfID;
      boolean ismessage = id.startsWith(XMLLump.FORID_PREFIX);
      if (id != null && !ismessage && id.indexOf(SplitID.SEPARATOR) != -1) {
        // we have entered a repetitive domain, by diagnosis of the template.
        // Seek in the component tree for the child list that must be here
        // at this component, and process them in order, looking them up in
        // the forward map, which must ALSO be here.
        String prefix = SplitID.getPrefix(id);
        List children = fetchComponents(basecontainer, prefix);
        // these are all children with the same prefix, which will be rendered
        // synchronously.
        if (children != null) {
          for (int i = 0; i < children.size(); ++i) {
            UIComponent child = (UIComponent) children.get(i);
            if (child instanceof UIBranchContainer) {
              XMLLump targetlump = (XMLLump) branchmap.get(child);
              if (targetlump != null) {
                if (debugrender) {
                  renderComment("Branching for " + child.getFullID() + " from "
                      + lump + " to " + targetlump);
                }
                renderContainer((UIBranchContainer) child, targetlump);
                if (debugrender) {
                  renderComment("Branch returned for " + child.getFullID()
                      + " to " + lump + " from " + targetlump);
                }
              }
              else {
                if (debugrender) {
                  renderer.renderDebugMessage(rsc,
                      "No matching template branch found for branch container with full ID "
                          + child.getFullID()
                          + " rendering from parent template branch "
                          + baselump.toString());
                }
              }
            }
            else { // repetitive leaf
              XMLLump targetlump = findChild(parentlump, child);
              // this case may trigger if there are suffix-specific renderers
              // but no fallback.
              if (targetlump == null) {
                renderer.renderDebugMessage(rsc,
                    "Repetitive leaf with full ID " + child.getFullID()
                        + " could not be rendered from parent template branch "
                        + baselump.toString());
                continue;
              }
              int renderend = renderer.renderComponent(rsc, child, targetlump);
              boolean wasopentag = tl.lumps[renderend].nestingdepth >= targetlump.nestingdepth;
              if (i != children.size() - 1) {
                // at this point, magically locate any "glue" that matches the
                // transition
                // from this component to the next in the template, and scan
                // along
                // until we reach the next component with a matching id prefix.
                // NB transition matching is not implemented and may never be.
                RenderUtil.dumpScan(tl.lumps, renderend,
                    targetlump.nestingdepth - 1, pos, false, wasopentag);
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
              else {
                RenderUtil.dumpScan(tl.lumps, renderend,
                    targetlump.nestingdepth, pos, true, wasopentag);
              }
            }

          } // end for each repetitive child
        }
        else {
          if (debugrender) {
            renderer
                .renderDebugMessage(rsc, "No branch container with prefix "
                    + prefix + ": found at "
                    + RSFUtil.reportPath(basecontainer)
                    + " at template position " + baselump.toString()
                    + ", skipping");
          }
        }
        // at this point, magically locate the "postamble" from lump, and
        // reset the index.

        XMLLump finallump = parentlump.downmap.getFinal(prefix);
        XMLLump closefinal = finallump.close_tag;
        renderindex = closefinal.lumpindex + 1;
        if (debugrender) {
          renderComment("Stack returned from branch for ID " + id + " to "
              + baselump.toString() + ": skipping from " + lump.toString()
              + " to " + closefinal.toString());
        }
      }
      else if (ismessage) {
        TargettedMessageList messages = messagetargets.getMessages(lump);
        if (messages == null)
          messages = new TargettedMessageList();
        if (!rendereddeadletters) {
          rendereddeadletters = true;
          TargettedMessageList deadmessages = messagetargets
              .getMessages(MessageTargetter.DEAD_LETTERS);
          if (deadmessages != null) {
            messages.addMessages(deadmessages);
          }
        }
        if (messages.size() != 0) {
          if (messagelump == null) {
            Logger.log
                .warn("No message template is configured (containing branch with rsf id rsf-messages:)");
          }
          else {
            UIBranchContainer messagebranch = messagerenderer
                .renderMessageList(messages);
            renderContainer(messagebranch, messagelump);
          }
        }
        XMLLump closelump = lump.close_tag;
        renderindex = closelump.lumpindex + 1;
      }
      else {
        // no colon - continue template-driven.
        // it is a single, irrepitable component - just render it, and skip
        // on, or skip completely if there is no peer in the component tree.
        UIComponent component = null;
        if (id != null) {
          if (debugrender) {
            rendered.add(id);
          }
          component = fetchComponent(basecontainer, id);
        }
        // Form rendering is now subject to "fairly normal" branch rendering logic
        // That is, a UIContainer may now also be a leaf
        if (component instanceof UIContainer) {
          renderContainer((UIContainer) component, lump);
          renderindex = lump.close_tag.lumpindex + 1;
        }
        else {
          // if we find a leaf component, render it.
          renderindex = renderer.renderComponent(rsc, component, lump);   
        }
      } // end if unrepeatable component.
      if (renderindex == tl.lumps.length) {
        // deal with the case where component was root element - Ryan of
        // 11/10/06
        break;
      }
    }
    if (debugrender) {
      UIComponent[] flatchildren = basecontainer.flatChildren();
      for (int i = 0; i < flatchildren.length; ++i) {
        UIComponent child = flatchildren[i];
        if (!(child.ID.indexOf(':') != -1) && !rendered.contains(child.ID)) {
          renderer.renderDebugMessage(rsc, "Leaf child component "
              + child.getClass().getName() + " with full ID "
              + child.getFullID() + " could not be found within template "
              + baselump.toString());
        }
      }
    }
  }

  private void renderComment(String string) {
    pos.print("<!-- " + string + "-->");

  }

  private static UIComponent fetchComponent(UIContainer basecontainer,
      String id) {
    while (basecontainer != null) {
      UIComponent togo = basecontainer.getComponent(id);
      if (togo != null)
        return togo;
      basecontainer = basecontainer.parent;
    }
    return null;
  }

  private static List fetchComponents(UIContainer basecontainer, String id) {
    while (basecontainer != null) {
      List togo = basecontainer.getComponents(id);
      if (togo != null)
        return togo;
      basecontainer = basecontainer.parent;
    }
    return null;
  }

  private XMLLump findChild(XMLLump sourcescope, UIComponent child) {
    // if child is not a container, there can be no lookahead in resolution,
    // and it must resolve to a component in THIS container which either
    // matches exactly or in prefix.
    SplitID split = new SplitID(child.ID);
    XMLLumpList headlumps = sourcescope.downmap.headsForID(child.ID);
    if (headlumps == null && split.suffix != null) {
      headlumps = sourcescope.downmap.headsForID(split.prefix
          + SplitID.SEPARATOR);
      // if (headlumps.size() == 0) {
      // throw UniversalRuntimeException.accumulate(new IOException(),
      // "Error in template file: peer for component with ID " + child.ID
      // + " not found in scope " + sourcescope.toDebugString());
      // }
    }
    return headlumps == null ? null
        : headlumps.lumpAt(0);
  }

  private void dumpBranchHead(UIBranchContainer branch, XMLLump targetlump) {
    HashMap attrcopy = new HashMap();
    attrcopy.putAll(targetlump.attributemap);
    IDassigner.adjustForID(attrcopy, branch);
    decoratormanager.decorate(branch.decorators, targetlump.getTag(), attrcopy);
    // TODO: normalise this silly space business
    pos
        .write(targetlump.parent.buffer, targetlump.start,
            targetlump.length - 1);
    XMLUtil.dumpAttributes(attrcopy, xmlw);
    pos.print(">");
  }

  public static String debugLump(XMLLump debug) {
    XMLLump[] lumps = debug.parent.lumps;
    CharWrap message = new CharWrap();
    message.append("Lump index " + debug.lumpindex + " (line " + debug.line
        + " column " + debug.column + ") ");
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
      message.append(lump.parent.buffer, lump.start, lump.length);
    }
    if (debug.downmap != null) {
      message.append("\nDownmap here: ").append(debug.downmap.getHeadsDebug());
    }
    return message.toString();
  }

}
