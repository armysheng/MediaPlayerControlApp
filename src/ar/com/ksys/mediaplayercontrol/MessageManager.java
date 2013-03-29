package ar.com.ksys.mediaplayercontrol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.InetSocketAddress;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MessageManager 
{
    private Socket socket;
    private InetSocketAddress address;
    private ConnectivityManager connectionManager;

    public MessageManager(String destination, int port, 
            ConnectivityManager connManager)
    {
        socket = new Socket();
        address = new InetSocketAddress(destination, port);
        connectionManager = connManager;
    }

    public boolean isNetworkAvailable() 
    {
        NetworkInfo netInfo = connectionManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public boolean isConnected() 
    {
        return socket.isConnected() && !socket.isClosed();
    }
    
    public boolean isClosed()
    {
        return socket.isClosed();
    }

    public void connect() throws IOException 
    {
        socket.connect(address);
    }

    public void renewConnection() 
    {
        socket = null;
        socket = new Socket();
    }

    public void closeConnection() 
    {
        try {
            socket.close();
        } catch (IOException e) { }
    }

    private void send(String msg) throws IOException
    {
        byte[] message = msg.getBytes();

        OutputStream writer = socket.getOutputStream();
        writer.write(message);
        writer.flush();
    }

    private String receive() throws IOException
    {
        byte[] response = new byte[65536];

        InputStream socketReader = socket.getInputStream();
        int msgLength = socketReader.read(response);

        return new String(response, 0, msgLength);
    }

    public String sendCommand(String msg) throws IOException
    {
        send(msg);
        return receive();
    }
}
