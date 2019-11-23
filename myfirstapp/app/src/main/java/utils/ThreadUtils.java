package utils;


import android.os.Handler;

import java.util.logging.LogRecord;

public class ThreadUtils {
    public static void runInThread(Runnable task){
        new Thread(task).start();
    }
    public static Handler mhnadler = new Handler();
    public static void runInUIThread(Runnable task){
        mhnadler.post(task);
    }

}
