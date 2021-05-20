package com.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static void main(String[] args) {
        DatagramSocket datagramSocket = null;
        try{
            int port = 1234;
            datagramSocket = new DatagramSocket(port);
            byte[] buffer = new byte[1000];

            while (true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(request);
                // some processing in data
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
                datagramSocket.send(reply);
            }

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
