package com.rel3.lixoconsciente.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.rel3.lixoconsciente.tasks.LocalizacaoTask;
import java.util.Timer;

public class NotificacaoService extends Service  {


    private LocalizacaoTask task;
    private Timer timer;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        task = new LocalizacaoTask(this);
        timer = new Timer();
        timer.schedule(task, 0, 120 * 1000);

        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        task.cancel();
        timer.cancel();
        super.onDestroy();
    }
}

