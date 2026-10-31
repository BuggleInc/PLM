package plm.universe.posix;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A state of a world, drawn by its view: its processes (the student's first, then the ones of the peers in their order) with the
 * titles of their machines, its files, and the event of the trace that led to it (null for the initial state), done by one of the
 * processes (or by the network, as actor -1). The world keeps a copy after each event, to draw its timeline.
 */
record Scene(int now, PosixWorld.Event event, int actor, List<PosixProcess> processes, List<String> titles, Map<String, String> files) {
  /** Whether the event is a system call of a process, rather than the network carrying data */
  boolean syscall() { return actor >= 0; }

  /** The world as it is, without copying it, for the view of the current state */
  static Scene live(PosixWorld world) { return new Scene(world.now, null, -1, world.processes(), titles(world), world.files); }

  /** A copy of the state of the world after an event of actor (the index of a process, or -1) */
  static Scene after(PosixWorld world, PosixWorld.Event event, int actor)
  {
    Map<FileDescription, FileDescription> copies = new IdentityHashMap<>(); // the sockets of all the processes point to each other
    List<PosixProcess> processes                 = new ArrayList<>();
    for (PosixProcess process : world.processes())
      processes.add(new PosixProcess(process, copies));
    return new Scene(world.now, event, actor, processes, titles(world), new TreeMap<>(world.files));
  }

  private static List<String> titles(PosixWorld world)
  {
    List<String> res = new ArrayList<>(List.of("Your machine (" + PosixWorld.STUDENT_IP + ")"));
    for (Peer p : world.peers)
      res.add(p.title());
    return res;
  }
}
