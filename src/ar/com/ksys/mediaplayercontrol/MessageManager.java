package ar.com.ksys.mediaplayercontrol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.InetSocketAddress;

import android.util.Log;

public class MessageManager {
	Socket socket;
	InetSocketAddress address;
	
	MessageManager(String destination, int port)
	{
		socket = new Socket();
		address = new InetSocketAddress(destination, port);
	}
	
	private void send(String msg) throws IOException
	{
		if(!socket.isConnected())
			socket.connect(address);

		byte[] message = msg.getBytes();
			
		OutputStream writer = socket.getOutputStream();
		writer.write(message);
	}
	
	private String receive() throws IOException
	{
		byte[] response = new byte[1000];
		int msgLength = 0;
		
		InputStream socketReader = socket.getInputStream();
		msgLength = socketReader.read(response);

		return new String(response, 0, msgLength);
	}
	
	void sendCommandNoResponse(String msg)
	{
		try {
			send(msg);
		} catch(IOException e) {
			Log.e("MessageManager", "Error sending data");
			e.printStackTrace();
		}
	}
	
	String sendCommand(String msg)
	{
		String response = new String();
		try {
			send(msg);
			response = receive();
		} catch (IOException e) {
			Log.e("MessageManager", "Error sending or receiving data");
			e.printStackTrace();
		}
		return response;
	}
}
