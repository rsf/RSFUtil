/*
 * Created on Nov 25, 2005
 */
package uk.org.ponder.rsf.expander;

import java.util.Enumeration;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.mapping.DARApplier;
import uk.org.ponder.reflect.DeepBeanCloner;
import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.rsf.components.ComponentList;
import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIReplicator;
import uk.org.ponder.saxalizer.MethodAnalyser;
import uk.org.ponder.saxalizer.SAXAccessMethod;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.EnumerationConverter;

/** Expands a "proto-template" file as read off disk, cloning all members into
 * non-shared state ready for fixups, in addition removing by expansion all
 * non-component elements such as UIReplicator and UISwitch.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class TemplateExpander {
  private DARApplier darapplier;
//  private BeanLocator rbl;
  private DeepBeanCloner deepcloner;
  private RSACBeanLocator rsacbeanlocator;

  public void setDARApplier(DARApplier darapplier) {
    this.darapplier = darapplier;
  }
// This is sadly a request-scope bean - if we wired it directly, the entire
// tree becomes request scope. We have to use the RSACBeanLocator itself.
  public void setSafeBeanLocator(BeanLocator beanlocator) {
    //this.rbl = beanlocator;
  }

  private BeanLocator getSafeLocator() {
    // Is there some Spring proxy magic coming that can help with this?
    
    return (BeanLocator) rsacbeanlocator.getBeanLocator().locateBean("rsacsafebeanlocator");
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
      stump = BeanUtil.stripEL(replicator.valuebinding);
    }
    else {
      IDRemapStrategy remapstrategy = (IDRemapStrategy) replicator.idstrategy;
      stump = BeanUtil.stripEL(remapstrategy.basepath);
    }
    return stump;
  }

  private static class RemapState {
    public String localid;
    public String stump;

    public RemapState(String localid, String stump) {
      this.localid = localid;
      this.stump = stump;
    }
  }

  public void expandTemplate(UIContainer target, UIContainer source) {
    expandTemplate(target, source, null);
  }
  
  private static StringList cloneexcept = new StringList();
  static {
    cloneexcept.add("component");
  }
  
  public void expandTemplate(UIContainer target, UIContainer source,
      RemapState state) {
    ComponentList children = source.flattenChildren();
    for (int i = 0; i < children.size(); ++i) {
      UIComponent child = (UIComponent) children.get(i);
      if (state != null) {
        if (child instanceof UIBound) {
          UIBound bound = (UIBound) child;
          UIBound copy = (UIBound) deepcloner.cloneBean(bound);
          String stripped = BeanUtil.stripEL(copy.valuebinding);
          if (stripped.startsWith(UIReplicator.LOCALID_WILDCARD)) {
            stripped = state.stump
                + "."
                + state.localid
                + stripped.substring(UIReplicator.LOCALID_WILDCARD
                    .length());
            copy.valuebinding = "#{" + stripped + "}";
          }
          target.addComponent(copy);
        }
      }
      else if (child instanceof UIReplicator) {
        expandReplicator(target, (UIReplicator) child, state);
      }
      else if (!(child instanceof UIContainer)) {
        UIComponent clonechild = (UIComponent) deepcloner.cloneBean(child);
        target.addComponent(clonechild);
      }
      else {
        UIContainer container = (UIContainer) deepcloner.cloneBean(child, cloneexcept);        
        target.addComponent(container);
        expandTemplate(container, (UIContainer) child, state);
      }
    }
  }

  /**
   * Expands the supplied replicator, encountered as a roadblock in an expanding
   * prototemplate, into the target branch container in the accreting true
   * template.
   * 
   * @param target
   *          The branch container which will receive replicated instances of
   *          the replicators container as replicated children.
   */
  public void expandReplicator(UIContainer target, UIReplicator replicator,
      RemapState state) {
    BeanLocator safelocator = getSafeLocator();
    // TODO: work out how to remap recursively - currently old remapstate is
    // thrown away.
    String listbinding = BeanUtil.stripEL(replicator.valuebinding);
    Object collection = darapplier.getBeanValue(listbinding, safelocator);
    int index = 0;
    String value = replicator.valuebinding;
    for (Enumeration colit = EnumerationConverter.getEnumeration(collection); colit
        .hasMoreElements();) {
      Object bean = colit.nextElement();
      UIBranchContainer replicated = UIBranchContainer.make(target,
          replicator.component.ID);
      String localid = computeLocalID(bean, replicator.idstrategy, index);
      replicated.localID = localid;
      String stump = computeStump(replicator);
      RemapState newstate = new RemapState(localid, stump);

      expandTemplate(replicated, replicator.component, newstate);
      ++index;
    }
  
  }

}
