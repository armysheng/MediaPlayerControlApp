package ar.com.ksys.mediaplayercontrol;

import android.widget.*;

public class PlayerCommands 
{
	// Helper method
	private static String timeString(int timeInSeconds)
	{
		int seconds = timeInSeconds % 60;
		int minutes = (timeInSeconds -  seconds) / 60;
		return minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
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
		private PlaybackManager manager;
		
		public VolumeCommand(PlaybackManager pbManager) {
			manager = pbManager;
		}
		
		@Override
		public void execute()
		{
			manager.setVolume( Integer.parseInt(getResponse()) );
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
		private Song song;
		
		public SongTitleCommand(Song s) {
			song = s;
		}
		
		@Override
		public void execute()
		{
			song.setTitle( getResponse() );
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
		private Song song;
		
		public CurrentPositionCommand(Song s) {
			song = s;
		}
		
		@Override
		public void execute()
		{
			song.setTrackNumber( Integer.parseInt(getResponse()) );
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
		private PlaybackManager manager;
		
		public IsShuffleCommand(PlaybackManager pbManager) {
			manager = pbManager;
		}
		
		@Override
		public void execute()
		{
			manager.setShuffle( getResponse().equalsIgnoreCase("true") );
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
		private PlaybackManager manager;
		
		public IsRepeatCommand(PlaybackManager pbManager) {
			manager = pbManager;
		}
		
		@Override
		public void execute()
		{
			manager.setRepeat( getResponse().equalsIgnoreCase("true") );
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
	
	public static class SongInfoCommand extends Command
	{
		private ArrayAdapter<String> playlist;
		private Song song;
		
		SongInfoCommand(Song s) {
			song = s;
		}
		
		SongInfoCommand(ArrayAdapter<String> adapter)
		{
			playlist = adapter;
		}
		
		@Override
		public void execute()
		{
			String response = getResponse();
			int separatorPos = response.indexOf(",");
			
			int songLength = Integer.parseInt( response.substring(0, separatorPos) );
			String timeText = timeString(songLength);
			
			String songTitle = response.substring(separatorPos + 1);
			
			// If we have args we add the song to the list.
			// If we don't have args, we update the seek bar and song length.
			if( hasArgs() ) {
				playlist.add( Integer.parseInt(getArgs()) + 1 + ". " + songTitle + "\t\t\t\t\t\t\t\t\t\t" + timeText);
			} 
			else {
				song.setLength(songLength);
				song.setTitle(songTitle);
			}
		}
		
		@Override
		public boolean needsResponse()
		{
			return true;
		}
		
		public String name() 
		{
			return "songInfoTuple";
		}
	}
	
	public static class CurrentTimeCommand extends Command
	{
		private PlaybackManager manager;
		
		public CurrentTimeCommand(PlaybackManager pbManager) {
			manager = pbManager;
		}
		
		@Override
		public void execute()
		{
			manager.setTime( Integer.parseInt(getResponse()) );
		}
		
		@Override
		public boolean needsResponse()
		{
			return true;
		}
		
		public String name() 
		{
			return "currentTime";
		}
	}
	
	public static class JumpToTimeCommand extends Command
	{
		public String name() {
			return "jumpToTime";
		}
	}
}
