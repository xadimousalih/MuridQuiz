package com.tab28.quiz;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * @author xadimouSALIH
 * @Link http://www.tab28.com
 */
public class MyService extends Service {
	private static final String TAG = "Wolofalou Serigne Moussa Ka";
	MediaPlayer myPlayer;
	public static final int NOTIFICATION_ID = 1;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, this.getApplicationContext().getString(R.string.wolo), Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
		myPlayer = MediaPlayer.create(this, R.raw.jazahou);
		myPlayer.setLooping(false);
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, this.getApplicationContext().getString(R.string.wolo), Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
		myPlayer.stop();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, R.string.wolo, Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
		myPlayer.start();
	}

}
