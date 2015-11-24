package markettracker.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
	private static final String TAG = "MyService";

	private static Thread timer = null;

	// MediaPlayer player;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// Toast.makeText(this, "My Service created", Toast.LENGTH_LONG).show();
		// Log.i(TAG, "onCreate");
		//
		// player = MediaPlayer.create(this, R.raw.braincandy);
		// player.setLooping(false);

		timer = new Thread() {
			public void run() {
				while (true) {
//					addServerTimes();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		};
	}

	@Override
	public void onDestroy() {
		// Toast.makeText(this, "My Service Stoped", Toast.LENGTH_LONG).show();
		// Log.i(TAG, "onDestroy");
		// player.stop();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		// Toast.makeText(this, "My Service Start", Toast.LENGTH_LONG).show();
		// Log.i(TAG, "onStart");
		// player.start();
	}
}