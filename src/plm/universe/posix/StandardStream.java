package plm.universe.posix;

/** A standard stream of a process: the data waiting on its input, or the data written so far on one of its outputs */
final class StandardStream extends FileDescription {
  /** The name of the stream, as stdin */
  final String name;
  final boolean input;
  String data = "";

  StandardStream(String name, boolean input)
  {
    this.name  = name;
    this.input = input;
  }

  @Override StandardStream duplicate()
  {
    StandardStream res = new StandardStream(name, input);
    res.data           = data;
    return res;
  }

  @Override String describe() { return name; }
}
