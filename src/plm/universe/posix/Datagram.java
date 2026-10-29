package plm.universe.posix;

/** A UDP datagram, with the address of its sender */
public record Datagram(String ip, int port, String data) {}
