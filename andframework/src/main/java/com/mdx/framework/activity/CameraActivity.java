package com.mdx.framework.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mdx.framework.Frame;
import com.mdx.framework.utility.BitmapRead;
import com.mdx.framework.utility.Device;
import com.mdx.framework.utility.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("deprecation")
public class CameraActivity extends NoTitleAct implements SurfaceHolder.Callback {

	private ImageView position;

	private SurfaceView surface;

	private ImageButton shutter;

	private SurfaceHolder holder;

	private Camera camera;

	private String filepath;

	private int cameraPosition;

	protected int cameraOrientation;

	View.OnClickListener listener;

	Camera.PictureCallback jpeg;

	private SensorManager sensorManager;

	private int nowRec = 0;

	private MySensorEventListener mySensorEventListener = new MySensorEventListener();

	public void init() {

	}

	@SuppressLint("NewApi")
	public void create(Bundle savedInstanceState) {
		getActivity().getWindow().addFlags(128);
		getActivity().setRequestedOrientation(1);
		setContentView(com.mdx.framework.R.layout.default_open_camera);
		position = (ImageView) findViewById(com.mdx.framework.R.id.camera_position);
		surface = (SurfaceView) findViewById(com.mdx.framework.R.id.myCameraView);
		shutter = (ImageButton) findViewById(com.mdx.framework.R.id.camera_shutter);
		sensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
		holder = surface.getHolder();
		holder.addCallback(this);
		holder.setType(3);
		position.setOnClickListener(listener);
		shutter.setOnClickListener(listener);
		int cameraCount = Camera.getNumberOfCameras();
		if (cameraCount == 1)
			position.setVisibility(8);
		if (getActivity().getIntent().getParcelableExtra("output") != null) {
			String file = getActivity().getIntent().getParcelableExtra("output").toString();
			filepath = file.substring(7);
		}
	}

	private final class MySensorEventListener implements SensorEventListener {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// 重力传感器
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				float x = event.values[SensorManager.DATA_X];
				float y = event.values[SensorManager.DATA_Y];
				// if(x>y)
				if (Math.abs(x) - 2 > Math.abs(y)) {
					if (x < 0) {
						nowRec = 180;
					} else {
						nowRec = 0;
					}
				} else if (Math.abs(y) - 2 > Math.abs(x)) {
					if (y < 0) {
						nowRec = 270;
					} else {
						nowRec = 90;
					}
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

	}

	protected void resume() {
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(mySensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
	}

	protected void pause() {
		sensorManager.unregisterListener(mySensorEventListener);
	}

	protected void destroy() {
		if (Frame.HANDLES.size() == 0) {
			System.exit(1);
		}
	}

	public CameraActivity() {
		filepath = "";
		cameraPosition = 1;
		cameraOrientation = 0;
		listener = new View.OnClickListener() {

			@SuppressLint("NewApi")
			public void onClick(View v) {
				if (v.getId() == com.mdx.framework.R.id.camera_position) {
					int cameraCount = 0;
					Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
					cameraCount = Camera.getNumberOfCameras();
					for (int i = 0; i < cameraCount; i++) {
						Camera.getCameraInfo(i, cameraInfo);
						if (cameraPosition == 1) {
							if (cameraInfo.facing != 1)
								continue;
							camera.stopPreview();
							camera.release();
							camera = null;
							camera = Camera.open(i);
							try {
								camera.setPreviewDisplay(holder);
							} catch (IOException e) {
								e.printStackTrace();
							}
							setStartCamera(0);
							cameraOrientation = 270;
							cameraPosition = 0;
							break;
						}
						if (cameraInfo.facing != 0)
							continue;
						camera.stopPreview();
						camera.release();
						camera = null;
						camera = Camera.open(i);
						try {
							camera.setPreviewDisplay(holder);
						} catch (IOException e) {
							e.printStackTrace();
						}
						setStartCamera(0);
						cameraPosition = 1;
						break;
					}

				} else if (v.getId() == com.mdx.framework.R.id.camera_shutter)
					camera.autoFocus(new Camera.AutoFocusCallback() {

						public void onAutoFocus(boolean success, Camera camera) {
							if (success) {
								Camera.Parameters params = camera.getParameters();
								params.setRotation(cameraOrientation);
								List<Size> list = params.getSupportedPictureSizes();
								Size si = Device.getNearSize(list, Device.getMeticsW(), Device.getMeticsH());
								params.setPictureSize(si.width, si.height);
								camera.setParameters(params);
								params.setPictureFormat(256);
								camera.setParameters(params);
								camera.takePicture(null, null, jpeg);
							}
						}
					});
			}
		};
		jpeg = new Camera.PictureCallback() {

			public void onPictureTaken(byte data[], Camera camera) {
				try {
					if (TextUtils.isEmpty(filepath)) {
						filepath = Util.getPath(getActivity(), "temp", (new StringBuilder("img")).append(System.currentTimeMillis()).append(".temp").toString()).getPath();
					}
					File file = new File(filepath);
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

					int roat = 0;
					if (bitmap.getWidth() < bitmap.getHeight()) {
						roat = 90;
					}
					roat = nowRec - roat;

					bitmap = BitmapRead.rotationImg(bitmap, roat);
					BitmapRead.saveImage(bitmap, new FileOutputStream(file));

					camera.stopPreview();
					camera.startPreview();
					Intent intent = new Intent();
					intent.setData(Uri.fromFile(file));
					intent.putExtra("selfcam", 1);
					Bitmap bit = BitmapRead.decodeSampledBitmapFromByte(data, 100F, 100F);
					intent.putExtra("data", bit);
					getActivity().setResult(Activity.RESULT_OK, intent);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
	}

	@SuppressLint("NewApi")
	private void setStartCamera(int addorg) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(0, info);
		int result = info.orientation % 360;
		cameraOrientation = result;
		Camera.Parameters params = camera.getParameters();
		List<Size> list = params.getSupportedPictureSizes();
		Size si = Device.getNearSize(list, Device.getMeticsW(), Device.getMeticsH());
		params.setPictureSize(si.width, si.height);
		camera.setParameters(params);
		camera.setDisplayOrientation(result);
		camera.startPreview();
	}

	protected void onDestroy() {
		super.onDestroy();
		if (Frame.HANDLES.size() == 0) {
			System.exit(1);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (camera == null) {
			try {
				camera = Camera.open();
				try {
					camera.setPreviewDisplay(holder);
					setStartCamera(0);
				} catch (IOException e) {
				}
			} catch (Exception e) {
				finish();
			}
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			camera.stopPreview();
			camera.release();
		} catch (Exception e) {
		}
		camera = null;
		holder = null;
		surface = null;
	}

}
