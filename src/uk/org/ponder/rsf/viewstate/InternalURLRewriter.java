/*
 * Created on 10 Aug 2006
 */
package uk.org.ponder.rsf.viewstate;

/** Supplies URL rewriting services for "internal" URLs (i.e. directed 
 * at application views) but which are not participating in the 
 * ViewParameters system. For example UIInternalLink components which have
 * specified a raw link, or UIForms. In "normal" servlet environments, these
 * will be no-op operations. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface InternalURLRewriter {
  public String rewriteRenderURL(String original);
  public String rewriteActionURL(String original);
}
