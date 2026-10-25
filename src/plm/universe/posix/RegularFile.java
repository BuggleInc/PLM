package plm.universe.posix;

/** An open regular file: its path, its mode and its offset, while its content stays in PosixWorld.files */
final class RegularFile extends FileDescription {
  final String path;
  final boolean readable, writable, append;
  int offset;

  RegularFile(String path, boolean readable, boolean writable, boolean append)
  {
    this.path     = path;
    this.readable = readable;
    this.writable = writable;
    this.append   = append;
  }

  @Override RegularFile duplicate()
  {
    RegularFile res = new RegularFile(path, readable, writable, append);
    res.offset      = offset;
    return res;
  }

  @Override String describe() { return "file '" + path + "' open for " + (readable ? (writable ? "reading and writing" : "reading") : "writing"); }
}
