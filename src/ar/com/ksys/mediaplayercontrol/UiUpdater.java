package ar.com.ksys.mediaplayercontrol;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.widget.*;

public class UiUpdater implements Observer
{
    private UiContainer container;
    private boolean volumeUpdating;
    private boolean seekUpdating;

    private class UiContainer 
    {
        public CheckBox checkRepeat;
        public CheckBox checkShuffle;
        public SeekBar volumeBar;
        public SeekBar songPosBar;
        public TextView textPos;
        public TextView textSong;
        public TextView textCurTime;
        public TextView textSongLength;
        public ListView playlistView;

        public UiContainer(Activity activity) 
        {
            checkRepeat    = (CheckBox) activity.findViewById(R.id.checkRepeat);
            checkShuffle   = (CheckBox) activity.findViewById(R.id.checkShuffle);
            volumeBar      = (SeekBar)  activity.findViewById(R.id.volumeBar);
            songPosBar     = (SeekBar)  activity.findViewById(R.id.seekBarSongLength);
            textPos        = (TextView) activity.findViewById(R.id.textCurrentPos);
            textSong       = (TextView) activity.findViewById(R.id.textCurrentSong);
            textCurTime    = (TextView) activity.findViewById(R.id.textSongTime);
            textSongLength = (TextView) activity.findViewById(R.id.textSongLength);
            playlistView   = (ListView) activity.findViewById(R.id.listPlaylist);
        }
    }

    // Helper method
    public static String timeString(int timeInSeconds)
    {
        int seconds = timeInSeconds % 60;
        int minutes = (timeInSeconds -  seconds) / 60;
        return minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }

    public UiUpdater(Activity activity) 
    {
        container = new UiContainer(activity);
    }

    public void updateUi(PlaybackManager playback ) 
    {
        int songPos = playback.getTime() / 1000;
        Song curSong = playback.getCurrentSong();

        container.checkRepeat.setChecked( playback.isRepeat() );
        container.checkShuffle.setChecked( playback.isShuffle() );
        container.textCurTime.setText( timeString(songPos) );       
        container.textPos.setText( curSong.getTrackNumber() + 1 + "." );
        container.textSong.setText( curSong.getTitle() );
        container.textSongLength.setText( timeString(curSong.getLength()) );
        container.songPosBar.setMax( curSong.getLength() );

        if(!volumeUpdating)
            container.volumeBar.setProgress( playback.getVolume() );
        
        if(!seekUpdating)
            container.songPosBar.setProgress( songPos );

        if( playback.isPlaylistChanged() ) {
            BaseAdapter adapter = (BaseAdapter)container.playlistView.getAdapter();
            adapter.notifyDataSetChanged();
        }
    }
    
    public void setVolumeBarIsUpdating(boolean updating)
    {
        volumeUpdating = updating;
    }
    
    public void setSeekBarIsUpdating(boolean updating)
    {
        seekUpdating = updating;
    }

    @Override
    public void update(Observable observable, Object data) 
    {
        updateUi( (PlaybackManager)observable );
    }
}
