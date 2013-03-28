package ar.com.ksys.mediaplayercontrol;

import android.os.AsyncTask;

public class CommandManager 
{
    private MessageManager mMessageManager;
    private MainActivity mainActivity;

    public CommandManager(MessageManager messageManager, MainActivity activity)
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

    public void sendCommandToPlayer(Command command, String arg)
    {
        command.setArgs(arg);
        sendCommandToPlayer(command);
    }

    public void sendCommandToPlayer(Command command, int arg) 
    {
        command.setArgs( String.valueOf(arg) );
        sendCommandToPlayer(command);
    }

    public void sendCommandToPlayer(Command command, boolean arg)
    {
        command.setArgs(arg ? "true" : "false");
        sendCommandToPlayer(command);
    }

    private class CommandSender extends AsyncTask<Command, Void, String> 
    {
        private Command command;
        private Exception error;

        protected String doInBackground(Command... cmds)
        {
            command = cmds[0];
            String stringCmd = command.stringCommand();
            String response = new String();

            try {
                response = mMessageManager.sendCommand(stringCmd);
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
