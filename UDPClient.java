package com.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) {
        DatagramSocket datagramSocket = null;
        try{
             datagramSocket = new DatagramSocket();
            byte[] message = args[0].getBytes();
            int port = Integer.parseInt(args[1]);
            InetAddress host = InetAddress.getByName(args[2]);
            DatagramPacket packet = new DatagramPacket(message, args[0].length(), host, port);
            datagramSocket.send(packet);

            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

            datagramSocket.receive(reply);

            System.out.println(reply.getData());

        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }finally {

            if (datagramSocket != null){
                datagramSocket.close();
                System.out.println("socket closed");
            }
        }
    }
}
