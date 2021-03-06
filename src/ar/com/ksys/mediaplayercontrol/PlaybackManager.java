package ar.com.ksys.mediaplayercontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ar.com.ksys.mediaplayercontrol.PlayerCommands.*;

public class PlaybackManager extends Observable implements Observer 
{
    private CommandManager cm;
    private Song currentSong;
    private List<Song> playlist;
    private boolean is_shuffle;
    private boolean is_repeat;
    private int volume;
    private int time;
    private int playlistLength;
    private boolean playlistUpdating;
    private boolean playlistChanged;

    public PlaybackManager(CommandManager cm) 
    {
        this.cm = cm;
        currentSong = new Song();
        playlist = new ArrayList<Song>();
    }

    public void play() 
    {
        cm.sendCommandToPlayer( new PlayCommand() );
    }

    public void pause() 
    {
        cm.sendCommandToPlayer( new PauseCommand() );
    }

    public void playPause() 
    {
        cm.sendCommandToPlayer( new PlayPauseCommand() );
    }

    public void stop() 
    {
        cm.sendCommandToPlayer( new StopCommand() );
    }

    public void next() 
    {
        cm.sendCommandToPlayer( new NextCommand() );
        updateStatusSmall();
    }

    public void prev() 
    {
        cm.sendCommandToPlayer( new PrevCommand() );
        updateStatusSmall();
    }

    public Song getCurrentSong() 
    {
        return currentSong;
    }

    public void notifySongChanged()
    {
        updateObservers();
    }
    
    public void setCurrentSong(int position) 
    {
        cm.sendCommandToPlayer(new SetCurrentPositionCommand(), position);
        play();
        
        // Force an update in the UI
        currentSong = playlist.get(position);
        time = 0;
        updateObservers();
    }

    public boolean isShuffle() 
    {
        return is_shuffle;
    }

    public void setShuffle(boolean is_shuffle, boolean update) 
    {
        this.is_shuffle = is_shuffle;
        if(update)
            cm.sendCommandToPlayer(new SetShuffleCommand(), is_shuffle);
    }

    public boolean isRepeat() 
    {
        return is_repeat;
    }

    public void setRepeat(boolean is_repeat, boolean update) 
    {
        this.is_repeat = is_repeat;
        if(update)
            cm.sendCommandToPlayer(new SetRepeatCommand(), is_repeat);
    }

    public int getVolume() 
    {
        return volume;
    }

    public void setVolume(int volume, boolean update) 
    {
        this.volume = volume;
        if(update)
            cm.sendCommandToPlayer(new SetVolumeCommand(), volume);
    }

    public int getTime() 
    {
        return time;
    }

    public void setTime(int time, boolean update) 
    {
        this.time = time;
        if(update)
            cm.sendCommandToPlayer(new JumpToTimeCommand(), time);
    }

    public int getPlaylistLength() 
    {
        return playlistLength;
    }

    public void setPlaylistLength(int playlistLength) 
    {
        this.playlistLength = playlistLength;
    }

    public List<Song> getPlaylist() 
    {
        return playlist;
    }

    public boolean isPlaylistChanged() 
    {
        return playlistChanged;
    }

    @Override
    public void update(Observable observable, Object data) 
    {
        updateStatus();
        updateObservers();

        // playListChanged: this flag will be set during only one 
        // call to notify(). After that, we clear it.
        if( playlistChanged )
            playlistChanged = false;

        // If we finished populating the items, set the flag indicating 
        // we're done updating the playlist and that it has changed. 
        if( playlistUpdating && playlistLength == playlist.size() ) {
            playlistUpdating = false;
            playlistChanged = true;
        }

        // Check if the playlist changed and we're not currently updating it
        if( playlistLength != playlist.size() && !playlistUpdating ) {
            playlistUpdating = true;
            playlist.clear();
            for(int i = 0; i < playlistLength; i++) {
                cm.sendCommandToPlayer(new SongInfoCommand(playlist), i);
            }
        }
    }
    
    private void updateStatus() 
    {
        cm.sendCommandToPlayer( new CurrentTimeCommand(this) );
        cm.sendCommandToPlayer( new CurrentPositionCommand(this) );
        cm.sendCommandToPlayer( new SongInfoCommand(this) );
        cm.sendCommandToPlayer( new IsRepeatCommand(this) );
        cm.sendCommandToPlayer( new IsShuffleCommand(this) );
        cm.sendCommandToPlayer( new PlaylistLengthCommand(this) );
        cm.sendCommandToPlayer( new VolumeCommand(this) );
    }
    
    private void updateStatusSmall()
    {
        cm.sendCommandToPlayer( new CurrentTimeCommand(this) );
        cm.sendCommandToPlayer( new CurrentPositionCommand(this) );
        cm.sendCommandToPlayer( new SongInfoCommand(this) );
    }
    
    private void updateObservers()
    {
        setChanged();
        notifyObservers();
    }
}
