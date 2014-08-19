package ucsoftworks.com.bikestation.helpers;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;

public class UITimer {

    public static abstract class Task {
        public abstract void run();

        private Object object;
    }

    private Handler handler;
    private Map<String, Runnable> runnablesMap;

    public UITimer() {
        this.handler = new Handler(Looper.myLooper());
        this.runnablesMap = new HashMap<>();
    }

    public void schedule(final Task task, int delayMs, String tag) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                task.run();
            }
        };
        runnablesMap.put(tag, runnable);
        handler.postDelayed(runnable, delayMs);
    }

    public void schedule(final Task task, int delayMs, final int periodMs, String tag) {
        Runnable periodRunnable = new Runnable() {
            @Override
            public void run() {
                task.run();
                handler.postDelayed(this, periodMs);
            }
        };
        runnablesMap.put(tag, periodRunnable);
        handler.postDelayed(periodRunnable, delayMs);
    }

    public boolean cancel(String tag) {
        Runnable runnable = runnablesMap.remove(tag);
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            return true;
        }
        return false;
    }
}
