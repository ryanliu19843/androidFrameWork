package com.mdx.framework.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mdx.framework.R;
import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.widget.decodecode.ViewfinderView;
import com.mdx.framework.widget.decodecode.camera.CameraManager;
import com.mdx.framework.widget.decodecode.zxing.CaptureActivityHandler;
import com.mdx.framework.widget.decodecode.zxing.InactivityTimer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class DecodeCodeActivity extends Activity implements Callback {
    public final static String RECEIVE_DECODE_CODE = "RECEIVE_DECODE_CODE";
    
    private CaptureActivityHandler handler;
    
    private ViewfinderView viewfinderView;
    
    private boolean hasSurface;
    
    private Vector<BarcodeFormat> decodeFormats;
    
    private String characterSet;
    
    private InactivityTimer inactivityTimer;
    
    private MediaPlayer mediaPlayer;
    
    private boolean playBeep;
    
    private static final float BEEP_VOLUME = 0.10f;
    
    private boolean vibrate;
    
    private SurfaceView mSurfaceView;
    
    private boolean continuation = false;
    
    private HashMap<String, Long> readedCode = new HashMap<String, Long>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        continuation = getIntent().getBooleanExtra("continuation", continuation);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }
    
    public void init(){
        setContentView(R.layout.decodecode_act_scan);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
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
    }
    
    /**
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        String resultString = result.getText();
        
        if (!readedCode.containsKey(resultString) || System.currentTimeMillis()-readedCode.get(resultString) > 3000) {
            readedCode.put(resultString, System.currentTimeMillis() );
            if (resultString.equals("")) {
//                Toast.makeText(DecodeCodeActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
            } else {
                BIntent bIntent=new BIntent(RECEIVE_DECODE_CODE, "", null, 0, resultString);
                bIntent.obj1=barcode;
                BroadCast.PostBroad(bIntent);
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("result", resultString);
                bundle.putParcelable("bitmap", barcode);
                resultIntent.putExtras(bundle);
                this.setResult(RESULT_OK, resultIntent);
//                Toast.makeText(DecodeCodeActivity.this, "读取成功:" + resultString, Toast.LENGTH_SHORT).show();
            }
            playBeepSoundAndVibrate();
        }
        if (continuation) {
            // Start ourselves capturing previews and decoding.
            CameraManager.get().startPreview();
            handler.restartPreviewAndDecode();
        } else {
            DecodeCodeActivity.this.finish();
        }
    }
    
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder, mSurfaceView);
        }
        catch (IOException ioe) {
          	finish();
            return;
        }
        catch (RuntimeException e) {
        	finish();
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        
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
            
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.decodecode_beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            }
            catch (IOException e) {
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