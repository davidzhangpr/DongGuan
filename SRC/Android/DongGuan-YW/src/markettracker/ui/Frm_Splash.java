package markettracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import orient.champion.business.R;

public class Frm_Splash extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		init();
	}

	private void init() {

		//两秒后调用此Runnable对象
		new Handler().postDelayed(new Runnable() {
			// @Override
			public void run() {	//151  50
				//跳转到登录页面
				Intent intent = new Intent(Frm_Splash.this, Frm_Login.class);
				startActivity(intent);
				//finish掉此页面
				Frm_Splash.this.finish();

				overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
			}
		}, 2000);
	}
}