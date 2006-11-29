/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.request;

import uk.org.ponder.conversion.ConvertUtil;
import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UIDeletionBinding;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.rsf.uitype.UIType;
import uk.org.ponder.rsf.uitype.UITypes;
import uk.org.ponder.saxalizer.SAXalXMLProvider;

/*
 * Manages the (to some extent RenderSystem dependent) process of converting
 * bindings (of three types - fossilized, deletion and pure EL) into String
 * key/value pairs suitable for transit over HTTP.
 * 
 * <p>In case of an HTTP submission, these are encoded as key/value in the
 * request map (via hidden form fields) as follows: <br>key =
 * componentid-fossil, value=[i|o]uitype-name#{bean.member}oldvalue <br>Alternatively,
 * this SVE may represent a "fast EL" binding, without a component. In this
 * case, it has the form <br>key = [deletion|el]-binding, value =
 * [e|o]#{el.lvalue}rvalue, where rvalue may represent an EL rvalue, a
 * SAXLeafType or a Object. <br>The actual value submission is encoded in the
 * RenderSystem for UIInputBase, but is generally expected to simply follow <br>key =
 * componentid, value = newvalue.
 */

public class FossilizedConverter {
  public static final char INPUT_COMPONENT = 'i';
  public static final char INPUT_COMPONENT_MUSTAPPLY = 'j';
  public static final char OUTPUT_COMPONENT = 'o';
  public static final char EL_BINDING = 'e';
  public static final char OBJECT_BINDING = 'o';
  /**
   * The suffix appended to the component fullID in order to derive the key for
   * its corresponding fossilized binding.
   */
  public static final String FOSSIL_SUFFIX = "-fossil";
  /**
   * A suffix to be used for the "componentless" bindings defined by
   * UIDeletionBinding and UIELBinding.
   */
  public static final String BINDING_SUFFIX = "-binding";
  public static final String RESHAPER_SUFFIX = "-reshaper";
  public static final String DELETION_KEY = "deletion" + BINDING_SUFFIX;
  public static final String VALUE_DELETION_KEY = "valuedeletion"
      + BINDING_SUFFIX;
  public static final String ELBINDING_KEY = "el" + BINDING_SUFFIX;
  public static final String VIRTUAL_ELBINDING_KEY = "virtual-" + ELBINDING_KEY;

  private SAXalXMLProvider xmlprovider;

  public static final String COMMAND_LINK_PARAMETERS = "command link parameters";

  public void setSAXalXMLProvider(SAXalXMLProvider xmlprovider) {
    this.xmlprovider = xmlprovider;
  }

  /**
   * A utility method to determine whether a given key (from the request map)
   * represents a Fossilised binding, i.e. it ends with the suffix
   * {@link #FOSSIL_SUFFIX}.
   */
  public boolean isFossilisedBinding(String key) {
    return key.endsWith(FOSSIL_SUFFIX);
  }

  public boolean isNonComponentBinding(String key) {
    // TODO: After 0.7.0 reform the bindings encoding system so that
    // virtual bindings can be nameless.
    return key.endsWith(BINDING_SUFFIX) && !key.equals(VIRTUAL_ELBINDING_KEY);
  }

  /** Parse a "non-component binding" key/value pair * */
  public SubmittedValueEntry parseBinding(String key, String value) {
    SubmittedValueEntry togo = new SubmittedValueEntry();
    togo.isEL = value.charAt(0) == EL_BINDING;
    int endcurly = findEndCurly(value);
    togo.valuebinding = value.substring(3, endcurly);
    togo.newvalue = value.substring(endcurly + 1);
    if (key.equals(DELETION_KEY)) {
      togo.isdeletion = true;
      togo.newvalue = null;
    }
    else if (key.equals(VALUE_DELETION_KEY)) {
      togo.isdeletion = true;
    }
    // such a binding will hit the data model via the RSVCApplier.
    return togo;
  }

  /**
   * Attempts to construct a SubmittedValueEntry on a key/value pair found in
   * the request map, for which isFossilisedBinding has already returned
   * <code>true</code>. In order to complete this (non-deletion) entry, the
   * <code>newvalue</code> field must be set separately.
   * 
   * @param key
   * @param value
   */
  public SubmittedValueEntry parseFossil(String key, String value) {
    SubmittedValueEntry togo = new SubmittedValueEntry();

    togo.mustapply = value.charAt(0) == INPUT_COMPONENT_MUSTAPPLY;
    int firsthash = value.indexOf('#');
    String uitypename = value.substring(1, firsthash);
    int endcurly = findEndCurly(value);
    togo.valuebinding = value.substring(firsthash + 2, endcurly);
    String oldvaluestring = value.substring(endcurly + 1);

    UIType uitype = UITypes.forName(uitypename);
    if (oldvaluestring.length() > 0) {
      Class uiclass = uitype == null ? null
          : uitype.getPlaceholder().getClass();
      togo.oldvalue = ConvertUtil.parse(oldvaluestring, xmlprovider, uiclass);
    }
    else {
      // must ensure that we record the TYPE of oldvalue here for later use by
      // fixupNewValue, even if the oldvalue is empty.
      togo.oldvalue = uitype.getPlaceholder();
    }

    togo.componentid = key.substring(0, key.length() - FOSSIL_SUFFIX.length());

    return togo;
  }

  private int findEndCurly(String value) {
    for (int i = 0; i < value.length(); ++i) {
      char c = value.charAt(i);
      if (c == '}' && (i == 0 || value.charAt(i - 1) != '\\'))
        return i;
    }
    return -1;
  }

  public String computeBindingValue(String lvalue, Object rvalue) {
    if (rvalue instanceof ELReference) {
      ELReference elref = (ELReference) rvalue;
      return EL_BINDING + "#{" + lvalue + "}" + elref.value;
    }
    else {
      // The value type will be inferred on delivery, for Object bindings.
      return OBJECT_BINDING + "#{" + lvalue + "}" + (rvalue == null ? ""
          : ConvertUtil.render(rvalue, xmlprovider));
    }
  }

  public void computeDeletionBinding(UIDeletionBinding binding) {
    if (binding.deletetarget == null) {
      binding.name = DELETION_KEY;
      binding.value = OBJECT_BINDING + "#{" + binding.deletebinding.value + "}";
    }
    else {
      binding.name = VALUE_DELETION_KEY;
      binding.value = computeBindingValue(binding.deletebinding.value,
          binding.deletetarget);
    }
  }

  public void computeELBinding(UIELBinding binding) {
    binding.name = binding.virtual ? VIRTUAL_ELBINDING_KEY : ELBINDING_KEY;
    binding.value = computeBindingValue(binding.valuebinding.value,
        binding.rvalue);
  }

  /**
   * Computes the fossilised binding parameter that needs to be added to forms
   * for which the supplied UIBound is a submitting control. The value of the
   * bound component is one of the (three) UITypes, or else an unknown non-leaf
   * type. This value will be serialized and added to the end of the binding.
   */
  // NB! UIType is hardwired to use the static StringArrayParser, to avoid a
  // wireup graph cycle of FossilizedConverter on the HTMLRenderSystem.
  public UIParameter computeFossilizedBinding(UIBound togenerate,
      Object modelvalue) {
    if (!togenerate.fossilize) {
      throw new IllegalArgumentException("Cannot compute fossilized binding "
          + "for non-fossilizing component with ID " + togenerate.getFullID());
    }
    UIParameter togo = new UIParameter();
    togo.name = togenerate.submittingname + FOSSIL_SUFFIX;
    String oldvaluestring = null;
    Object oldvalue = modelvalue == null ? togenerate.acquireValue()
        : modelvalue;
    if (oldvalue == null) {
      throw new IllegalArgumentException(
          "Error: cannot compute fossilized binding for component with full ID "
              + togenerate.getFullID() + " since bound value is null");

    }
    UIType type = UITypes.forObject(oldvalue);

    if (type != null) {
      // don't try to write as a leaf type, since the the parser (above) will
      // not have enough context to infer the type above.
      oldvaluestring = xmlprovider.getMappingContext().saxleafparser
          .render(oldvalue);
    }
    else {
      oldvaluestring = xmlprovider.toString(oldvalue);
    }
    String typestring = type == null ? ""
        : type.getName();
    togo.value = (togenerate.mustapply ? INPUT_COMPONENT_MUSTAPPLY
        : (togenerate.willinput ? INPUT_COMPONENT
            : OUTPUT_COMPONENT)) + typestring + "#{"
        + togenerate.valuebinding.value + "}" + oldvaluestring;
    return togo;
  }

  public String getReshaperKey(String componentid) {
    return componentid + RESHAPER_SUFFIX;
  }

  public UIParameter computeReshaperBinding(UIBound togenerate) {
    if (togenerate.darreshaper == null) {
      return null;
    }
    else {
      UIParameter togo = new UIParameter();
      togo.name = getReshaperKey(togenerate.submittingname);
      togo.value = togenerate.darreshaper.value;
      return togo;
    }
  }

  /**
   * Fixes up the supplied "new value" relative to information discovered in the
   * fossilized binding. After this point, newvalue is authoritatively not null
   * for every submission where a component which was marked as "expecting
   * input" SHOULD be receiving data, and of the same type as oldvalue.
   * <p>
   * In the case of an already "erroneous fossilization" where oldvalue was
   * itself empty/null, it is possible that no submission returns, and none is
   * actually possible (e.g. selection controls which have no parent choices).
   * In this case newvalue will remain null (semantically null rather than
   * submittedly null).
   * <p>
   * Note that oldvalue is now always not null here.
   * 
   * @param value The value assigned to the fossilized binding
   */
  public void fixupNewValue(SubmittedValueEntry sve,
      RenderSystemDecoder rendersystemstatic, String key, String value) {
    char typechar = value.charAt(0);
    if (typechar == INPUT_COMPONENT || typechar == INPUT_COMPONENT_MUSTAPPLY) {
      rendersystemstatic.fixupUIType(sve);
      Class requiredclass = sve.oldvalue.getClass();
      if (sve.newvalue != null && sve.newvalue.getClass() != requiredclass) {
        // no attempt to catch the exceptions from the next two lines since they
        // all represent assertion errors.
        String[] newvalues = (String[]) sve.newvalue;
        sve.newvalue = xmlprovider.getMappingContext().saxleafparser.parse(
            requiredclass, newvalues[0]);
        // The only non-erroneous case here is where newvalue is String[], and
        // oldvalue is some scalar type. Should new UITypes arise, this will
        // need to be reviewed.
      }

    }

  }

}
