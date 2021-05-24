package com.project.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

public class FileTransfer {

    private static final String SERVER  = "SERVER";
    private static final String CLIENT  = "CLIENT";

    public FileTransfer(String mode){
        Scanner reader = new Scanner(System.in);

        // make validation the inputs must not be empty
        if (mode.equals(FileTransfer.SERVER)){
            System.out.println("initializing the server...");
            System.out.println("Enter Port > 1023 : ");
            startServer(Integer.parseInt(reader.nextLine()));

        }else{
            System.out.println("initializing the connection...");
            System.out.println("Enter Hostname : ");
            String hostname = reader.nextLine();
            System.out.println("Enter Port > 1023 : ");
            int port = Integer.parseInt(reader.nextLine());
            startClient(hostname, port);

        }
    }


    public void startServer(int port){
        try {

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("server running now in port " + port);

            while (true) {

                Socket clientSocket = serverSocket.accept();
                // start new thread to deal with this client
                // send offer to download file
                // accept the response
                // if user accepted send file to him
                // if user rejected end the thread
                Thread thread = new Thread(new Runnable() {
                    private Socket newClient = clientSocket;
                    public void run() {
                        System.out.println("client connected" + clientSocket);
                        try {

                            DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());
                            DataInputStream reader = new DataInputStream(clientSocket.getInputStream());

                            String message = new String(reader.readUTF());

                            if (message.toLowerCase().equals("open"))
                                writer.writeUTF("offer");
                            else
                                writer.writeUTF("error");


                            message = reader.readUTF();

                            if (message.toLowerCase().equals("accept")){
                                // start send file from here...
                                String filename = "/home/elsheref/Desktop/JAVA_CODE/fci_4_semester_2/socket/uploads/db.txt";
                                File file = new File(filename);
                                byte [] buffer = new byte[(int)file.length()];
                                FileInputStream fileInputStream = new FileInputStream(filename);
                                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                                bufferedInputStream.read(buffer,0,buffer.length);
                                writer.write(buffer,0,buffer.length);
                                writer.flush();
                                reader.close();
                                writer.close();
                            }

                            writer.writeUTF("close");
                            reader.close();
                            writer.close();
                            clientSocket.close();
                        }catch (IOException exception){
                            System.out.println(exception.getMessage());
                        }
                    };
                });


                thread.start();

            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());;
        }
    }


    public void startClient(String hostname, int port){
        try {
            Socket socket = new Socket(hostname, port);

            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            DataInputStream reader = new DataInputStream(socket.getInputStream());

            writer.writeUTF("open");
            String message = new String(reader.readUTF());

           if (!message.toLowerCase().equals("close") || !message.toLowerCase().equals("error")){

               if (message.toLowerCase().equals("offer")){

                   System.out.println("Accept Transferring FIle [Y/N]?");

                   Scanner scanner = new Scanner(System.in);

                    boolean state = true;
                   switch ((scanner.nextLine()).toLowerCase(Locale.ROOT)){
                       case "y":
                           writer.writeUTF("accept");
                           break;
                       case "n":
                           state = false;
                           writer.writeUTF("refuse");
                           break;
                   }

                   if (state){

                       int bytesRead;
                       int currentTot = 0;
                       byte [] buffer = new byte [1022386];


                       System.out.println("Enter FIle Name : ");
                       Scanner s = new Scanner(System.in);
                       String name = s.nextLine();
                       name = name.isEmpty() ? "db.txt" : name;

                       FileOutputStream fileOutputStream = new FileOutputStream("/home/elsheref/Desktop/JAVA_CODE/fci_4_semester_2/socket/downloads/" + name);

                       System.out.println("downloading ...");
                       BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                       bytesRead = reader.read(buffer,0,buffer.length);
                       currentTot = bytesRead;
                       do {
                           bytesRead = reader.read(buffer, currentTot, (buffer.length-currentTot));
                           if(bytesRead >= 0) currentTot += bytesRead;
                       } while(bytesRead > -1);

                       bufferedOutputStream.write(buffer, 0 , currentTot);
                       bufferedOutputStream.flush();
                       System.out.println("file downloaded successfully");


                   }

               }
           }

            reader.close();
            writer.close();
            socket.close();


        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }


    public static void main(String[] args) {

        while (true){
            System.out.println("1) start as a server");
            System.out.println("2) start as a client");
            System.out.println("3) Exit");

            Scanner reader = new Scanner(System.in);
            int operation = Integer.parseInt(reader.nextLine());
            if (operation == 1 || operation == 2 || operation == 3){
                if (operation == 3)
                    System.exit(0);
                else if (operation == 1)
                    new FileTransfer(FileTransfer.SERVER);
                else
                    new FileTransfer(FileTransfer.CLIENT);
            }else{
                System.out.println("invalid operation enter valid operation from 1 - 3");
                continue;
            }
        }

    }

}
