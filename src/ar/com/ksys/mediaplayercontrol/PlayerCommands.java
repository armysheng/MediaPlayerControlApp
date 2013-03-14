package ar.com.ksys.mediaplayercontrol;

import android.app.Activity;
import android.widget.*;

public class PlayerCommands 
{
	public abstract static class Command 
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
			return name() + (hasArgs() ? "" : " " + getArgs());
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
			return args.isEmpty();
		}
	}
	
    public static class PlayCommand extends Command
    {
		public String name() {
			return "play";
		}
    }
    
    public static class PlayPauseCommand extends Command
    {
		public String name() {
			return "playPause";
		}
    }
    
    public static class PauseCommand extends Command
    {
		public String name() {
			return "pause";
		}
    }
    
    public static class StopCommand extends Command
    {
		public String name() {
			return "stop";
		}
    }
    
    public static class NextCommand extends Command
    {
		public String name() {
			return "next";
		}
    }
    
    public static class PrevCommand extends Command
    {
		public String name() {
			return "previous";
		}
    }
    
    public static class SetShuffleCommand extends Command
    {
		public String name() {
			return "setShuffle";
		}
    }
    
    public static class SetRepeatCommand extends Command
    {
		public String name() {
			return "setRepeat";
		}
    }
    
    public static class SetVolumeCommand extends Command
    {
		public String name() {
			return "setVolume";
		}
    }
    
	public static class VolumeCommand extends Command
	{
		@Override
		public void execute(Activity activity)
		{
			SeekBar volumeBar = (SeekBar)activity.findViewById(R.id.volumeBar);
			volumeBar.setProgress( Integer.parseInt( getResponse() ) );
		}

		@Override
		public boolean needsResponse()
		{
			return true;
		}
		
		public String name() 
		{
			return "volume";
		}
	}
	
	public static class SongTitleCommand extends Command
	{
		@Override
		public void execute(Activity activity)
		{
			TextView textView = (TextView)activity.findViewById(R.id.textCurrentSong);
			textView.setText( getResponse() );
		}
		
		@Override
		public boolean needsResponse()
		{
			return true;
		}

		public String name() {
			return "songTitle";
		}
	}
	
	public static class CurrentPositionCommand extends Command
	{
		@Override
		public void execute(Activity activity)
		{
			TextView textCurrentPos = (TextView)activity.findViewById(R.id.textCurrentPos);
			int reponse = Integer.parseInt(getResponse()) + 1;
			textCurrentPos.setText( String.valueOf(reponse) + ".");
		}
		
		@Override
		public boolean needsResponse()
		{
			return true;
		}

		public String name() {
			return "currentPos";
		}
	}
	
	public static class IsShuffleCommand extends Command
	{
		@Override
		public void execute(Activity activity)
		{
			CheckBox checkShuffle = (CheckBox)activity.findViewById(R.id.checkShuffle);
			checkShuffle.setChecked(getResponse().equalsIgnoreCase("true"));
		}
		
		@Override
		public boolean needsResponse()
		{
			return true;
		}

		public String name() {
			return "isShuffle";
		}
	}
	
	public static class IsRepeatCommand extends Command
	{
		@Override
		public void execute(Activity activity)
		{
			CheckBox checkRepeat = (CheckBox)activity.findViewById(R.id.checkRepeat);
			checkRepeat.setChecked(getResponse().equalsIgnoreCase("true"));
		}

		@Override
		public boolean needsResponse()
		{
			return true;
		}
		
		public String name() {
			return "isRepeat";
		}
	}
}
