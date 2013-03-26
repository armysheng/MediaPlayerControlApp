package ar.com.ksys.mediaplayercontrol;

import android.os.AsyncTask;

public class CommandManager 
{
	private MessageManager mMessageManager;
	private MainActivity mainActivity;
	
	CommandManager(MessageManager messageManager, MainActivity activity) 
	{
        mMessageManager = messageManager;
        mainActivity = activity;
	}
	
	public void sendCommandToPlayer(Command command)
	{
		if( mMessageManager.isNetworkAvailable() &&
			mMessageManager.isConnected() )
			new CommandSender().execute(command);
	}
	
	public void sendCommandToPlayer(Command command, String args)
	{
		command.setArgs(args);
		sendCommandToPlayer(command);
	}
	
    private class CommandSender extends AsyncTask<Command, Void, String> {
    	private Command command;
    	private Exception error;
    	
    	protected String doInBackground(Command... cmds) 
    	{
    		command = cmds[0];
    		String stringCmd = command.stringCommand();
    		String response = new String();
    		
    		try {
	    		if( command.needsResponse() )
	    			response = mMessageManager.sendCommand( stringCmd );
	    		else {
	    			mMessageManager.sendCommandNoResponse( stringCmd );
	    		}
    		} catch(Exception e) {
    			error = e;
    		}
    		return response;
    	}
    	
    	protected void onPostExecute(String r)
    	{
    		if( error == null ) {
	    		command.setResponse(r);
	    		command.execute();
    		} else {
    			mMessageManager.closeConnection();
    			mainActivity.showAlertDialog("Error", 
    					"Connection error: " + error.getMessage());
    		}
    	}
    }
}
