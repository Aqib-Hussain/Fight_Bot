package Main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static Main.Bro.bro;

class Main {

    private static String ipAddress;
    private static String portNumber;
    private static String channelName;
    private static PrintWriter out;
    private static Scanner in;
    public static int brocount = 0;


    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner console = new Scanner(System.in);
        Fight fight = new Fight();

//        System.out.println("Enter the IP Address: ");
//        ipAddress = console.nextLine();
//        System.out.println("Enter the port number: ");
//        portNumber = console.nextLine();
//        System.out.println("Enter the channel name: ");
//        channelName = console.nextLine();

//        Socket socket = new Socket(ipAddress, Integer.parseInt(portNumber));
        Socket socket = new Socket("127.0.0.1", 6667);

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());

        // Bot name and Server
//        write("Nick", "FightBot");
//        write("USER", "FightBot 8 * :Aqib's bot v0.1" );
//        write("JOIN", "#" + channelName);
        write("Nick", "FightBot");
        write("USER", "FightBot 8 * :Aqib's bot v0.1");
        write("JOIN", "#TheBois");

        //Initial loop to check that intro messages have played
        while (in.hasNext()) {
            String serverMessage = in.nextLine();
            System.out.println("<<< " + serverMessage);
            //replace server name with variable
            if (serverMessage.equals(":selsey.nsqdc.city.ac.uk 366 FightBot #TheBois :End of NAMES list")) {
                break;
            }
        }


        //Check that messages are being sent
        while (in.hasNext()) {
            String serverMessage = in.nextLine();
            System.out.println("<<< " + serverMessage);
            if (serverMessage.equals("PING :selsey.nsqdc.city.ac.uk")){
                write("PRIVMSG ", "#TheBois  : FightBot is ready to go!");
            }

            String[] prefixArray = serverMessage.split(":");
            if (prefixArray.length > 2) {

                String[] arrayOfServerMessages = prefixArray[2].split(" ");

                if (arrayOfServerMessages[0].toLowerCase().equals("fightbot")) {

                    if (arrayOfServerMessages.length > 1) {
                        // Commands are checked for here
                        if (arrayOfServerMessages[1].toLowerCase().equals("help")){
                            write("PRIVMSG ", "#TheBois  : ");
                        }
                        // Fight command activated here
                        if (arrayOfServerMessages[1].toLowerCase().equals("fight")) {
                            fight.setWinner(null);
                            fight.setUser1HP(100);
                            fight.setUser1HP(100);
                            fight.initiateFight(arrayOfServerMessages, prefixArray);
                            write("PRIVMSG ", "#TheBois  : " + fight.getUser1() + " Has declared war on " + fight.getUser2());
                            while (fight.getUser1HP() > 0 && fight.getUser2HP() > 0) {
                                TimeUnit.SECONDS.sleep(1);
                                fight.calculateDamageUser1();
                                write("PRIVMSG ", "#TheBois  : " + fight.getUser1() + " " + fight.getAttack() + " " + fight.getUser2() + " for " + fight.getDamageResultUser1());
                                if (fight.getUser2HP() < 0) {
                                    fight.setUser2HP(0);
                                }
                                write("PRIVMSG ", "#TheBois  : " + fight.getUser2() + " has: " + fight.getUser2HP() + " HP");
                                TimeUnit.SECONDS.sleep(1);
                                fight.calculateDamageUser2();
                                write("PRIVMSG ", "#TheBois  : " + fight.getUser2() + " " + fight.getAttack() + " " + fight.getUser1() + " for " + fight.getDamageResultUser2());
                                if (fight.getUser1HP() < 0) {
                                    fight.setUser1HP(0);
                                }
                                write("PRIVMSG ", "#TheBois  : " + fight.getUser1() + " has: " + fight.getUser1HP() + " HP");
                            }
                            write("PRIVMSG ", "#TheBois  : The winner is " + fight.getWinner() + " Congratulations!");

                        }

                        // Bro command activated here
                        if (arrayOfServerMessages[1].toUpperCase().equals("BRO")) {
                            if (brocount < 200) {
                                Bro.incrementBro(brocount);
                            }
                            write("PRIVMSG ", "#TheBois  : " + bro);
                            brocount++;
                        }

                        //insert more commands here
                    }
                }
            }
        }
        in.close();
        out.close();
        socket.close();
    }

    private static void write(String command, String message) {
        String fullMessage = command + " " + message;
        System.out.println(">>> " + fullMessage);
        out.print(fullMessage + "\r\n");
        out.flush();
    }
}