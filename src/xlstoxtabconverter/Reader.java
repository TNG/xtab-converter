/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xlstoxtabconverter;

import java.util.Scanner;

/**
 * This class reads input from the user.
 *
 * @author mairs
 */
public class Reader {

    private static final String TRUE = "yes";
    private static final String FALSE = "no";
    private static final String STOP = ".";
    private static final String STOPMSG = "Press \"" + STOP + "\" whenever you "
            + "want to stop";

    public static void printStopMsg() {
        System.out.println(STOPMSG);
    }

    public static String readString(String msg) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String s = "";
        do {
            System.out.print(msg);
            s = scanner.nextLine();
            if (s.equals(STOP)) {
                throw new Exception("The user wants to stop.");
            }
        } while (s.isEmpty());
        return s.trim();
    }

    public static boolean readBoolean(String msg) throws Exception {
        Boolean b = null;
        do {
            String input = readString(msg + "(" + TRUE + "/" + FALSE + ")");
            b = convertStringToBool(input);
        } while (b == null);
        return b;
    }

    private static Boolean convertStringToBool(String s)
            throws IllegalArgumentException {
        switch (s.trim()) {
            case TRUE:
                return true;
            case FALSE:
                return false;
            default:
                return null;
        }
    }
}
