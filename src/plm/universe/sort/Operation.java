package plm.universe.sort;

public abstract class Operation {

  protected int source;      // the source of the operation
  protected int destination; // the destination of the operation

  /**
   * Constructor of the class Operation
   * @param source the source of the operation
   * @param destination the destination of the operation
   */
  public Operation(int source, int destination)
  {
    this.source      = source;
    this.destination = destination;
  }

  /** Rebuilds an operation from its textual form, as written by toString() in the subclasses */
  static Operation parse(String line)
  {
    String[] tokens = line.split(" ");
    int first       = Integer.parseInt(tokens[1]);
    return switch (tokens[0]) {
      case "swap" -> new Swap(first, Integer.parseInt(tokens[2]));
      case "copy" -> new CopyVal(first, Integer.parseInt(tokens[2]));
      case "set" -> new SetVal(first, Integer.parseInt(tokens[2]));
      case "get" -> new GetVal(first);
      default -> throw new IllegalArgumentException("unknown operation '" + line + "'");
    };
  }

  /**
   * Indicate whether some other object is "equal to" this one
   * @param o the reference object with which to compare
   */
  public boolean equals(Object o)
  {
    if (o == null || !(o instanceof Operation))
      return false;

    Operation other = (Operation)o;
    if (destination != other.destination)
      return false;
    if (!getClass().equals(o.getClass()))
      return false;
    if (source != other.source)
      return false;
    return true;
  }

  /**
   * Returns the source of the operation
   */
  public int getSource() { return this.source; }

  /**
   * Returns the destination of the operation
   */
  public int getDestination() { return this.destination; }

  /**
   * Compute an operation on init
   * @param init the values on which compute the operation
   * @return a new array of values equals to init where the operation had been computed
   */
  public abstract int[] run(int[] init);
}
