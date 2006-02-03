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
import uk.org.ponder.rsf.viewstate.EntityCentredViewParameters;
import uk.org.ponder.saxalizer.MethodAnalyser;
import uk.org.ponder.saxalizer.SAXAccessMethod;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.EnumerationConverter;

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
      MethodAnalyser ma = MethodAnalyser.getMethodAnalyser(bean, darapplier
          .getMappingContext());
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

  private static StringList cloneexcept = new StringList();
  static {
    cloneexcept.add("component");
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

  /**
   * @param target A "to be live" container to receiver cloned (expanded)
   *          children correponding to child and any descendents.
   * @param child A "template" component which is to be expanded into children
   *          of the target.
   * @param state The current remapping state.
   */
  private void expandComponent(UIContainer target, UIComponent child,
      RemapState state) {

    if (child instanceof UIBound) {
      UIBound bound = (UIBound) child;
      UIBound copy = (UIBound) deepcloner.cloneBean(bound);
      rewritePossibleELRef(copy.valuebinding, state);
      target.addComponent(copy);
    }

    // We *do* have the reflective power to avoid this special-casing, but
    // it is unknown at this point what the performance/flexibility tradeoffs
    // are.
    else if (child instanceof UIInternalLink) {
      UIInternalLink childlink = (UIInternalLink) child;

      UIInternalLink cloned = (UIInternalLink) deepcloner.cloneBean(child);
      if (childlink.viewparams instanceof EntityCentredViewParameters
          && state != null) {

        EntityCentredViewParameters ecvp = (EntityCentredViewParameters) cloned.viewparams;
        if (ecvp.entity.ID.equals(state.idwildcard)) {
          ecvp.entity.ID = state.localid;
        }
      }
      target.addComponent(cloned);
    }
    else if (child instanceof UIReplicator) {
      expandReplicator(target, (UIReplicator) child, state);
    }
    else if (child instanceof UISwitch) {
      expandSwitch(target, (UISwitch) child, state);
    }
    else if (!(child instanceof UIContainer)) {
      UIComponent clonechild = (UIComponent) deepcloner.cloneBean(child);
      target.addComponent(clonechild);

    }
    else {
      UIContainer container = (UIContainer) deepcloner.cloneBean(child,
          cloneexcept);
      rewriteParameterList(container.parameters, state);
      target.addComponent(container);
      expandContainer(container, (UIContainer) child, state);
    }
  }

  // the source is interpreted as DIRECTLY CORRESPONDING to the target, as
  // opposed to expandComponent, where the source will become a CHILD of the
  // target.
  private void expandContainer(UIContainer target, UIContainer source,
      RemapState state) {
    ComponentList children = source.flattenChildren();
    for (int i = 0; i < children.size(); ++i) {
      UIComponent child = (UIComponent) children.get(i);
      expandComponent(target, child, state);
    }
  }

  private void expandSwitch(UIContainer target, UISwitch switch1,
      RemapState state) {
    BeanLocator beanlocator = rsacbeanlocator.getBeanLocator();
    Object lvalue = switch1.lvalue;
    if (lvalue instanceof ELReference) {
      lvalue = darapplier.getBeanValue(((ELReference) lvalue).value,
          beanlocator);
    }
    Object rvalue = switch1.rvalue;
    if (rvalue instanceof ELReference) {
      rvalue = darapplier.getBeanValue(((ELReference) rvalue).value,
          beanlocator);
    }
    UIComponent toadd = lvalue.equals(rvalue) ? switch1.truecomponent
        : switch1.falsecomponent;
    expandComponent(target, toadd, state);
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
        UIBranchContainer replicated = UIBranchContainer.make(target,
            replicator.component.ID);
        replicated.localID = localid;
        expandtarget = replicated;
      }
      String stump = computeStump(replicator);
      RemapState newstate = new RemapState(localid, stump,
          replicator.idwildcard);

      expandContainer(expandtarget, replicator.component, newstate);
      ++index;
    }
  }

  public void expandTemplate(UIContainer target, UIContainer source) {
    expandContainer(target, source, null);
  }

}
