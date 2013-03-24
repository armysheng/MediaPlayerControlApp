package ar.com.ksys.mediaplayercontrol;

import java.util.Observable;
import java.util.Observer;

import ar.com.ksys.mediaplayercontrol.PlayerCommands.*;

public class PlaybackManager extends Observable implements Observer {
	private CommandManager cm;
	private Song currentSong;
	private boolean is_playing;
	private boolean is_shuffle;
	private boolean is_repeat;
	private int volume;
	private int time;
	
	public PlaybackManager(CommandManager cm) {
		this.cm = cm;
		currentSong = new Song();
	}
	
	public void play() {
		cm.sendCommandToPlayer( new PlayCommand() );
	}
	
	public void pause() {
		cm.sendCommandToPlayer( new PauseCommand() );
	}
	
	public void playPause() {
		cm.sendCommandToPlayer( new PlayPauseCommand() );
	}
	
	public void stop() {
		cm.sendCommandToPlayer( new StopCommand() );
	}
	
	public void next() {
		cm.sendCommandToPlayer( new NextCommand() );
	}
	
	public void prev() {
		cm.sendCommandToPlayer( new PrevCommand() );
	}
	
	public void updateStatus() {
		cm.sendCommandToPlayer( new VolumeCommand(this) );
		cm.sendCommandToPlayer( new CurrentPositionCommand(currentSong) );
		cm.sendCommandToPlayer( new SongInfoCommand(currentSong) );
		cm.sendCommandToPlayer( new CurrentTimeCommand(this) );
		cm.sendCommandToPlayer( new IsRepeatCommand(this) );
		cm.sendCommandToPlayer( new IsShuffleCommand(this) );
	}
	
	public Song getCurrentSong() {
		return currentSong;
	}
	
	public void setCurrentSong(Song currentSong) {
		this.currentSong = currentSong;
	}
	
	public boolean isPlaying() {
		return is_playing;
	}
	
	public boolean isShuffle() {
		return is_shuffle;
	}
	
	public void setShuffle(boolean is_shuffle, boolean update) {
		this.is_shuffle = is_shuffle;
		if( update )
			cm.sendCommandToPlayer( new SetShuffleCommand(), 
									is_shuffle ? "true" : "false");
	}
	
	public boolean isRepeat() {
		return is_repeat;
	}
	
	public void setRepeat(boolean is_repeat, boolean update) {
		this.is_repeat = is_repeat;
		if( update )
			cm.sendCommandToPlayer( new SetRepeatCommand(),
									is_repeat ? "true" : "false");
	}
	
	public int getVolume() {
		return volume;
	}
	
	public void setVolume(int volume, boolean update) {
		this.volume = volume;
		if( update )
			cm.sendCommandToPlayer(new SetVolumeCommand(), String.valueOf(volume));
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time, boolean update) {
		this.time = time;
		if( update )
			cm.sendCommandToPlayer(new JumpToTimeCommand(), String.valueOf(time));
	}

	@Override
	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
	}
}
