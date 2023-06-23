package com.example.chatapplication;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;

public class ServerController {
    @FXML
    private TextField HostID;
    @FXML
    private TextField PortID;
    @FXML
    private ListView<String> testview;
    @FXML
    private TextField MymsgID;

    PrintWriter Pw;
    @FXML
    protected void onConnect() throws IOException{
        String host=HostID.getText();
        int port= Integer.parseInt(PortID.getText());
        Socket s = new Socket(host,port);

        InputStream is = s.getInputStream();
        InputStreamReader isr= new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        OutputStream os = s.getOutputStream();
        Pw= new PrintWriter(os,true);
        new Thread(()->{
            while(true) {
                try {
                    String Response = br.readLine();
                    Platform.runLater(() -> {
                        testview.getItems().add(Response);
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @FXML
    public void onSubmit(){
        String message =MymsgID.getText();
        Pw.println(message);
        MymsgID.clear();
    }

}