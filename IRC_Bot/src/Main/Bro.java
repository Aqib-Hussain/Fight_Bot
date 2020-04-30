package Main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static Main.Main.brocount;


public class Bro {


    static String bro = "BRO";

    public static void incrementBro(int brocount) throws IOException {
        if (brocount > 0) {
            for (int i = 0; i <= brocount; i++) {
                bro += "O";
            }
        }
    }
}
