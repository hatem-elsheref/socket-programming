package com.project.rmi;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RMIFileTransfer implements RMIFileTransferInterface {


    private static final String SERVER  = "SERVER";
    private static final String CLIENT  = "CLIENT";


    public RMIFileTransfer(){}
    public RMIFileTransfer(String mode) throws NotBoundException, IOException {
        Scanner reader = new Scanner(System.in);

        // make validation the inputs must not be empty
        if (mode.equals(RMIFileTransfer.SERVER)){
            System.out.println("initializing the server...");
            System.out.println("Enter Registry Port > 1023 : ");
            int port = Integer.parseInt(reader.nextLine());

            System.out.println("Enter Service Name : ");
            String serviceName = reader.nextLine();

            server(port, serviceName);

        }else{
            System.out.println("initializing the connection...");
            System.out.println("Enter Hostname : ");
            String hostname = reader.nextLine();
            System.out.println("Enter Registry Port > 1023 : ");
            int port = Integer.parseInt(reader.nextLine());

            System.out.println("Enter Service Name : ");
            String serviceName = reader.nextLine();

            client(hostname, port, serviceName);

        }
    }




    public void client(String hostname, int port, String serviceName) throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(hostname, port);
        RMIFileTransferInterface stub = (RMIFileTransferInterface)registry.lookup(serviceName);

        System.out.println("Accept Transferring FIle [Y/N]?");
        Scanner scanner = new Scanner(System.in);
        byte [] buffer;
        switch ((scanner.nextLine()).toLowerCase()){
            case "y":
                buffer = stub.accept(stub.filename());
                System.out.println("Enter File Name : ");
                String filename = "/home/elsheref/Desktop/JAVA_CODE/fci_4_semester_2/socket/downloads/" + scanner.nextLine();
                File file = new File(filename);

                System.out.println("downloading..." + filename);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(buffer);
                fileOutputStream.flush();
                fileOutputStream.close();
                break;
            case "n":
                stub.refuse();
                return;
        }

    }


    public void server(int port, String serviceName){
        try {

            RMIFileTransfer rmi = new RMIFileTransfer();

            RMIFileTransferInterface skelton = (RMIFileTransferInterface) UnicastRemoteObject.exportObject(rmi, 0);

            Registry registry = LocateRegistry.createRegistry(port);

            registry.rebind(serviceName, skelton);

            System.err.println("server ready");
            System.err.println("server running now");

        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }


    public byte[] downloadFromServer(String filename){

        try {
            File file = new File(filename);
            byte [] buffer = new byte[(int) file.length()];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(buffer, 0, buffer.length);
            inputStream.close();
            return  buffer;
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        return new byte[0];
    }


    public String filename(){
        return "/home/elsheref/Desktop/JAVA_CODE/fci_4_semester_2/socket/uploads/users.txt";
    }

    public byte[] accept(String filename){
        System.out.println("accept File Transferring...");
        return downloadFromServer(filename);
    }


    public void refuse(){
        System.out.println("Refused File Transferring...");
    }
    public static void main(String[] args) throws NotBoundException, IOException {
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
                    new RMIFileTransfer(RMIFileTransfer.SERVER);
                else
                    new RMIFileTransfer(RMIFileTransfer.CLIENT);
            }else{
                System.out.println("invalid operation enter valid operation from 1 - 3");
                continue;
            }
        }

    }

}





