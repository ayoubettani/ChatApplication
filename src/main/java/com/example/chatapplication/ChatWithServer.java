package com.example.chatapplication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatWithServer extends Thread{
    private int ClientNumber;
    private List<Communication> Clients = new ArrayList<Communication>();
    public static void main(String[] args) {
        new ChatWithServer().run();
    }
    @Override
    public void run(){
        try {
            ServerSocket ss = new ServerSocket(1234);
            System.out.println("Le serveur essaie de démarrer");
            while (true){
                Socket s = ss.accept();
                ++ClientNumber;
                Communication communication = new Communication(s,ClientNumber);
                Clients.add(communication);
                communication.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  class Communication extends Thread{
        private int ClientNumber;
        private Socket s;
        public Communication(Socket s, int ClientNumber) {
            this.s=s;
            this.ClientNumber=ClientNumber;
        }
        @Override
        public void run(){
            try {
                InputStream is = s.getInputStream();

                InputStreamReader isr= new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                OutputStream os = s.getOutputStream();
                String Ip = s.getRemoteSocketAddress().toString();
                System.out.println("client numero "+ClientNumber +" et son Ip est  "+Ip);
                PrintWriter Pr = new PrintWriter(os,true);
                Pr.println("vous etes le client : "+ClientNumber);
                Pr.println("send a message");

                while(true){
                    String UserRequest = br.readLine();
                    if(UserRequest.contains("=>")){
                        String[] UserMessage = UserRequest.split("=>");
                        if(UserMessage.length == 2){
                            String msg = UserMessage[1];
                            int numClient = Integer.parseInt(UserMessage[0]);
                            Send(msg,s,numClient);
                        }
                    }else{
                        Send(UserRequest,s,-1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        void Send(String UserRequest , Socket socket , int numClient) throws IOException {
            for(Communication client : Clients) {
                if (client.s != socket) {
                    if (client.ClientNumber == numClient || numClient == -1) {
                        PrintWriter Pw = new PrintWriter(client.s.getOutputStream(), true);
                        Pw.println(UserRequest);
                    }
                }
            }

        }
    }
}
