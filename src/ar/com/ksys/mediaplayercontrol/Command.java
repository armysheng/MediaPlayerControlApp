package ar.com.ksys.mediaplayercontrol;

import android.app.Activity;

public abstract class Command 
{
	private String args = new String();
	private String response = new String();
	
	public abstract String name();

	public void execute(Activity activity) { };
	
	public boolean needsResponse()
	{ 
		return false;
	}
	
	public String stringCommand() 
	{
		return name() + (hasArgs() ? " " + getArgs() : "");
	}
	
	public String getResponse() 
	{
		return response;
	}
	
	public void setResponse(String r) 
	{
		response = r;
	}
	
	public String getArgs() 
	{
		return args;
	}
	
	public void setArgs(String args) 
	{
		this.args = args;
	}
	
	public boolean hasArgs() 
	{
		return !args.isEmpty();
	}
}
