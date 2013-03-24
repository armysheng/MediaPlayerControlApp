package ar.com.ksys.mediaplayercontrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import ar.com.ksys.mediaplayercontrol.PlayerCommands.*;

public class MainActivity extends Activity
{
	private static final int PORT = 9696;
	
	private CommandManager cm;
	private UiUpdater uiUpdater;
	private TimerHandler timer;
	private PlaybackManager playback;
	public ArrayAdapter<String> playlistAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        EditText editIpAddress = (EditText)findViewById(R.id.editIpAddress);
        
        cm = new CommandManager(editIpAddress.getText().toString(), PORT);
        playback = new PlaybackManager(cm);
        uiUpdater = new UiUpdater(this);
        timer = new TimerHandler();
        
        playback.addObserver(uiUpdater);
        timer.addObserver(playback);
        
        playlistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView playlist = (ListView)findViewById(R.id.listPlaylist);
        playlist.setAdapter(playlistAdapter);
        
        Button buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		playback.updateStatus();
        		
        		if(!playlistAdapter.isEmpty())
        			return;
        		
        		//playlistAdapter.clear();
        		for(int i = 0; i < 30; i++) {
        			cm.sendCommandToPlayer(new SongInfoCommand(playlistAdapter), String.valueOf(i));
        		}
        	}
        });
        
        CheckBox checkShuffle = (CheckBox)findViewById(R.id.checkShuffle);
        checkShuffle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				playback.setShuffle(isChecked, true);
			}
		});
        
        CheckBox checkRepeat = (CheckBox)findViewById(R.id.checkRepeat);
        checkRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				playback.setRepeat(isChecked, true);
			}
		});

        SeekBar volumeBar = (SeekBar)findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				playback.setVolume(seekBar.getProgress(), true);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
		});
        
        SeekBar songPosBar = (SeekBar)findViewById(R.id.seekBarSongLength);
        songPosBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				playback.setTime(seekBar.getProgress() * 1000, true);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
		});
        
        Button buttonPlay = (Button)findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playback.play();
			}
		});
        
        Button buttonPause = (Button)findViewById(R.id.buttonPause);
        buttonPause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playback.playPause();
			}
		});
        
        Button buttonStop = (Button)findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playback.stop();
			}
		});
        
        Button buttonNext = (Button)findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playback.next();
			}
		});
        
        Button buttonPrev = (Button)findViewById(R.id.buttonPrev);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playback.prev();
			}
		});
    }
    
    @Override
    public void onResume() 
    {
    	timer.start();
    	super.onResume();
    }
    
    @Override
    public void onPause() 
    {
    	timer.stop();
    	super.onPause();
    }
}
