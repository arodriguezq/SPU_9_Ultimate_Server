import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final int PORT = 9090;
    private static boolean endComunication = false;
    private static PublicKey publicKey;

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + " - SERVER START");
        listen();
        System.out.println(Thread.currentThread().getName() + " - SERVER END");
    }

    private static void listen() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {

            serverSocket = new ServerSocket(PORT);

            while (!endComunication) {

                System.out.println("SERVER.listen(): waiting for new client connection...");

                clientSocket = serverSocket.accept();

                System.out.println("SERVER.listen(): client connected!");


                processComunicationWithClient(clientSocket);

                closeClient(clientSocket);

                System.out.println("SERVER.listen(): client disconnected, waiting for new client connection...");
            }

            //Tanquem el sòcol principal
            if ((serverSocket != null) && (!serverSocket.isClosed())) {
                serverSocket.close();
            }


        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void closeClient(Socket clientSocket) {
        if ((clientSocket != null) && (!clientSocket.isClosed())) {
            try {
                if (!clientSocket.isInputShutdown()) {
                    clientSocket.shutdownInput();
                }
                if (!clientSocket.isOutputShutdown()) {
                    clientSocket.shutdownOutput();
                }
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }

    private static void processComunicationWithClient(Socket clientSocket) {

        ObjectInputStream objectInputStream;
        Message inMessage = null;
        Message outMessage = null;

        try {

            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            do {

                inMessage = (Message) objectInputStream.readObject();

                switch (inMessage.getTypeMessage()) {
                    case "TANCARCONNEXIO":
                        System.out.println("SERVER.procesarMissatgeDelClient(): tipusMissatge rebut= '" + inMessage.getTypeMessage() + "'.");
                        System.out.println("SERVER.processComunicationWithClient(): recieved new message from client: '" + inMessage.toString());
                        break;
                    case "CHAT":
                        System.out.println("SERVER.procesarMissatgeDelClient(): tipusMissatge rebut= '" + inMessage.getTypeMessage() + "'.");
                        System.out.println("SERVER.processComunicationWithClient(): recieved new message from client: '" + inMessage.toString());
                        break;
                    case "RETORNCTRL":
                        System.out.println("SERVER.procesarMissatgeDelClient(): tipusMissatge rebut= '" + inMessage.getTypeMessage() + "'.");
                        System.out.println("SERVER.processComunicationWithClient(): recieved new message from client: '" + inMessage.toString());
                        break;
                    case "CLAUPUBLICA":
                        System.out.println("SERVER.procesarMissatgeDelClient(): tipusMissatge rebut= '" + inMessage.getTypeMessage() + "'.");
                        System.out.println("SERVER.processComunicationWithClient(): recieved new message from client: '" + inMessage.toString());
                        publicKey = inMessage.getPublicKey();
                        break;
                }

                if (!inMessage.getTypeMessage().equals("TANCARCONNEXIO")) {
                    respondToClient(clientSocket);
                }


            } while (!outMessage.getTypeMessage().equals("TANCARCONNEXIO"));

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    private static void respondToClient(Socket clientSocket) {
        boolean correctOption;
        Message reply = null;
        String option;

        Scanner sc = new Scanner(System.in);
        correctOption = false;
        do {
            System.out.println("---------------- SERVER ----------------");
            System.out.println("0. Desconnectar-se del CLIENT");
            System.out.println();
            System.out.println("   ENCRIPTACIÓ ASIMÈTRICA (RSA amb clau embolcallada)");
            System.out.println("1. Generar clau simètrica i público-privades");
            System.out.println("2. Enviar clau pública al CLIENT");
            System.out.println("3. Encriptar missatge amb RSA amb clau embolcallada");
            System.out.println("4. Enviar el missatge encriptat al CLIENT");
            System.out.println();
            System.out.println("    SENSE ENCRIPTACIÓ");
            System.out.println("11. Enviar un missatge al CLIENT (chat)");
            System.out.println("12. Retornar el control de les comunicacions al CLIENT");
            System.out.println("15. Enviar un missatge encriptat al CLIENT");
            System.out.println();
            System.out.println("21. Enviar un fitxer al CLIENT");
            System.out.println();
            System.out.println("50. Tancar el programa (equival al menú 0)");
            System.out.println();
            System.out.print("opció?: ");


            option = sc.next();

            switch (option) {
                case "0":
                    reply = new Message("TANCARCONNEXIO", "El server tanca la comunicació.");
                    correctOption = true;
                    break;
                case "1":

                    //opcioCorrecta = true;
                    break;
                case "3":

                    break;
                case "4":

                    //opcioCorrecta = true;
                    break;
                case "11":
                    Scanner sc2 = new Scanner(System.in);
                    System.out.print("SERVER.procesarMissatgeDelClient(): quin missatge vols enviar al server?: ");

                    String missatge = sc2.nextLine();
                    reply = new Message("CHAT", missatge);
                    correctOption = true;
                    break;
                case "12":
                    reply = new Message("RETORNCTRL", "El server retorna el control de les comunicacions al client.");
                    correctOption = true;
                    break;
                case "15":

                    //opcioCorrecta = true;
                    break;
                case "21":

                    //opcioCorrecta = true;
                    break;
                case "50":
                    reply = new Message("TANCARCONNEXIO", "El server tanca la comunicació.");
                    correctOption = true;
                    break;
                default:
                    System.out.println("COMANDA NO RECONEGUDA");
            }
        } while (!correctOption);

        sendResponseToClient(reply, clientSocket);

    }

    private static void sendResponseToClient(Message reply, Socket clientSocket) {
        try {
            ObjectOutputStream outMessage = new ObjectOutputStream(clientSocket.getOutputStream());
            outMessage.writeObject(reply);
            outMessage.flush();
        } catch (IOException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
