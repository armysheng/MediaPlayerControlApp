package ar.com.ksys.mediaplayercontrol;

import android.os.AsyncTask;

public class CommandManager 
{
	private MessageManager mMessageManager;
	
	CommandManager(String ipAddress, int port) 
	{
        mMessageManager = new MessageManager(ipAddress, port);
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
    		command.execute();
    	}
    }
}
