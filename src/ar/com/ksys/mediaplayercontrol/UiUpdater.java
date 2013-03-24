package ar.com.ksys.mediaplayercontrol;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.widget.*;

public class UiUpdater implements Observer
{
	private UiContainer container;
	
	private class UiContainer {
		public CheckBox checkRepeat;
		public CheckBox checkShuffle;
		public SeekBar volumeBar;
		public SeekBar songPosBar;
		public TextView textPos;
		public TextView textSong;
		public TextView textCurTime;
		public TextView textSongLength;
		
		UiContainer(Activity activity) {
			checkRepeat 	= (CheckBox)	activity.findViewById(R.id.checkRepeat);
			checkShuffle 	= (CheckBox)	activity.findViewById(R.id.checkShuffle);
			volumeBar 		= (SeekBar)		activity.findViewById(R.id.volumeBar);
			songPosBar 		= (SeekBar)		activity.findViewById(R.id.seekBarSongLength);
			textPos 		= (TextView)	activity.findViewById(R.id.textCurrentPos);
			textSong		= (TextView)	activity.findViewById(R.id.textCurrentSong);
			textCurTime		= (TextView)	activity.findViewById(R.id.textSongTime);
			textSongLength	= (TextView)	activity.findViewById(R.id.textSongLength);
		}
	}
	
	// Helper method
	private static String timeString(int timeInSeconds)
	{
		int seconds = timeInSeconds % 60;
		int minutes = (timeInSeconds -  seconds) / 60;
		return minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
	}
	
	public UiUpdater(Activity activity) 
	{
		container = new UiContainer(activity);
	}
	
	public void updateUi(PlaybackManager playback) {
		int songPos = playback.getTime() / 1000;
		Song song = playback.getCurrentSong();
		
		container.checkRepeat.setChecked( playback.isRepeat() );
		container.checkShuffle.setChecked( playback.isShuffle() );
		container.volumeBar.setProgress( playback.getVolume() );
		container.songPosBar.setProgress( songPos );
		container.textCurTime.setText( timeString(songPos) );		
		container.textPos.setText( song.getTrackNumber() + 1 + "." );
		container.textSong.setText( song.getTitle() );
		container.textSongLength.setText( timeString(song.getLength()) );
		container.songPosBar.setMax( song.getLength() );
	}

	@Override
	public void update(Observable observable, Object data) {
		updateUi( (PlaybackManager)observable );
	}
}
