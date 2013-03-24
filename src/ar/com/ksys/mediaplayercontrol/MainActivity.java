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
	private PlaybackStatus playbackStatus;
	public ArrayAdapter<String> playlistAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        EditText editIpAddress = (EditText)findViewById(R.id.editIpAddress);
        cm = new CommandManager(this, editIpAddress.getText().toString(), PORT);
        
        playbackStatus = new PlaybackStatus();
        uiUpdater = new UiUpdater(playbackStatus, this);
        
        playlistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView playlist = (ListView)findViewById(R.id.listPlaylist);
        playlist.setAdapter(playlistAdapter);
        
        Button buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		cm.sendCommandToPlayer(new VolumeCommand(playbackStatus));
        		cm.sendCommandToPlayer(new CurrentPositionCommand( playbackStatus.getCurrentSong() ));
        		cm.sendCommandToPlayer(new SongInfoCommand( playbackStatus.getCurrentSong() ));
        		cm.sendCommandToPlayer(new CurrentTimeCommand(playbackStatus));
        		cm.sendCommandToPlayer(new IsRepeatCommand(playbackStatus));
        		cm.sendCommandToPlayer(new IsShuffleCommand(playbackStatus));
        		
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
				cm.sendCommandToPlayer(new SetShuffleCommand(), isChecked ? "true" : "false");
			}
		});
        
        CheckBox checkRepeat = (CheckBox)findViewById(R.id.checkRepeat);
        checkRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				cm.sendCommandToPlayer(new SetRepeatCommand() , isChecked ? "true" : "false");
			}
		});

        SeekBar volumeBar = (SeekBar)findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				cm.sendCommandToPlayer(new SetVolumeCommand(), String.valueOf( seekBar.getProgress() ));				
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
				cm.sendCommandToPlayer(new JumpToTimeCommand(), String.valueOf(seekBar.getProgress() * 1000));
				cm.sendCommandToPlayer(new CurrentTimeCommand( playbackStatus ));
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
				cm.sendCommandToPlayer(new PlayCommand());
			}
		});
        
        Button buttonPause = (Button)findViewById(R.id.buttonPause);
        buttonPause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cm.sendCommandToPlayer(new PlayPauseCommand());
			}
		});
        
        Button buttonStop = (Button)findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cm.sendCommandToPlayer(new StopCommand());
			}
		});
        
        Button buttonNext = (Button)findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cm.sendCommandToPlayer(new NextCommand());
			}
		});
        
        Button buttonPrev = (Button)findViewById(R.id.buttonPrev);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cm.sendCommandToPlayer(new PrevCommand());
			}
		});
    }
    
    @Override
    public void onResume() 
    {
    	uiUpdater.start();
    	super.onResume();
    }
    
    @Override
    public void onPause() 
    {
    	uiUpdater.stop();
    	super.onPause();
    }
}
