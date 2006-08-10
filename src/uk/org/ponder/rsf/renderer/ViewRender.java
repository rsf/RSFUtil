/*
 * Created on Aug 7, 2005
 */
package uk.org.ponder.rsf.renderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.content.ContentTypeInfo;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.template.XMLLumpList;
import uk.org.ponder.rsf.template.XMLLumpMMap;
import uk.org.ponder.rsf.template.XMLViewTemplate;
import uk.org.ponder.rsf.util.SplitID;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.rsf.view.ViewTemplate;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.util.Logger;
import uk.org.ponder.xml.XMLUtil;
import uk.org.ponder.xml.XMLWriter;

/**
 * Encapsulates the request-specific process of rendering a view - 
 * a request-scope bean containing the implementation of the IKAT rendering
 * algorithm. 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ViewRender {
  private XMLViewTemplate template; // here only as a reminder for inversion
  private XMLLump[] lumps;
  private int roottagindex;
  private XMLLump rootlump;
  private XMLLumpMMap globalmap;
  private View view;
  private RenderSystem renderer;
  private PrintOutputStream pos;
  private XMLWriter xmlw;

  private Map branchmap;
  
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
  private DecoratorManager decoratormanager;

  public void setViewTemplate(ViewTemplate viewtemplateo) {
    // TODO: hack for now - will we have other kinds of template?
    XMLViewTemplate viewtemplate = (XMLViewTemplate) viewtemplateo;
    this.template = viewtemplate;
    this.lumps = template.lumps;
    this.rootlump = template.rootlump;
    this.globalmap = template.globalmap;
    this.roottagindex = template.roottagindex;
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
  
  public void render(PrintOutputStream pos) {
    branchmap = BranchResolver.resolveBranches(globalmap, view.viewroot, rootlump);
    messagetargets = MessageTargetter.targetMessages(branchmap, view, 
        messagelist, globalmessagetarget);
    String declaration = contenttypeinfo.get().declaration;
    if (declaration != null) pos.print(declaration);
    this.pos = pos;
    this.xmlw = new XMLWriter(pos);
    rendereddeadletters = false;
    renderRecurse(view.viewroot, rootlump, lumps[roottagindex]);
  }

  private void renderRecurse(UIBranchContainer basecontainer, XMLLump parentlump,
      XMLLump baselump) {

    int renderindex = baselump.lumpindex;
    int basedepth = parentlump.nestingdepth;

    while (true) {
      // continue scanning along this template section until we either each
      // the last lump, or the recursion level.
      renderindex = RenderUtil.dumpScan(lumps, renderindex, basedepth, pos, true);
      if (renderindex == lumps.length)
        break;
      XMLLump lump = lumps[renderindex];
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
        List children = basecontainer.getComponents(prefix);
        // these are all children with the same prefix, which will be rendered
        // synchronously.
        if (children != null) {
          for (int i = 0; i < children.size(); ++i) {
            UIComponent child = (UIComponent) children.get(i);
            if (child instanceof UIBranchContainer) {
              XMLLump targetlump = (XMLLump) branchmap.get(child);
              if (targetlump != null) {
                XMLLump firstchild = lumps[targetlump.open_end.lumpindex + 1];
                dumpContainerHead((UIBranchContainer) child, targetlump);
                renderRecurse((UIBranchContainer) child, targetlump, firstchild);
              }
            }
            else { // repetitive non-branch
              XMLLump targetlump = findChild(parentlump, child);
              // this case may trigger if there are suffix-specific renderers
              // but no fallback.
              if (targetlump == null) continue;
              int renderend = renderer.renderComponent(child, view, lumps,
                  targetlump.lumpindex, pos, contenttypeinfo.IDStrategy);
              if (i != children.size() - 1) {
                // at this point, magically locate any "glue" that matches the
                // transition
                // from this component to the next in the template, and scan
                // along
                // until we reach the next component with a matching id prefix.
                // NB transition matching is not implemented and may never be.
                RenderUtil.dumpScan(lumps, renderend, targetlump.nestingdepth - 1, pos, false);
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
          if (Logger.log.isDebugEnabled()) {
          Logger.log.debug("No component with prefix " + prefix
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
        // no colon - continue template-driven.
        // it is a single, irrepitable component - just render it, and skip
        // on, or skip completely if there is no peer in the component tree.
        UIComponent component = null;
        if (id != null) {
          if (ismessage) {
            TargettedMessageList messages = messagetargets.getMessages(lump);
            if (messages == null) messages = new TargettedMessageList();
            if (!rendereddeadletters) {
              rendereddeadletters = true;
              TargettedMessageList deadmessages = 
                messagetargets.getMessages(MessageTargetter.DEAD_LETTERS);
              if (deadmessages != null) {
                messages.addMessages(deadmessages);
              }
            }
            component = messagerenderer.renderMessageList(messages);
          }
          else {
            component = basecontainer.getComponent(id);
          }
        }
        // if we find a leaf component, render it.
        renderindex = renderer.renderComponent(component, view, lumps, renderindex,
            pos, contenttypeinfo.IDStrategy);
      } // end if unrepeatable component.
    }
  }

  private XMLLump findChild(XMLLump sourcescope, UIComponent child) {
    // if child is not a container, there can be no lookahead in resolution,
    // and it must resolve to a component in THIS container which either
    // matches exactly or in prefix.
    SplitID split = new SplitID(child.ID);
    XMLLumpList headlumps = sourcescope.downmap.headsForID(child.ID);
    if (headlumps.size() == 0 && split.suffix != null) {
      headlumps = sourcescope.downmap.headsForID(split.prefix
          + SplitID.SEPARATOR);
//      if (headlumps.size() == 0) {
//        throw UniversalRuntimeException.accumulate(new IOException(),
//            "Error in template file: peer for component with ID " + child.ID
//                + " not found in scope " + sourcescope.toDebugString());
//      }
    }
    return headlumps.size() > 0? headlumps.lumpAt(0) : null;
  }

  private void dumpContainerHead(UIBranchContainer branch, 
      XMLLump targetlump) {
    HashMap attrcopy = new HashMap();
    attrcopy.putAll(targetlump.attributemap);
    String IDStrategy = RenderUtil.determineIDStrategy(branch, contenttypeinfo.IDStrategy);
    RenderUtil.adjustForID(attrcopy, IDStrategy, branch.getFullID());
    decoratormanager.decorate(branch.decorators, targetlump.getTag(), attrcopy);
    // TODO: normalise this silly space business
    pos.write(targetlump.buffer, targetlump.start, targetlump.length - 1);
    XMLUtil.dumpAttributes(attrcopy, xmlw);
    pos.print(">");
  }

  public static String debugLump(XMLLump debug, XMLLump[] lumps) {
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

}
