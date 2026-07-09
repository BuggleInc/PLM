import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;

public abstract class Remote {

    /*
     * System.in  : answers from the PLM
     * System.out : student's debug output
     * System.err : commands sent to the PLM
     */

    private static final Scanner inputScan = new Scanner(System.in);

    private static String answerBuffer;

    private static void getAnswerLine() {
        answerBuffer = inputScan.nextLine();
        System.out.println("Student receives: " + answerBuffer);
        System.out.flush();
        if (answerBuffer == null) {
            System.exit(1);
        }
    }

    public static int getAnswerInt() {
        getAnswerLine();
        return Integer.parseInt(answerBuffer);
    }

    public static boolean getAnswerBoolean() {
        return getAnswerInt() == 1;
    }


    public static double getAnswerDouble() {
        getAnswerLine();
        return Double.parseDouble(answerBuffer);
    }

    public static String getAnswerString() {
        getAnswerLine();
        return answerBuffer;
    }

    public static char getAnswerChar() {
        getAnswerLine();
        return answerBuffer.charAt(0);
    }

    public static void sendCommand(String format, Object... args) {
      String command = String.format(Locale.ENGLISH, format, args);

      System.out.println("Student sends: " + command);
      System.out.flush();
      System.err.println(command);
      System.err.flush();
    }

    /* BEGIN UTILS */

    public static String int2str(int n) {
        return Integer.toString(n);
    }

    /* END UTILS */

    public abstract void run();

    public static void main(String[] args) {
        throw new UnsupportedOperationException(
                "Subclasses should provide their own main() method.");
    }
}