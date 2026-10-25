package plm.universe.posix;

import java.util.Map;

/**
 * What a descriptor points to: an open file description in POSIX terms (struct file in Linux), that is a regular file, a socket
 * or a standard stream with the state of its opening. Several descriptors, possibly of several processes, may share one.
 */
abstract class FileDescription {
  /**
   * The copy of this description in copies, made if needed: the descriptions shared by several descriptors or processes, or
   * pointing to each other, stay so in the copies. The copy has the class of this description.
   */
  @SuppressWarnings("unchecked") final <T extends FileDescription> T copy(Map<FileDescription, FileDescription> copies)
  {
    FileDescription res = copies.get(this);
    if (res == null) {
      res = duplicate();
      copies.put(this, res);
      res.copyLinks(this, copies);
    }
    return (T)res;
  }

  /** A new description of the same class, with the own state of this one */
  abstract FileDescription duplicate();

  /** Points to the copies of the descriptions that original points to, if any */
  void copyLinks(FileDescription original, Map<FileDescription, FileDescription> copies) {}

  /** The description shown when comparing the open descriptors of the worlds */
  abstract String describe();
}
