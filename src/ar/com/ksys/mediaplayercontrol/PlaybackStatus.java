package ar.com.ksys.mediaplayercontrol;

public class PlaybackStatus {
	private Song currentSong;
	private boolean is_playing;
	private boolean is_shuffle;
	private boolean is_repeat;
	private int volume;
	private int time;
	
	public PlaybackStatus() {
		currentSong = new Song();
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
	
	public void setShuffle(boolean is_shuffle) {
		this.is_shuffle = is_shuffle;
	}
	
	public boolean isRepeat() {
		return is_repeat;
	}
	
	public void setRepeat(boolean is_repeat) {
		this.is_repeat = is_repeat;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
}