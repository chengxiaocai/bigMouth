package com.bigmouth.app.scan;

import java.io.IOException;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigmouth.app.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

public class MipcaCaptureActivity extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private Button back;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	// <add by Yongfeng.zhang 2014.8.29
	private Button mOpenLightBtn;
	private boolean isFlashlightOpen;
	private TextView mFlashlightStatusTv;

	// >end by Yongfeng.zhang

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_scan);

		// MyApplication.getInstance().addActivity(this);

		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		/*
		 * this.back = (Button) findViewById(R.id.back);
		 * this.back.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { finish(); } });
		 */
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		// <add by Yongfeng.zhang 2014.8.29
		mFlashlightStatusTv = (TextView) findViewById(R.id.mFlashlightStatusTv);
		mOpenLightBtn = (Button) findViewById(R.id.mOpenLightBtn);
		mOpenLightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (isFlashlightOpen) {
					setFlashLightEnabled(false);

					isFlashlightOpen = false;

					mFlashlightStatusTv
							.setText(getString(R.string.flight_open));
				} else {
					setFlashLightEnabled(true);

					isFlashlightOpen = true;

					mFlashlightStatusTv
							.setText(getString(R.string.flight_close));
				}
			}
		});

		// TextView mTitleView=(TextView)findViewById(R.id.mTitleView);
		// mTitleView.setText("查询");

		/*
		 * Button leftBtn=(Button)findViewById(R.id.leftBtn);
		 * leftBtn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * finish(); } });
		 */
		// >end by Yongfeng.zhang

		// <add by Yongfeng.zhang 2014.9.16
		viewfinderView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.e("zyf", "viewfinderView touch touch touch.....");

				// finish();
			}
		});
		// >end by Yongfeng.zhang
	}

	// <add by Yongfeng.zhang 2014.8.29
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	@SuppressLint("NewApi")
	private void setFlashLightEnabled(boolean enable) {

		Camera camera = CameraManager.get().getCurCamera();

		Parameters parameter = camera.getParameters();

		if (enable) {
			parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
		} else {
			parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
		}

		camera.setParameters(parameter);
	}

	// >end by Yongfeng.zhang

	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) {
	 * 
	 * Log.e("zyf","touch touch touch......");
	 * 
	 * return super.onTouchEvent(event); }
	 */

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();

		// /MyApplication.getInstance().removeActivity(this);
	}

	/**
	 * 处理扫描结果
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(MipcaCaptureActivity.this, "Scan failed!",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
			// bundle.putParcelable("bitmap", barcode);
			resultIntent.putExtras(bundle);
			this.setResult(RESULT_OK, resultIntent);
		}
		MipcaCaptureActivity.this.finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}