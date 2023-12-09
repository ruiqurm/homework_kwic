package cn.edu.bupt.sa.kwic.source;

import cn.edu.bupt.sa.kwic.Pipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketSource extends Source<String> {
    private String serverHost;
    private int serverPort;
    public SocketSource(Pipe<String> input, String serverHost, int serverPort) {
        super(input);
        if(serverHost == null || serverHost.isEmpty()) {
            throw new IllegalArgumentException("serverHost is null or empty.");
        }
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }


    @Override
    protected  void handleInput() {
        try {
            Socket socket = new Socket(serverHost, serverPort);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String serverResponse;
            while(((serverResponse = reader.readLine()) != null)) {
                System.out.println(serverResponse);
                this.outPipe.put(serverResponse);
            }

            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (BindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}