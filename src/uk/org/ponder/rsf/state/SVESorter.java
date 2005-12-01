/*
 * Created on Nov 9, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import uk.org.ponder.beanutil.BeanUtil;

/** Applies a topological sorting to a collection of SubmittedValueEntry
 * objects, such that any read or write of an entry of the bean model is 
 * scheduled after a write of a dependency. A dependency of an EL operation
 * is a write to a path equal or higher to it in the bean hierarchy. Writes
 * to the same path will currently be scheduled arbitrarily.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class SVESorter {
// Notes on the topological sorting applied by this class.

//Each entry is a WRITE to the EL path "valuebinding".
//In addition, a "fast EL" may represent a "read" from path newvalue,
//if newvalue represents an EL.
//Component binding: will WRITE to valuebinding ( when real value appears )
//EL: will WRITE to valuebinding, READ from newvalue. 
//these READS must be shifted until AFTER writes of values related to newvalue.

//i) a WRITE to a nested path DEPENDS ON a WRITE to a STRICTLY higher path.
//ii) a READ from a nested path DEPENDS ON a WRITE to a STRICTLY OR EQUAL higher path.
//iii) a WRITE to the SAME PATH should technically just replace the dependent write.

//OK then. edge created FROM bean TO dependency, i.e. that which operates on higher path.
//                  WRITE                             READ
//e.g. valuebinding {permissionbean.name}       value [field]             NODE 1
//fast EL:          {resource.a8394.permission} value {permissionbean}    NODE 2
//edge FROM 2 -> 1
//hah. fast EL is actually SLOW!
  RequestSubmittedValueCache rsvc;
  ELDependencyMap elmap = new ELDependencyMap();
  HashSet emitted = new HashSet();
  ArrayList output = new ArrayList();

  public SVESorter(RequestSubmittedValueCache tosort) {
    this.rsvc = tosort;

    for (int i = 0; i < rsvc.entries.size(); ++i) {
      SubmittedValueEntry entry = rsvc.entryAt(i);
      elmap.recordWrite(entry.valuebinding, entry);
      if (entry.newvalue instanceof String) {
        String readel = (String) entry.newvalue;
        if (readel != null) {
          elmap.recordRead(readel, entry);
        }
      }
    }
  }

  public List getSortedRSVC() {

    for (int i = 0; i < rsvc.entries.size(); ++i) {
      SubmittedValueEntry entry = rsvc.entryAt(i);
      if (!emitted.contains(entry)) {
        attemptEvaluate(entry);
      }
    }
    return output;
  }

  private void attemptEvaluate(SubmittedValueEntry entry) {
    emitted.add(entry);
    String readpath = elmap.getReadPath(entry);
    if (readpath != null) {
      scheduleWrites(readpath);
    }
    String writepath = elmap.getWritePath(entry);
    if (writepath != null) {
      scheduleWrites(writepath);
    }
    output.add(entry);
  }

  private void scheduleWrites(String writepath) {
    // NB, the keeping of this on the stack results from being SURE that we
    // can never recur to this path as a result of attemptEvaluate of
    // a parent path (Even though we clearly do this as a result of fearing
    // recurring to it from further up our *own* stack).
    List writingbeans = elmap.getWriters(writepath);
    if (writingbeans == ELDependencyMap.VALID_LIST_MARKER) return;
    // If we *write* a path, all pending *writes* to higher paths must already
    // be done.
    String parentpath = BeanUtil.getContainingPath(writepath);
    if (parentpath != null) {
      scheduleWrites(parentpath);
    }
  
    if (writingbeans != null) {
      for (int i = 0; i < writingbeans.size(); ++i) {
        SubmittedValueEntry sve = (SubmittedValueEntry) writingbeans.get(i);
        if (!emitted.contains(sve)) {
          attemptEvaluate(sve);
        }
      }
      elmap.recordPathValid(writepath);
    }
  }
}
