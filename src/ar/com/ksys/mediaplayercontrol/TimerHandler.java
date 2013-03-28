package ar.com.ksys.mediaplayercontrol;

import java.lang.ref.WeakReference;
import java.util.Observable;

import android.os.Handler;
import android.os.Message;

public class TimerHandler extends Observable 
{
    private MyHandler handler;
    private boolean active;
    private int interval = 1000;

    // When Handler is an inner class it must be static, otherwise
    // there can be memory leaks. So I need another way to access the
    // TimerHandler from inside MyHandler. I could simply have had 
    // TimerHandler inherit from Handler, but I wanted it encapsulated.
    private static class MyHandler extends Handler 
    {
        private final WeakReference<TimerHandler> timerRef;

        MyHandler(TimerHandler th) 
        {
            timerRef = new WeakReference<TimerHandler>(th);
        }

        @Override
        public void handleMessage(Message msg) 
        {
            TimerHandler timer = timerRef.get();
            timer.setChanged();
            timer.notifyObservers();
            if(timer.isActive()) {
                sendEmptyMessageDelayed(0, timer.getInterval());
            }
        }	
    };

    public TimerHandler() 
    {
        handler = new MyHandler(this);
    }

    public void start()
    {
        if(active)
            return;
        active = true;
        handler.sendEmptyMessageDelayed(0, interval);
    }

    public void stop()
    {
        active = false;
    }

    public boolean isActive()
    {
        return active;
    }

    public int getInterval()
    {
        return interval;
    }
}
