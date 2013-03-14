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
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        EditText editIpAddress = (EditText)findViewById(R.id.editIpAddress);
        cm = new CommandManager(this, editIpAddress.getText().toString(), PORT);
        
        Button buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		cm.sendCommandToPlayer(new VolumeCommand());
        		cm.sendCommandToPlayer(new CurrentPositionCommand());
        		cm.sendCommandToPlayer(new SongTitleCommand());
        		cm.sendCommandToPlayer(new IsRepeatCommand());
        		cm.sendCommandToPlayer(new IsShuffleCommand());
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
				cm.sendCommandToPlayer(new SetVolumeCommand(), String.valueOf( seekBar.getProgress()));				
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
}
