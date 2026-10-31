/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks;

/* BEGIN IMPORT */
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
/* END IMPORT */

public class UdpDnsEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */

  /* BEGIN SOLUTION */
  static final int A = 1, TXT = 16, SRV = 33;

  /** The name in the wire format: each label preceded by its length, then a zero byte */
  byte[] encodeName(String name)
  {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    for (String label : name.split("\\.")) {
      result.write(label.length());
      result.writeBytes(label.getBytes(StandardCharsets.US_ASCII));
    }
    result.write(0);
    return result.toByteArray();
  }

  /** The offset after the name written at that offset */
  int skipName(byte[] data, int offset)
  {
    while (data[offset] != 0)
      offset += 1 + data[offset];
    return offset + 1;
  }

  /** The name written at that offset (never compressed by this server) */
  String readName(byte[] data, int offset)
  {
    List<String> labels = new ArrayList<>();
    while (data[offset] != 0) {
      int length = data[offset];
      labels.add(new String(data, offset + 1, length, StandardCharsets.US_ASCII));
      offset += 1 + length;
    }
    return String.join(".", labels);
  }

  /** Asks the records of that type of the name, and returns the data of each record of the answer section */
  List<byte[]> query(DatagramSocket socket, String name, int type) throws IOException
  {
    ByteArrayOutputStream query = new ByteArrayOutputStream();
    query.writeBytes(new byte[] {0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0}); // ID 1, RD, one question
    query.writeBytes(encodeName(name));
    query.writeBytes(new byte[] {0, (byte)type, 0, 1}); // the type (below 256 here), class IN
    byte[] data = query.toByteArray();
    socket.send(new DatagramPacket(data, data.length, InetAddress.getByName("10.0.0.53"), 53));

    byte[] buffer = new byte[512];
    socket.receive(new DatagramPacket(buffer, buffer.length));
    ByteBuffer answer    = ByteBuffer.wrap(buffer);
    int answers          = answer.getShort(6) & 0xFFFF;
    int offset           = skipName(buffer, 12) + 4; // after the copy of the question, with its type and class
    List<byte[]> records = new ArrayList<>();
    for (int i = 0; i < answers; i++) {
      offset         = skipName(buffer, offset);
      int recordType = answer.getShort(offset) & 0xFFFF;
      int length     = answer.getShort(offset + 8) & 0xFFFF;
      offset += 10;
      if (recordType == type)
        records.add(Arrays.copyOfRange(buffer, offset, offset + length));
      offset += length;
    }
    return records;
  }
  /* END SOLUTION */

  public void client() throws IOException
  {
    /* BEGIN SOLUTION */
    try (DatagramSocket socket = new DatagramSocket()) {
      // The hints followed while exploring, which this program does not need to read: the notes of the zone lead to
      // conference.buggleinc.lan, whose notes lead to the SRV record of the XMPP service
      query(socket, "buggleinc.lan", TXT);
      query(socket, "conference.buggleinc.lan", TXT);

      // The XMPP server of buggleinc.lan with the lowest priority
      int bestPriority  = Integer.MAX_VALUE;
      String bestTarget = null;
      for (byte[] record : query(socket, "_xmpp-client._tcp.buggleinc.lan", SRV)) {
        int priority = ByteBuffer.wrap(record).getShort(0) & 0xFFFF;
        if (priority < bestPriority) {
          bestPriority = priority;
          bestTarget   = readName(record, 6);
        }
      }

      byte[] address = query(socket, bestTarget, A).get(0);
      System.out.println((address[0] & 0xFF) + "." + (address[1] & 0xFF) + "." + (address[2] & 0xFF) + "." + (address[3] & 0xFF));
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run() throws Exception { client(); }
  /* END REMOTE */
}
