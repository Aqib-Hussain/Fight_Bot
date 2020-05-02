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
    public static boolean messageSent;
    static Fight fight = new Fight();
    public static String dateTime = "";

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner console = new Scanner(System.in);


        System.out.println("Enter the IP Address: ");
        ipAddress = console.nextLine();
        System.out.println("Enter the port number: ");
        portNumber = console.nextLine();
        System.out.println("Enter the channel name: ");
        channelName = "#" + console.nextLine();

        Socket socket = new Socket(ipAddress, Integer.parseInt(portNumber));

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());


        // Bot name and Server
        write("NICK", "FightBot");
        write("USER", "FightBot 8 * :Aqib's bot v0.1");
        write("JOIN", channelName);


        // Initial loop to check that intro messages have played
        while (in.hasNext()) {
            String serverMessage = in.nextLine();
            System.out.println("<<< " + serverMessage);
            if (serverMessage.contains("366")) {
                break;
            }
        }

        // Intro message upon joining the channel
        write("NOTICE ", channelName + " : FightBot is here!");

        // Check that messages are being sent and then output commands
        while (in.hasNext()) {
            String serverMessage = in.nextLine();
            System.out.println("<<< " + serverMessage);
            String[] prefixArray = serverMessage.split(":");

            // Keeps the bot in the server
            if (serverMessage.startsWith("PING")) {
                String pingContents = serverMessage.split(" ")[1].replace(":", "");
                System.out.println(serverMessage);
                write("PONG ", pingContents);
            }

            // Checks if the time message has been sent
            if (messageSent) {
                write("PRIVMSG ", channelName + " : " + prefixArray[2] + ":" + prefixArray[3]);
                messageSent = false;
            }

            if (prefixArray.length > 2) {

                String[] arrayOfServerMessages = prefixArray[2].split(" ");

                if (arrayOfServerMessages[0].toLowerCase().equals("fightbot")) {

                    if (arrayOfServerMessages.length > 1) {
                        // Commands are checked for here
                        if (arrayOfServerMessages[1].toLowerCase().equals("help")) {
                            write("PRIVMSG ", channelName + " : // Here's a list of commands FightBot can use");
                            write("PRIVMSG ", channelName + " : // Fight <username> : declares a war on the selected user");
                            write("PRIVMSG ", channelName + " : // DeathBattle <username> : declares a war on the selected user with MUCH higher stakes (bot must have op)");
                            write("PRIVMSG ", channelName + " : // Kick <username> : FightBot purges the channel of weaklings (bot must have op)");
                            write("PRIVMSG ", channelName + " : // Bro : FightBot bros out with you");
                            write("PRIVMSG ", channelName + " : // DateTime : FightBot tells you the date and time");
                            write("PRIVMSG ", channelName + " : // Users : FightBot checks how many users and channels there are");
                            write("PRIVMSG ", channelName + " : // Leave : FightBot leaves the channel to find stronger competitors");
                        }
                        // Leave the server when requested
                        if (arrayOfServerMessages[1].toLowerCase().equals("leave")) {
                            write("PRIVMSG ", channelName + " : Bye Everyone!");
                            write("QUIT ", channelName);
                        }
                        // Display the time upon request
                        if (arrayOfServerMessages[1].equals("datetime")) {
                            messageSent = true;
                            write("TIME ", "");
                        }
                        // Kicks a user from the channel if bot is op
                        if (arrayOfServerMessages[1].toLowerCase().equals("kick")) {
                            String[] userArray = prefixArray[1].split("!");
                            write("KICK ", channelName + " " + arrayOfServerMessages[2] + " : Upon request of " + userArray[0]);
                            write("PRIVMSG ", channelName + " : Sorry " + arrayOfServerMessages[2] + " you were too weak for this channel");
                        }
                        // check the users on the channel
                        if (arrayOfServerMessages[1].toLowerCase().equals("users")) {
                            write("LUSERS ", channelName);
                        }
                        // Fight command activated here
                        if (arrayOfServerMessages[1].toLowerCase().equals("fight")) {
                            fight.setFightState();
                            fight.initiateFight(arrayOfServerMessages, prefixArray);
                            write("PRIVMSG ", channelName + " : " + fight.getUser1() + " Has declared war on " + fight.getUser2());
                            write("PRIVMSG ", channelName + " : ");
                            // While neither user is defeated
                            fightLogic();
                            write("PRIVMSG ", channelName + " : The victor is " + fight.getWinner() + ", congratulations!");
                        }
                        // DeathBattle command activated here
                        if (arrayOfServerMessages[1].toLowerCase().equals("deathbattle")) {
                            fight.setFightState();
                            fight.initiateFight(arrayOfServerMessages, prefixArray);
                            write("PRIVMSG ", channelName + " : " + fight.getUser1() + " Has declared a DEATHBATTLE on " + fight.getUser2());
                            write("PRIVMSG ", channelName + " : ");
                            // While neither user is defeated
                            fightLogic();
                            write("PRIVMSG ", channelName + " : The victor is " + fight.getWinner() + ", congratulations!");
                            write("PRIVMSG ", channelName + " : As for " + fight.getLoser() + "... we don't tolerate weakness in this channel. Goodbye.");
                            write("KICK ", channelName + " " + fight.getLoser() + " : For losing to " + fight.getWinner() + " in the DEATHBATTLE");
                        }
                        // Bro command activated here
                        if (arrayOfServerMessages[1].toUpperCase().equals("BRO")) {
                            if (brocount < 200) {
                                Bro.incrementBro(brocount);
                            }
                            write("PRIVMSG ", channelName + " : " + bro);
                            brocount++;
                        }
                    }
                }
            }
        }
        in.close();
        out.close();
        socket.close();
    }

    public static void write(String command, String message) {
        String fullMessage = command + " " + message;
        System.out.println(">>> " + fullMessage);
        out.print(fullMessage + "\r\n");
        out.flush();
    }

    private static void fightLogic() throws InterruptedException {
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
    }
}