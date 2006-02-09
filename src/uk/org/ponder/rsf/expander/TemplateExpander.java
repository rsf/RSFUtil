/*
 * Created on Nov 25, 2005
 */
package uk.org.ponder.rsf.expander;

import java.util.Enumeration;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.mapping.DARApplier;
import uk.org.ponder.reflect.DeepBeanCloner;
import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.rsf.components.ComponentList;
import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIDeletionBinding;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.rsf.components.UIReplicator;
import uk.org.ponder.rsf.components.UISwitch;
import uk.org.ponder.rsf.uitype.UITypes;
import uk.org.ponder.rsf.viewstate.EntityCentredViewParameters;
import uk.org.ponder.saxalizer.MethodAnalyser;
import uk.org.ponder.saxalizer.SAXAccessMethod;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.EnumerationConverter;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * Expands a "proto-template" file as read off disk, cloning all members into
 * non-shared state ready for fixups, in addition removing by expansion all
 * non-component elements such as UIReplicator and UISwitch.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class TemplateExpander {
  private DARApplier darapplier;
  // private BeanLocator rbl;
  private DeepBeanCloner deepcloner;
  private RSACBeanLocator rsacbeanlocator;

  public void setDARApplier(DARApplier darapplier) {
    this.darapplier = darapplier;
  }

  // This is a request-scope bean until we move all view producers into request
  // scope.
  public void setSafeBeanLocator(BeanLocator beanlocator) {
    // this.rbl = beanlocator;
  }

  private BeanLocator getSafeLocator() {
    return (BeanLocator) rsacbeanlocator.getBeanLocator().locateBean(
        "rsacsafebeanlocator");
  }

  public void setDeepBeanCloner(DeepBeanCloner deepcloner) {
    this.deepcloner = deepcloner;
  }

  public void setRSACBeanLocator(RSACBeanLocator rsacbeanlocator) {
    this.rsacbeanlocator = rsacbeanlocator;
  }

  private String computeLocalID(Object bean, Object idstrategy, int index) {
    String localid = null;
    if (idstrategy instanceof DirectIndexStrategy) {
      localid = Integer.toString(index);
    }
    else {
      IDRemapStrategy remapstrategy = (IDRemapStrategy) idstrategy;
      MethodAnalyser ma = darapplier.getMappingContext().getAnalyser(
          bean.getClass());
      SAXAccessMethod sam = ma.getAccessMethod(remapstrategy.idfield);
      localid = sam.getChildObject(bean).toString();
    }
    return localid;
  }

  private String computeStump(UIReplicator replicator) {
    String stump = null;
    if (replicator.idstrategy instanceof DirectIndexStrategy) {
      stump = replicator.valuebinding.value;
    }
    else {
      IDRemapStrategy remapstrategy = (IDRemapStrategy) replicator.idstrategy;
      stump = remapstrategy.basepath.value;
    }
    return stump;
  }

  private static class RemapState {
    public String idwildcard;
    public String localid;
    public String stump;

    public RemapState(String localid, String stump, String idwildcard) {
      this.localid = localid;
      this.stump = stump;
      this.idwildcard = idwildcard;
    }
  }

  private static void rewritePossibleELRef(Object elrefo, RemapState state) {
    if (elrefo instanceof ELReference && state != null) {
      ELReference elref = (ELReference) elrefo;
      if (elref.value.startsWith(state.idwildcard)) {
        elref.value = state.stump + "." + state.localid
            + elref.value.substring(state.idwildcard.length());
      }
    }
  }

  private static void rewriteParameterList(ParameterList parameters,
      RemapState state) {
    for (int j = 0; j < parameters.size(); ++j) {
      UIParameter param = parameters.parameterAt(j);
      if (param instanceof UIELBinding) {
        UIELBinding elbinding = (UIELBinding) param;
        rewritePossibleELRef(elbinding.valuebinding, state);
        rewritePossibleELRef(elbinding.rvalue, state);
      }
      else if (param instanceof UIDeletionBinding) {
        rewritePossibleELRef(((UIDeletionBinding) param).deletebinding, state);
      }
    }
  }


  private UIComponent resolveSwitch(UISwitch switchh, RemapState state) {
    BeanLocator beanlocator = rsacbeanlocator.getBeanLocator();
    Object lvalue = switchh.lvalue;
    if (lvalue instanceof ELReference) {
      lvalue = darapplier.getBeanValue(((ELReference) lvalue).value,
          beanlocator);
    }
    Object rvalue = switchh.rvalue;
    if (rvalue instanceof ELReference) {
      rvalue = darapplier.getBeanValue(((ELReference) rvalue).value,
          beanlocator);
    }
    UIComponent toadd = lvalue.equals(rvalue) ? switchh.truecomponent
        : switchh.falsecomponent;
    return toadd;
  }
  
  /**
   * @param target A "to be live" container to receiver cloned (expanded)
   *          children correponding to child and any descendents.
   * @param toclone A "template" component which is to be expanded into children
   *          of the target.
   * @param state The current remapping state.
   */
  private UIComponent cloneComponent(UIComponent toclone, RemapState state) {
    if (toclone instanceof UISwitch) {
      UIComponent resolved = resolveSwitch((UISwitch) toclone, state);
      return cloneComponent(resolved, state);
    }
    else {
      MethodAnalyser ma = darapplier.getMappingContext().getAnalyser(
          toclone.getClass());
      UIComponent cloned = (UIComponent) deepcloner.emptyClone(toclone);
      for (int i = 0; i < ma.allgetters.length; ++i) {
        SAXAccessMethod sam = ma.allgetters[i];
        if (!sam.canGet() || !sam.canSet())
          continue;
        if (sam.tagname.equals("parent")) {
          continue;
        }
        if (toclone instanceof UIBound && sam.tagname.equals("value")) {
          UIBound bound = (UIBound) toclone;
          UIBound copy = (UIBound) cloned;
          // use care when copying bound VALUE since placeholder values operate
          // object handle identity semantics
          if (UITypes.isPlaceholder(bound.acquireValue())) {
            copy.updateValue(bound.acquireValue());
          }
          else {
            Object valuecopy = deepcloner.cloneBean(bound.acquireValue());
            copy.updateValue(valuecopy);
          }
          continue;
        }

        Object child = sam.getChildObject(toclone);
        Object clonechild = null;

        if (child instanceof UIComponent) {
          UIComponent childcomp = (UIComponent) child;
          if (child instanceof UIReplicator
              && !(toclone instanceof UIContainer)) {
            throw UniversalRuntimeException.accumulate(
                new IllegalArgumentException(), "UIReplicator " + childcomp.ID
                    + " must have a parent which is a container - in fact "
                    + toclone.getClass());
          }
          clonechild = cloneComponent(childcomp, state);
        }
        else {
          // non-component child can be cloned normally
          clonechild = deepcloner.cloneBean(child);
        }
        if (state != null) {
          if (clonechild instanceof ELReference) {
            rewritePossibleELRef(clonechild, state);
          }
          if (clonechild instanceof ParameterList) {
            rewriteParameterList((ParameterList) clonechild, state);
          }
          else if (clonechild instanceof EntityCentredViewParameters) {
            EntityCentredViewParameters ecvp = (EntityCentredViewParameters) clonechild;
            if (ecvp.entity.ID.equals(state.idwildcard)) {
              ecvp.entity.ID = state.localid;
            }
          }
        }
        sam.setChildObject(cloned, clonechild);
      }
      // there is a "leaf component tree" here, which may include a UIBound.
      if (toclone instanceof UIContainer) {
        cloneChildren((UIContainer) cloned, (UIContainer) toclone, state);
      }
      return cloned;
    }
  }

  // Clone all the children of the "source" container into children
  // of the target.
  private void cloneChildren(UIContainer target, UIContainer source,
      RemapState state) {
    ComponentList children = source.flattenChildren();
    for (int i = 0; i < children.size(); ++i) {
      UIComponent child = (UIComponent) children.get(i);
      if (child instanceof UIReplicator) {
        expandReplicator(target, (UIReplicator) child, state);
      }
      else {
        UIComponent cloned = cloneComponent(child, state);
        target.addComponent(cloned);
      }

    }
  }

  /**
   * Expands the supplied replicator, encountered as a roadblock in an expanding
   * prototemplate, into the target branch container in the accreting true
   * template.
   * 
   * @param target The branch container which will receive replicated instances
   *          of the replicator's container as replicated children.
   */
  private void expandReplicator(UIContainer target, UIReplicator replicator,
      RemapState state) {
    BeanLocator safelocator = getSafeLocator();
    // TODO: work out how to remap recursively - currently old remapstate is
    // thrown away.
    String listbinding = replicator.valuebinding.value;
    Object collection = darapplier.getBeanValue(listbinding, safelocator);
    int index = 0;
    // for each member of the object "list", instantiate a BranchContainer with
    // corresponding localID, and then recurse further.
    for (Enumeration colit = EnumerationConverter.getEnumeration(collection); colit
        .hasMoreElements();) {
      Object bean = colit.nextElement();
      String localid = computeLocalID(bean, replicator.idstrategy, index);
      UIContainer expandtarget = target;

      if (!replicator.elideparent) {
        UIBranchContainer replicated = new UIBranchContainer();
        replicated.ID = replicator.component.ID;
        replicated.localID = localid;
        expandtarget = replicated;
      }
      String stump = computeStump(replicator);
      RemapState newstate = new RemapState(localid, stump,
          replicator.idwildcard);

      cloneChildren(expandtarget, replicator.component, newstate);
      ++index;
    }
  }

  public void expandTemplate(UIContainer target, UIContainer source) {
    cloneChildren(target, source, null);
  }

}
