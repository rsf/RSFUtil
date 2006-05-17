/*
 * Created on May 16, 2006
 */
package uk.org.ponder.rsf.components.decorators;

/** A lightweight aggregative "decorator" attached to a UIComponent via its
 * <code>decorators</code> list. In general a decorator has a rendering effect
 * caused by manipulating the attributes of the peering XML tag, as they pass
 * from template to rendered output.
 * 
 * The attribute list will be applied <i>on top of</i> attributes inherited
 * from the template, but will be <i>overwritten by</i> any component-specific
 * attributes applied by the renderer for the component to which this decorator
 * is attached.
 * 
 * Template attrs -- &gt; decorators -- &gt; component renderers -- &gt; rendered output.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIDecorator {

}
