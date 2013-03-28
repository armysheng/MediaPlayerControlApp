package ar.com.ksys.mediaplayercontrol;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlaylistAdapter extends BaseAdapter 
{
    List<Song> playlist;
    Activity activity;

    public PlaylistAdapter(Activity activity, List<Song> playlist) 
    {
        this.playlist = playlist;
        this.activity = activity;
    }

    @Override
    public int getCount() 
    {
        return playlist.size();
    }

    @Override
    public Object getItem(int position) 
    {
        return playlist.get(position);
    }

    @Override
    public long getItemId(int position) 
    {
        return playlist.get(position).getTrackNumber();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        View rowView = convertView;

        if( rowView == null ) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.playlist_layout, parent, false);
        }

        TextView textSongTitle = (TextView)rowView.findViewById(R.id.textSongName);
        TextView textSongLength = (TextView)rowView.findViewById(R.id.textSongLength);

        Song song = playlist.get(position);
        textSongTitle.setText( song.getTrackNumber() + 1 + ". " + song.getTitle() );
        textSongLength.setText( UiUpdater.timeString(song.getLength()) );

        return rowView;
    }
}
