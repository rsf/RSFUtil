/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.renderer.html;

import java.util.HashMap;
import java.util.Iterator;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInputBase;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIOutputMultiline;
import uk.org.ponder.rsf.components.UISelectBoolean;
import uk.org.ponder.rsf.renderer.ComponentRenderer;
import uk.org.ponder.rsf.renderer.RenderSystem;
import uk.org.ponder.rsf.renderer.RenderUtil;
import uk.org.ponder.rsf.renderer.StaticComponentRenderer;
import uk.org.ponder.rsf.renderer.StaticRendererCollection;
import uk.org.ponder.rsf.state.SubmittedValueEntry;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.template.XMLLumpList;
import uk.org.ponder.streamutil.PrintOutputStream;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.xml.XMLWriter;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
// assumes that all values are now set, link target for href is fixed up.
// so - fixups must i) traverse bean model and try to create stuff
// ii) resolve links, well! links will always be emitted by the global
// munger, which we hope at worse will issue a prefix and some additional
// parameters. Unless of course they are absolute links...
public class BasicHTMLRenderSystem implements RenderSystem {
  private String declaration = "<!DOCTYPE html      " +
        "PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"" + 
     " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
  private StaticRendererCollection scrc;

  public Object copy() {
    BasicHTMLRenderSystem togo = new BasicHTMLRenderSystem();
    return togo;
  }
  
  public String getDeclaration() {
    return declaration;
  }
    
  public void setStaticRenderers(StaticRendererCollection scrc) {
    this.scrc = scrc;
  }
  
  // No, this method will not stay like this forever! We plan on an architecture
  // with renderer-per-component "class" as before, plus interceptors.
  // Although a lot of the parameterisation now lies in the allowable tag
  // set at target.
  public int renderComponent(UIComponent torendero, XMLLump[] lumps,
      int lumpindex, PrintOutputStream pos) {
    XMLWriter xmlw = new XMLWriter(pos);
    XMLLump lump = lumps[lumpindex];
    int nextpos = -1;
    XMLLump outerendopen = lump.open_end;
    XMLLump outerclose = lump.close_tag;

    nextpos = outerclose.lumpindex + 1;

    XMLLumpList payloadlist = lump.downmap == null ? null
        : lump.downmap.hasID(XMLLump.PAYLOAD_COMPONENT) ? lump.downmap
            .headsForID(XMLLump.PAYLOAD_COMPONENT)
            : null;
    XMLLump payload = payloadlist == null ? null
        : payloadlist.lumpAt(0);

    // if there is no peer component, it might still be a static resource holder
    // that needs URLs rewriting.
    // we assume there is no payload component here, since there is no producer
    // ID that might govern selection. So we use "outer" indices.
    if (torendero == null) {
      if (lump.rsfID.startsWith(XMLLump.SCR_PREFIX)) {
        String scrname = lump.rsfID.substring(XMLLump.SCR_PREFIX.length());
        StaticComponentRenderer scr = scrc.getSCR(scrname);
        if (scr != null) {
          int tagtype = scr.render(lumps, lumpindex, xmlw);
          nextpos = tagtype == ComponentRenderer.LEAF_TAG? 
              outerclose.lumpindex + 1 : outerendopen.lumpindex + 1;
        }
      }
     
      if (lump.textEquals("<form ")) {
        // Logger.log.info("Warning: skipping form with all children at lump
        // index " + lumpindex);
      }
      // if it is not a component marked with URL_REWRITE, just skip rendering
      // it completely.
    }
    else {
      // else there IS a component and we are going to render it. First make
      // sure we render any preamble.
      XMLLump endopen = outerendopen;
      XMLLump close = outerclose;
      XMLLump uselump = lump;
      if (payload != null) {
        endopen = payload.open_end;
        close = payload.close_tag;
        uselump = payload;
        RenderUtil.dumpTillLump(lumps, lumpindex, payload.lumpindex, pos);
        lumpindex = payload.lumpindex;
      }

      String fullID = torendero.getFullID();
      //HashMap attrcopy = (HashMap) uselump.attributemap.clone();
      HashMap attrcopy = new HashMap();
      attrcopy.putAll(uselump.attributemap);
      attrcopy.put("id", fullID);
      attrcopy.remove(XMLLump.ID_ATTRIBUTE);
      // ALWAYS dump the tag name, this can never be rewritten. (probably?!)
      pos.write(uselump.buffer, uselump.start, uselump.length);

      if (torendero.getClass() == UIOutput.class) {
        String value = ((UIOutput) torendero).text;
        if (value == null) {
          RenderUtil.dumpTillLump(lumps, lumpindex + 1, close.lumpindex + 1,
              pos);
        }
        else {
          RenderUtil.dumpTillLump(lumps, lumpindex + 1, endopen.lumpindex + 1,
              pos);
          xmlw.write(value);
          pos.write(close.buffer, close.start, close.length);
        }
      }
      else if (torendero.getClass() == UIOutputMultiline.class) {
        StringList value = ((UIOutputMultiline) torendero).value;
        if (value == null) {
          RenderUtil.dumpTillLump(lumps, lumpindex + 1, close.lumpindex + 1,
              pos);
        }
        else {
          RenderUtil.dumpTillLump(lumps, lumpindex + 1, endopen.lumpindex + 1,
              pos);
          for (int i = 0; i < value.size(); ++ i) {
            if (i != 0) {
              pos.print("<br/>");
            }
            xmlw.write(value.stringAt(i));
          }
          pos.write(close.buffer, close.start, close.length);
        }
      }
      else if (torendero instanceof UIInputBase) {
        UIInputBase torender = (UIInputBase) torendero;
        attrcopy.put("name", fullID);
        //attrcopy.put("id", fullID);
        String value = "";
        String body = null;
        if (torendero instanceof UIInput) {
          value = ((UIInput) torender).value;
          if (uselump.textEquals("<textarea ")) {
            body = value;
          }
          else {
            attrcopy.put("value", value);
          }

        }
        else if (torendero instanceof UISelectBoolean) {
          if (((UISelectBoolean) torender).value) {
            attrcopy.put("checked", "yes");
            value = "true";
          }
          else {
            value = "false";
          }
          // eh? What is the "value" attribute for one of these?
          attrcopy.put("value", "true");
        }

        RenderUtil.dumpAttributes(attrcopy, xmlw);
        pos.print(">");
        if (body != null) {
          xmlw.write(body);
          pos.write(close.buffer, close.start, close.length);
        }
        else {
          RenderUtil.dumpTillLump(lumps, endopen.lumpindex + 1,
              close.lumpindex + 1, pos);
        }
        // unify hidden field processing? ANY parameter children found must
        // be dumped as hidden fields.
        // NB this is the WRONG ID, we need to put the GLOBAL ID here.
//        RenderUtil.dumpHiddenField(torender.ID
//            + SubmittedValueEntry.FOSSIL_SUFFIX, torender.valuebinding + value,
//            pos);
        // this is not done in makeField, call to setFossilisedBinding.
      }
      else if (torendero instanceof UILink) {
        UILink torender = (UILink) torendero;

        attrcopy.put("href", torender.target);
        RenderUtil.dumpAttributes(attrcopy, xmlw);
        pos.print(">");
        if (torender.text != null) {
          xmlw.write(torender.text);
          pos.write(close.buffer, close.start, close.length);
        }
        else {
          RenderUtil.dumpTillLump(lumps, endopen.lumpindex + 1,
              close.lumpindex + 1, pos);
        }
      }
      else if (torendero instanceof UICommand) {
        UICommand torender = (UICommand) torendero;
        String value = RenderUtil.makeURLAttributes(torender.commandparams);
        // any desired "attributes" decoded for JUST THIS ACTION must be
        // secretly
        // bundled as this special attribute.
        attrcopy.put("name", SubmittedValueEntry.COMMAND_LINK_PARAMETERS + value);
        if (lump.textEquals("<input ") && torender.text != null) {
          attrcopy.put("value", torender.text);
        }

        RenderUtil.dumpAttributes(attrcopy, xmlw);
        if (endopen.lumpindex == close.lumpindex) {
          pos.print("/>");
        }
        else {
          pos.print(">");
          if (torender.text != null && lump.textEquals("<button ")) {
            xmlw.write(torender.text);
            pos.write(close.buffer, close.start, close.length);
          }
          else {
            RenderUtil.dumpTillLump(lumps, endopen.lumpindex + 1,
                close.lumpindex + 1, pos); 
          }
        }
        // RenderUtil.dumpHiddenField(SubmittedValueEntry.ACTION_METHOD,
        // torender.actionhandler, pos);
      }
      // Forms are a total arse. We decided that i) they will NOT APPEAR IN
      // THE CONTAINER HIERARCHY! So in which case how will they be located?
      // see iv) for this. ii) every form WILL HAVE
      // AN ID, even if it is almost always BASIC_FORM. iii) since it has an
      // ID, the default scanner will always stop on it. Therefore iv)
      // forms WILL appear in the childmap, since we need to allow multiple
      // forms
      // covering different child sets in a domain.
      else if (torendero instanceof UIForm) {
        UIForm torender = (UIForm) torendero;
        attrcopy.put("method", "post"); // yes, we MEAN this!
        int qpos = torender.postURL.indexOf('?');
        // guard against possibility of parameters coming through twice.
        attrcopy.put("action", qpos == -1? torender.postURL: torender.postURL.substring(0, qpos));
        RenderUtil.dumpAttributes(attrcopy, xmlw);
        pos.println(">");
        for (Iterator fit = torender.hiddenfields.keySet().iterator(); fit
            .hasNext();) {
          String name = (String) fit.next();
          String value = (String) torender.hiddenfields.get(name);
          RenderUtil.dumpHiddenField(name, value, xmlw);
        }
        // override "nextpos" - form is expected to contain numerous nested
        // Components.
        // this is the only ANOMALY!! Forms together with payload cannot work.
        // the fact we are at the wrong recursion level will "come out in the
        // wash"
        // since we must return to the base recursion level before we exit this
        // domain.
        // Assuming there are no paths *IN* through forms that do not also lead
        // *OUT* there will be no problem. Check what this *MEANS* tomorrow.
        nextpos = endopen.lumpindex + 1;
      }
      // if there is a payload, dump the postamble.
      if (payload != null) {
        RenderUtil.dumpTillLump(lumps, close.lumpindex + 1,
            outerclose.lumpindex + 1, pos);
      }
    }

    return nextpos;
  }

  

}
