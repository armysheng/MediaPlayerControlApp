package ar.com.ksys.mediaplayercontrol;

import java.util.List;

public class PlayerCommands 
{
    public static class PlayCommand extends Command
    {
        public String name() 
        {
            return "play";
        }
    }

    public static class PlayPauseCommand extends Command
    {
        public String name() 
        {
            return "playPause";
        }
    }

    public static class PauseCommand extends Command
    {
        public String name() 
        {
            return "pause";
        }
    }

    public static class StopCommand extends Command
    {
        public String name() 
        {
            return "stop";
        }
    }

    public static class NextCommand extends Command
    {
        public String name() 
        {
            return "next";
        }
    }

    public static class PrevCommand extends Command
    {
        public String name() 
        {
            return "previous";
        }
    }

    public static class SetShuffleCommand extends Command
    {
        public String name() 
        {
            return "setShuffle";
        }
    }

    public static class SetRepeatCommand extends Command
    {
        public String name() 
        {
            return "setRepeat";
        }
    }

    public static class SetVolumeCommand extends Command
    {
        public String name() 
        {
            return "setVolume";
        }
    }

    public static class VolumeCommand extends Command
    {
        private PlaybackManager manager;

        public VolumeCommand(PlaybackManager pbManager) 
        {
            manager = pbManager;
        }

        @Override
        public void execute()
        {
            manager.setVolume( Integer.parseInt(getResponse()), false );
        }

        public String name() 
        {
            return "volume";
        }
    }

    public static class SongTitleCommand extends Command
    {
        private Song song;

        public SongTitleCommand(Song s) 
        {
            song = s;
        }

        @Override
        public void execute()
        {
            song.setTitle( getResponse() );
        }

        public String name() 
        {
            return "songTitle";
        }
    }

    public static class CurrentPositionCommand extends Command
    {
        private Song song;

        public CurrentPositionCommand(Song s) 
        {
            song = s;
        }

        @Override
        public void execute()
        {
            song.setTrackNumber( Integer.parseInt(getResponse()) );
        }

        public String name() 
        {
            return "currentPos";
        }
    }

    public static class SetCurrentPositionCommand extends Command
    {
        public String name() 
        {
            return "setCurrentPos";
        }
    }

    public static class IsShuffleCommand extends Command
    {
        private PlaybackManager manager;

        public IsShuffleCommand(PlaybackManager pbManager) 
        {
            manager = pbManager;
        }

        @Override
        public void execute()
        {
            manager.setShuffle( getResponse().equalsIgnoreCase("true"), false );
        }

        public String name() 
        {
            return "isShuffle";
        }
    }

    public static class IsRepeatCommand extends Command
    {
        private PlaybackManager manager;

        public IsRepeatCommand(PlaybackManager pbManager) 
        {
            manager = pbManager;
        }

        @Override
        public void execute()
        {
            manager.setRepeat( getResponse().equalsIgnoreCase("true"), false );
        }

        public String name() 
        {
            return "isRepeat";
        }
    }

    public static class SongInfoCommand extends Command
    {
        private Song song;
        private List<Song> playlist;

        public SongInfoCommand(Song s) 
        {
            song = s;
        }

        public SongInfoCommand(List<Song> songList)
        {
            playlist = songList;
        }

        @Override
        public void execute()
        {
            String response = getResponse();
            int separatorPos = response.indexOf(",");

            int songLength = Integer.parseInt( response.substring(0, separatorPos) );
            String songTitle = response.substring(separatorPos + 1);

            if( hasArgs() ) {
                int trackNumber = Integer.parseInt( getArgs() );
                playlist.add( new Song(trackNumber, songLength, songTitle) );
            } 
            else {
                song.setLength(songLength);
                song.setTitle(songTitle);
            }
        }

        public String name() 
        {
            return "songInfoTuple";
        }
    }

    public static class CurrentTimeCommand extends Command
    {
        private PlaybackManager manager;

        public CurrentTimeCommand(PlaybackManager pbManager) 
        {
            manager = pbManager;
        }

        @Override
        public void execute()
        {
            manager.setTime( Integer.parseInt(getResponse()), false );
        }

        public String name() 
        {
            return "currentTime";
        }
    }

    public static class JumpToTimeCommand extends Command
    {
        public String name() 
        {
            return "jumpToTime";
        }
    }

    public static class PlaylistLengthCommand extends Command
    {
        private PlaybackManager manager;

        public PlaylistLengthCommand(PlaybackManager pbManager) 
        {
            manager = pbManager;
        }

        @Override
        public void execute() 
        {
            int length = Integer.parseInt( getResponse() );
            manager.setPlaylistLength(length);
        }

        public String name() 
        {
            return "playlistLength";
        }
    }
}
