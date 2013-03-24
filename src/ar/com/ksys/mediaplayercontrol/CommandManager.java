package ar.com.ksys.mediaplayercontrol;

import android.app.Activity;
import android.widget.*;
import android.os.AsyncTask;

public class CommandManager 
{
	private MessageManager mMessageManager;
	private TextView textResponse;
	private Activity ui;
	
	CommandManager(Activity activity, String ipAddress, int port) 
	{
        mMessageManager = new MessageManager(ipAddress, port);
        ui = activity;
        textResponse = (TextView)ui.findViewById(R.id.textResponse);
	}

	public void sendCommandToPlayer(Command command)
	{
		new CommandSender().execute(command);
	}
	
	public void sendCommandToPlayer(Command command, String args)
	{
		command.setArgs(args);
		sendCommandToPlayer(command);
	}
	
    /***
     * Parses the response received from the server and executes the command
     * @param response String in the format "command,response"
     */
    private void parseResponse(Command cmd) {
    	textResponse.setText(cmd.stringCommand() + "," + cmd.getResponse());
    	cmd.execute(ui);
    }
	
    private class CommandSender extends AsyncTask<Command, Void, String> {
    	private Command command;
    	
    	protected String doInBackground(Command... cmds) 
    	{
    		command = cmds[0];
    		String stringCmd = command.stringCommand();
    		
    		if( command.needsResponse() )
    			return mMessageManager.sendCommand( stringCmd );
    		else {
    			mMessageManager.sendCommandNoResponse( stringCmd );
    			return new String();
    		}
    	}
    	
    	protected void onPostExecute(String r)
    	{
    		command.setResponse(r);
    		parseResponse(command);
    	}
    }
}
