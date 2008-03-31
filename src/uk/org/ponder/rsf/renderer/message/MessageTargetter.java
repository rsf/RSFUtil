/*
 * Created on Nov 14, 2005
 */
package uk.org.ponder.rsf.renderer.message;

import java.util.Map;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.ComponentList;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.template.XMLLumpList;
import uk.org.ponder.rsf.util.RSFUtil;
import uk.org.ponder.rsf.util.SplitID;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.util.Logger;

public class MessageTargetter {
  // a dummy key to be used to signify a target for messages whose targets
  // cannot be found.
  public static final XMLLump DEAD_LETTERS = new XMLLump(); 
  public static final String RSF_MESSAGES = "rsf-messages:";
  public static class BestTarget {
    public XMLLump container;
    public CharWrap rootpath = new CharWrap();
    public XMLLump bestfor = null;

    public void setContainer(XMLLump container) {
      this.container = container;
    }

    public void checkTarget(String ID) {
      XMLLumpList forlumps = container.downmap.headsForID(ID);
      if (forlumps != null && forlumps.size() > 0) {
        bestfor = forlumps.lumpAt(0);
      }
    }
  }

  /**
   * For each message, find the MOST SPECIFIC message-for component in the tree
   * claiming to accept it. Search starts at the very root of the tree, with the
   * most generic target of all with simply the ID "message-for:*".
   * 
   * @param branchmap
   *          A map of UIBranchContainers to XMLLumps, as prepared by pass 1 of
   *          the renderer.
   * @param view
   *          The view being rendered - used for its component ID map.
   * @param messages
   *          The list of messages to be distributed to message target
   *          components.
   * @param globalmessagetarget
   *          The fullID of the "submitting control" for the previous view. This
   *          will form the SECOND LEVEL fallback after all targets in the
   *          GLOBAL ROOT have been searched. This should almost always be set.
   */
  
  public static MessageTargetMap targetMessages(Map branchmap, View view,
      TargettedMessageList messages, String globalmessagetarget) {
    MessageTargetMap togo = new MessageTargetMap();
    if (messages == null) return togo;
    
    BestTarget best = new BestTarget();
    UIComponent globaltarget = globalmessagetarget == null ? null
        : view.getComponent(globalmessagetarget);
    ComponentList globalrootpath = globaltarget == null ? null
        : RSFUtil.getRootPath(globaltarget);

    for (int i = 0; i < messages.size(); ++i) {
      TargettedMessage message = messages.messageAt(i);
      String targetid = message.targetid;
      if (targetid.equals(TargettedMessage.TARGET_NONE)) {
        targetid = globalmessagetarget;
      }
      best.bestfor = DEAD_LETTERS;
      UIComponent target = view.getComponent(targetid);
      if (targetid != null) {
        int colpos = targetid.lastIndexOf(':');
        if (colpos != -1) {
          targetid = targetid.substring(colpos + 1);
        }
      }
      // TODO: what if action has, despite errors, insisted on redirecting to
      // a DIFFERENT view?
      if (target == null) {
        // remove this spurious warning for now. "globaltarget" system was
        // intended to at least identify the right form in the view by means
        // of the submitting control, but needs review. In the meantime, there
        // is now the "DEAD_LETTER" last-ditch system for final delivery.
//        Logger.log.warn("Warning: Message " + message.acquireMessageCode()
//            + " queued for nonexistent component ID " + message.targetid);
      }
      else {
        SplitID split = new SplitID(targetid);
        boolean hassuffix = split.suffix != null;
        ComponentList rootpath = RSFUtil.getRootPath(target);
        for (int j = 0; j < rootpath.size() - 1; ++j) {
          if (globalrootpath != null && j == globalrootpath.size() - 2) {
            // if we are at the branch level of the "submitting control" (which
            // as we recall will ***NOT*** be nested in the branch stack at 
            // this point, implement the first-level fallback check for 
            // messages targetted at it globally.
            UIContainer globalbranch = (UIContainer) globalrootpath.get(j);
            XMLLump peer = (XMLLump) branchmap.get(globalbranch);
            best.setContainer(peer);
            String search0 = XMLLump.FORID_PREFIX + targetid;
            best.checkTarget(search0);
          }
          UIContainer branch = (UIContainer) rootpath.get(j);
          XMLLump peer = (XMLLump) branchmap.get(branch);
          best.setContainer(peer);
          best.checkTarget(XMLLump.FORID_PREFIX);
          String search1 = XMLLump.FORID_PREFIX + split.prefix;
          best.checkTarget(search1);
          if (hassuffix) {
            String search2 = XMLLump.FORID_PREFIX + targetid;
            best.checkTarget(search2);
          }
        }
      }
      if (best.bestfor != null) {
        togo.setTarget(best.bestfor, message);
      }
      else {
        // well noone can say we didn't try our darndest to deliver this message.
        Logger.log.error("Unable to deliver message " + 
            message.acquireMessageCode() +
            " targetted at " + message.targetid);
      }
    }
    return togo;
  }
}
