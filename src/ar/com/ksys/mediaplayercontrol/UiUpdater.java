package ar.com.ksys.mediaplayercontrol;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.*;

public class UiUpdater extends Handler
{
	private boolean active;
	private int interval = 1000;
	private MyHandler handler;
	private PlaybackStatus playbackStatus;
	private UiContainer container;
	
	// When Handler is an inner class it must be static, otherwise
	// there can be memory leaks. So I need another way to access the
	// UiUpdater from inside MyHandler. I could simply have had UiUpdater
	// inherit from Handler, but I wanted it encapsulated.
	private static class MyHandler extends Handler {
		private final WeakReference<UiUpdater> uiUpdaterRef;
		
		MyHandler(UiUpdater u) {
			uiUpdaterRef = new WeakReference<UiUpdater>(u);
		}
		
		@Override
		public void handleMessage(Message msg) {
			UiUpdater ui = uiUpdaterRef.get();
			ui.updateUi();
			if(ui.isActive()) {
				sendEmptyMessageDelayed(0, ui.getInterval());
			}
		}	
	};
	
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
	
	public UiUpdater(PlaybackStatus playbackStatus, Activity activity) 
	{
		handler = new MyHandler(this);
		this.playbackStatus = playbackStatus;
		container = new UiContainer(activity);
	}
	
	public void updateUi() {
		int songPos = playbackStatus.getTime() / 1000;
		Song song = playbackStatus.getCurrentSong();
		
		container.checkRepeat.setChecked( playbackStatus.isRepeat() );
		container.checkShuffle.setChecked( playbackStatus.isShuffle() );
		container.volumeBar.setProgress( playbackStatus.getVolume() );
		container.songPosBar.setProgress( songPos );
		container.textCurTime.setText( timeString(songPos) );		
		container.textPos.setText( song.getTrackNumber() + 1 + "." );
		container.textSong.setText( song.getTitle() );
		container.textSongLength.setText( timeString(song.getLength()) );
		container.songPosBar.setMax( song.getLength() );
	}
	
	public void start()
	{
		if(active)
			return;
		active = true;
		handler.sendEmptyMessageDelayed(0, interval);
	}
	
	public void stop()
	{
		active = false;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public int getInterval()
	{
		return interval;
	}
}
