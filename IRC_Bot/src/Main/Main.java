package Main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class Main {
    private static String nick;
    private static String serverName;
    private static String userName;
    private static String realName;
    private static PrintWriter out;
    private static Scanner in;
    //private String

    public static void main(String [] args) throws IOException {
        Scanner console = new Scanner(System.in);

        Socket socket = new Socket("127.0.0.1", 6667);

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());

        write("Nick", "FightBot");
        write("USER", "FightBot 8 * :Aqib's bot v0.1" );
        write("JOIN", "#TheBois");


        while(in.hasNext()) {
            String serverMessage = in.nextLine();
            System.out.println("<<< " + serverMessage);
            if (serverMessage.equals(":selsey.nsqdc.city.ac.uk 366 FightBot #TheBois :End of NAMES list")) {
                break;
            }
        }
        in.close();
        out.close();
        socket.close();

        System.out.println("done");
    }

    private static void write(String command, String message) {
        String fullMessage =  command + " " + message;
        System.out.println(">>> " + fullMessage);
        out.print(fullMessage + "\r\n");
        out.flush();
    }
}