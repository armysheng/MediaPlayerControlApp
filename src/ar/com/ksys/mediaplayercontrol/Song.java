package ar.com.ksys.mediaplayercontrol;

public class Song 
{
    private int trackNumber;
    private int length;
    private String title;

    public Song() { }

    public Song(int trackNumber, int length, String title) 
    {
        this.trackNumber = trackNumber;
        this.length = length;
        this.title = title;
    }

    public int getTrackNumber() 
    {
        return trackNumber;
    }

    public void setTrackNumber(int tn) 
    {
        trackNumber = tn;
    }

    public int getLength() 
    {
        return length;
    }

    public void setLength(int l) 
    {
        length = l;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setTitle(String newTitle) 
    {
        title = newTitle;
    }
}
