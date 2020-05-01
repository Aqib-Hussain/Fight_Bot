package Main;

import java.io.IOException;


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
