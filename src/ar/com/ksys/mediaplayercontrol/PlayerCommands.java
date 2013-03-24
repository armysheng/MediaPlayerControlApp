package ar.com.ksys.mediaplayercontrol;

import android.app.Activity;
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
		private PlaybackStatus status;
		
		public VolumeCommand(PlaybackStatus pbStatus) {
			status = pbStatus;
		}
		
		@Override
		public void execute(Activity activity)
		{
			//SeekBar volumeBar = (SeekBar)activity.findViewById(R.id.volumeBar);
			//volumeBar.setProgress( Integer.parseInt( getResponse() ) );
			status.setVolume( Integer.parseInt(getResponse()) );
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
		public void execute(Activity activity)
		{
			//TextView textView = (TextView)activity.findViewById(R.id.textCurrentSong);
			//textView.setText( getResponse() );
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
		public void execute(Activity activity)
		{
//			TextView textCurrentPos = (TextView)activity.findViewById(R.id.textCurrentPos);
//			int reponse = Integer.parseInt(getResponse()) + 1;
//			textCurrentPos.setText( String.valueOf(reponse) + ".");
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
		private PlaybackStatus status;
		
		public IsShuffleCommand(PlaybackStatus pbStatus) {
			status = pbStatus;
		}
		
		@Override
		public void execute(Activity activity)
		{
//			CheckBox checkShuffle = (CheckBox)activity.findViewById(R.id.checkShuffle);
//			checkShuffle.setChecked(getResponse().equalsIgnoreCase("true"));
			status.setShuffle( getResponse().equalsIgnoreCase("true") );
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
		private PlaybackStatus status;
		
		public IsRepeatCommand(PlaybackStatus pbStatus) {
			status = pbStatus;
		}
		
		@Override
		public void execute(Activity activity)
		{
//			CheckBox checkRepeat = (CheckBox)activity.findViewById(R.id.checkRepeat);
//			checkRepeat.setChecked(getResponse().equalsIgnoreCase("true"));
			status.setRepeat( getResponse().equalsIgnoreCase("true") );
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
		public void execute(Activity activity)
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
//				TextView textSongLength = (TextView)activity.findViewById(R.id.textSongLength);
//				textSongLength.setText(timeText);
//				
//				TextView textCurrentSong = (TextView)activity.findViewById(R.id.textCurrentSong);
//				textCurrentSong.setText(songTitle);
//				
//				SeekBar seekBar = (SeekBar)activity.findViewById(R.id.seekBarSongLength);
//				seekBar.setMax(songLength);
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
		private PlaybackStatus status;
		
		public CurrentTimeCommand(PlaybackStatus pbStatus) {
			status = pbStatus;
		}
		
		@Override
		public void execute(Activity activity)
		{
			// time is in milliseconds
			int time = Integer.parseInt( getResponse() );// / 1000;
			
//			TextView textSongTime = (TextView)activity.findViewById(R.id.textSongTime);
//			textSongTime.setText( timeString(time) );
//			
//			SeekBar seekBar = (SeekBar)activity.findViewById(R.id.seekBarSongLength);
//			seekBar.setProgress(time);
			status.setTime(time);
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
