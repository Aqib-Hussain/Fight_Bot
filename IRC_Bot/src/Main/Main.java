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

        String dateTime;

//        System.out.println("Enter the IP Address: ");
//        ipAddress = console.nextLine();
//        System.out.println("Enter the port number: ");
//        portNumber = console.nextLine();
        System.out.println("Enter the channel name: ");
        channelName = "#" + console.nextLine();

//        Socket socket = new Socket(ipAddress, Integer.parseInt(portNumber));
        Socket socket = new Socket("127.0.0.1", 6667);

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());

        // Bot name and Server
        write("NICK", "FightBot");
        write("USER", "FightBot 8 * :Aqib's bot v0.1");
        write("JOIN", channelName);


        //Initial loop to check that intro messages have played
        while (in.hasNext()) {
            String serverMessage = in.nextLine();
            System.out.println("<<< " + serverMessage);
            //replace server name with variable
            if (serverMessage.startsWith(":selsey.nsqdc.city.ac.uk 366")) {
                break;
            }
        }


        //Check that messages are being sent and then output commands
        while (in.hasNext()) {
            String serverMessage = in.nextLine();
            System.out.println("<<< " + serverMessage);

            // Keeps the bot in the server
            if (serverMessage.startsWith("PING")) {
                String pingContents = serverMessage.split(" ")[1].replace(":", "");
                System.out.println(serverMessage);
                write("PONG ", pingContents);
            }

            String[] prefixArray = serverMessage.split(":");
            if (prefixArray.length > 2) {

                String[] arrayOfServerMessages = prefixArray[2].split(" ");

                if (arrayOfServerMessages[0].toLowerCase().equals("fightbot")) {

                    if (arrayOfServerMessages.length > 1) {
                        // Commands are checked for here
                        if (arrayOfServerMessages[1].toLowerCase().equals("help")) {
                            write("PRIVMSG ", channelName + " : // Here's a list of commands FightBot can use");
                            write("PRIVMSG ", channelName + " : // Fight <username> : declares a war with the selected user");
                            write("PRIVMSG ", channelName + " : // Bro : FightBot bros out with you");
                            write("PRIVMSG ", channelName + " : // DateTime : FightBot tells you the date and time");
                            write("PRIVMSG ", channelName + " : // Users : FightBot checks how many users and channels there are");
                            write("PRIVMSG ", channelName + " : // Leave : FightBot leaves the channel");
                        }
                        // Leave the server when requested
                        if (arrayOfServerMessages[1].toLowerCase().equals("leave")) {
                            write("PRIVMSG ", channelName + " : Bye Everyone!");
                            write("QUIT ", channelName);
                        }
                        // Displays the local date and time
                        if (arrayOfServerMessages[1].toLowerCase().equals("datetime")) {

                            write("TIME ", ":selsey.nsqdc.city.ac.uk");

//                            System.out.println(Arrays.toString(arrayOfServerMessages));
//                            dateTime = arrayOfServerMessages[1];
//                            write("PRIVMSG ", channelName + " : " + dateTime);

                        }

                        // check the users on the channel
                        if (arrayOfServerMessages[1].toLowerCase().equals("users")) {
                            write("LUSERS ", channelName);
                        }

                        // Fight command activated here
                        if (arrayOfServerMessages[1].toLowerCase().equals("fight")) {
                            fight.setWinner(null);
                            fight.setUser1HP(100);
                            fight.setUser1HP(100);
                            fight.initiateFight(arrayOfServerMessages, prefixArray);
                            write("PRIVMSG ", channelName + " : " + fight.getUser1() + " Has declared war on " + fight.getUser2());
                            write("PRIVMSG ", channelName + " : ");
                            // While neither user is defeated
                            while (fight.getUser1HP() > 0 && fight.getUser2HP() > 0) {
                                if (fight.getUser1HP() > 0) {
                                    TimeUnit.SECONDS.sleep(2);
                                    fight.calculateDamageUser1();
                                    write("PRIVMSG ", channelName + " : " + fight.getUser1() + " " + fight.getAttack() + " " + fight.getUser2() + " for " + fight.getDamageResultUser1() + " damage!");
                                    if (fight.getUser2HP() < 0) {
                                        fight.setUser2HP(0);
                                    }
                                    write("PRIVMSG ", channelName + " : " + fight.getUser2() + " has: " + fight.getUser2HP() + " HP");
                                    write("PRIVMSG ", channelName + " : ");
                                }
                                if (fight.getUser2HP() > 0) {
                                    TimeUnit.SECONDS.sleep(2);
                                    fight.calculateDamageUser2();
                                    write("PRIVMSG ", channelName + " : " + fight.getUser2() + " " + fight.getAttack() + " " + fight.getUser1() + " for " + fight.getDamageResultUser2() + " damage!");
                                    if (fight.getUser1HP() < 0) {
                                        fight.setUser1HP(0);
                                    }
                                    write("PRIVMSG ", channelName + " : " + fight.getUser1() + " has: " + fight.getUser1HP() + " HP");
                                    write("PRIVMSG ", channelName + " : ");
                                }
                            }
                            write("PRIVMSG ", channelName + " : The winner is " + fight.getWinner() + ", congratulations!");

                        }

                        // Bro command activated here
                        if (arrayOfServerMessages[1].toUpperCase().equals("BRO")) {
                            if (brocount < 200) {
                                Bro.incrementBro(brocount);
                            }
                            write("PRIVMSG ", channelName + " : " + bro);
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