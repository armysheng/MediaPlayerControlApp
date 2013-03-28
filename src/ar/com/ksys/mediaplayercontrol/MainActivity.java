package ar.com.ksys.mediaplayercontrol;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity
{
    private static final int PORT = 9696;

    private MessageManager messageManager;
    private CommandManager cm;
    private UiUpdater uiUpdater;
    private TimerHandler timer;
    private PlaybackManager playback;
    private PlaylistAdapter playlistAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        EditText editIpAddress = (EditText)findViewById(R.id.editIpAddress);

        ConnectivityManager connManager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);

        messageManager = new MessageManager(editIpAddress.getText().toString(), PORT, connManager);
        cm = new CommandManager(messageManager, this);
        playback = new PlaybackManager(cm);
        uiUpdater = new UiUpdater(this);
        timer = new TimerHandler();

        playback.addObserver(uiUpdater);
        timer.addObserver(playback);

        ListView playlistView = (ListView)findViewById(R.id.listPlaylist);
        playlistAdapter = new PlaylistAdapter(this, playback.getPlaylist());
        playlistView.setAdapter(playlistAdapter);

        playlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listview, View view, int position,
                    long id) {
                playback.setCurrentSong(position);
            }
        });

        Button buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                playback.updateStatus();
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

        Button buttonConnect = (Button)findViewById(R.id.buttonConnect);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( messageManager.isConnected() ) {
                    // TODO Maybe we should ask the user to try to connect again?
                    showAlertDialog("Error", "There is already an active connection");
                } else {
                    new HostConnectionTask().execute();
                }
            }
        });
    }

    @Override
    public void onResume() 
    {
        if( messageManager.isNetworkAvailable() &&
                messageManager.isConnected() && !timer.isActive() ) {
            timer.start();
        }
        super.onResume();
    }

    @Override
    public void onPause() 
    {
        timer.stop();
        super.onPause();
    }

    public void showAlertDialog(String title, String message) 
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }

    private class HostConnectionTask extends AsyncTask<Void, Void, Void> 
    {
        private Exception error;

        @Override
        protected Void doInBackground(Void... params) 
        {
            try {
                messageManager.renewConnection();
                messageManager.connect();
            } catch (IOException e) {
                error = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) 
        {
            if( error == null ) {
                showAlertDialog("Connected", "Connection to host successful.");
                timer.start();
            } else {
                showAlertDialog("Error", "Could not connect to host. " + error.getMessage());
            }
        }
    }
}
