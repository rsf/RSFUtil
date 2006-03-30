/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.mappings;

import uk.org.ponder.conversion.DateParser;
import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.ELReferenceParser;
import uk.org.ponder.rsf.components.UIAnchor;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIDeletionBinding;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIError;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInputMany;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIOutputMany;
import uk.org.ponder.rsf.components.UIOutputMultiline;
import uk.org.ponder.rsf.components.UIReplicator;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISimpleContainer;
import uk.org.ponder.rsf.components.UISwitch;
import uk.org.ponder.rsf.expander.DirectIndexStrategy;
import uk.org.ponder.rsf.expander.IDRemapStrategy;
import uk.org.ponder.rsf.flow.StaticActionErrorStrategy;
import uk.org.ponder.rsf.view.ViewRoot;
import uk.org.ponder.rsf.viewstate.EntityCentredViewParameters;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsLeafParser;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;
import uk.org.ponder.saxalizer.mapping.MappableXMLProvider;
import uk.org.ponder.saxalizer.mapping.MappingLoadManager;
import uk.org.ponder.saxalizer.mapping.MappingLoader;

/** This somewhat "kitchen-sink" class is the central point of coordination for
 * all (XML) mapping files required by RSF.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class RSFMappingLoader implements MappingLoader {
  public static final Class[] allBuiltin = new Class[] {
    UICommand.class, UIForm.class, UIInput.class, UIInternalLink.class,
    UILink.class, UIMessage.class, UIOutput.class, UIOutputMultiline.class,
    UIBranchContainer.class, UISimpleContainer.class, UIELBinding.class,
    UIDeletionBinding.class, UIReplicator.class, UISwitch.class, UISelect.class,
    UIInputMany.class, UIOutputMany.class, UIError.class, UIAnchor.class
  };
  private ViewParamsLeafParser viewparamsleafparser;
  
  // do this in a somewhat ad hoc way, since leafparsers do not currently
  // advertise their parsed types.
  public void setViewParamsLeafParser(ViewParamsLeafParser viewparamsleafparser) {
    this.viewparamsleafparser = viewparamsleafparser;
  }
  
  public void loadExtendedMappings(SAXalizerMappingContext context) {
    context.setChainedInferrer(new ComponentMapperInferrer());
    for (int i = 0; i < allBuiltin.length; ++ i) {
      Class clazz = allBuiltin[i];
      String classname = clazz.getName();
      int lastdotpos = classname.lastIndexOf('.');
      // the nickname is the lowercase form of the text following "UI" in the classname.
      String nick = classname.substring(lastdotpos + 3).toLowerCase();
      context.classnamemanager.registerClass(nick, clazz);
    }
    context.classnamemanager.registerClass("directindex", DirectIndexStrategy.class);
    context.classnamemanager.registerClass("idremap", IDRemapStrategy.class);
    context.classnamemanager.registerClass("staticstrategy", StaticActionErrorStrategy.class);
    context.classnamemanager.registerClass("entitycentred", EntityCentredViewParameters.class);
    context.classnamemanager.registerClass("simple", SimpleViewParameters.class);
    
    context.classnamemanager.registerClass("elref", ELReference.class);
    context.classnamemanager.registerClass("dateparser", DateParser.class);
    
    context.classnamemanager.registerClass("view", ViewRoot.class);
    
    context.saxleafparser.registerParser(ViewParameters.class, viewparamsleafparser);
    context.saxleafparser.registerParser(ELReference.class, new ELReferenceParser());
  }

  public void loadStandardMappings(MappableXMLProvider xmlprovider) {
    // mappings for flow-lite
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/flow-map.xml");
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/actionstate-map.xml");
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/viewstate-map.xml");
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/endstate-map.xml");
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/action-map.xml");
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/transition-map.xml");
    // mappings for JSFNav
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/navigation-map-map.xml");
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/navigation-case-map.xml");
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/navigation-rule-map.xml");
    // mappings for components
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/viewroot-map.xml");
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/branchcontainer-map.xml");
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/simplecontainer-map.xml");
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/parameter-map.xml");
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/idremapstrategy-map.xml");
    // mapping for EntityID - should really be in PonderUtilCore
    MappingLoadManager.loadClasspathMapping(xmlprovider, 
    "uk/org/ponder/rsf/mappings/entityid-map.xml");
    
  }

}
