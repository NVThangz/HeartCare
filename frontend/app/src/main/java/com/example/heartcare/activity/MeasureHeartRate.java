package com.example.heartcare.activity;

import static com.example.heartcare.utilities.Calculations.bpm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.heartcare.R;
import com.example.heartcare.backend.Backend;
import com.example.heartcare.measure.CameraService;
import com.example.heartcare.measure.OutputAnalyzer;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class MeasureHeartRate extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private OutputAnalyzer analyzer;

    private final int REQUEST_CODE_CAMERA = 0;
    public static final int MESSAGE_UPDATE_REALTIME = 1;
    public static final int MESSAGE_UPDATE_FINAL = 2;
    public static final int MESSAGE_CAMERA_NOT_AVAILABLE = 3;

    private ImageView measureCircleHeart;
    private TextView tvPulse;
    private TextureView graphTextureView;
    private TextureView cameraTextureView;
    private TextView textViewBpm;

    private ImageView icBack;
    private ImageView icArrow;
    private ImageView imScale;

    private Animation animationHeart;
    public enum VIEW_STATE {
        MEASUREMENT,
        SHOW_RESULTS
    }

    private void map() {
        imScale = findViewById(R.id.im_scale);
        icArrow = findViewById(R.id.ic_arrow);
        measureCircleHeart = findViewById(R.id.measure_circle_heart);
        tvPulse = findViewById(R.id.tv_pulse);
        graphTextureView = findViewById(R.id.graphTextureView);
        cameraTextureView = findViewById(R.id.textureView2);
        textViewBpm = findViewById(R.id.bpm);
        icBack = findViewById(R.id.ic_back);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_heart_rate);
        map();

        graphTextureView.setOpaque(false);


        clickIcBack();
        setAnimationHeart();


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CODE_CAMERA);
    }

    private void clickIcBack() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setAnimationHeart() {
        animationHeart = AnimationUtils.loadAnimation(MeasureHeartRate.this, R.anim.zoom);
        animationHeart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // animation bắt đầu thực hiện
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // animation kết thúc
                // set animation khác sau khi animation này thực hiện xong
                Animation newAnimation = AnimationUtils.loadAnimation(MeasureHeartRate.this, R.anim.zoom_out);
                measureCircleHeart.startAnimation(newAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // animation lặp lại
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private final Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == MESSAGE_UPDATE_REALTIME) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    int curBpm = (int) Math.round((Double) obj.get("pulse"));
                    textViewBpm.setText(String.valueOf(curBpm));
                    tvPulse.setText(obj.get("pulse_content").toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            if (msg.what == MESSAGE_UPDATE_FINAL) {

                JSONObject obj = (JSONObject) msg.obj;
                try {
                    int curBpm = (int) Math.round((Double) obj.get("pulse"));
                    textViewBpm.setText(String.valueOf(curBpm));
                    tvPulse.setText(obj.get("pulse_content").toString());

                    // Di chuyển icArrow
                    int widthImScale = imScale.getWidth() - icArrow.getWidth();
                    double cur = (curBpm - 40.0) / (150.0 - 40.0) * widthImScale;

                    int currentX = icArrow.getLeft();
                    int currentY = icArrow.getTop();
                    int tmp = imScale.getLeft();
                    int newX = imScale.getLeft() + (int) cur;
                    int newY = currentY;

                    icArrow.animate()
                            .x(newX)
                            .y(newY)
                            .setDuration(1000)
                            .start();
                    icArrow.layout(newX, newY, newX + icArrow.getWidth(), newY + icArrow.getHeight());


                /*
                    Lưu giá trị bpm trong biến curBpm vào backend
                    Ghép backend
                */
                    Backend.createHistory(curBpm);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                setViewState(VIEW_STATE.SHOW_RESULTS);
            }

            if (msg.what == MESSAGE_CAMERA_NOT_AVAILABLE) {
                Log.println(Log.WARN, "camera", msg.obj.toString());

                measureCircleHeart.clearAnimation();
                animationHeart.setAnimationListener(null);
                tvPulse.setText(
                        R.string.camera_not_found
                );
                analyzer.stop();
            }
        }
    };

    private final CameraService cameraService = new CameraService(this, mainHandler);

    @Override
    protected void onResume() {
        super.onResume();

        analyzer = new OutputAnalyzer(this, graphTextureView, mainHandler);

        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();

        if (previewSurfaceTexture != null) {
            // this first appears when we close the application and switch back
            // - TextureView isn't quite ready at the first onResume.
            Surface previewSurface = new Surface(previewSurfaceTexture);

            // show warning when there is no flash
            if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Snackbar.make(
                        findViewById(R.id.constraintLayout),
                        getString(R.string.noFlashWarning),
                        Snackbar.LENGTH_LONG
                ).show();
            }

//            cameraService.start(previewSurface);
//            analyzer.measurePulse(cameraTextureView, cameraService);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraService.stop();
        if (analyzer != null) analyzer.stop();
        analyzer = new OutputAnalyzer(this, graphTextureView, mainHandler);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(
                        findViewById(R.id.constraintLayout),
                        getString(R.string.cameraPermissionRequired),
                        Snackbar.LENGTH_LONG
                ).show();
            }
        }
    }

    public void setViewState(VIEW_STATE state) {
        switch (state) {
            case MEASUREMENT:
                findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
                break;
            case SHOW_RESULTS:
                findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
                break;
        }
    }

    public void onClickNewMeasurement(View view) {
        onClickNewMeasurement();
    }

    public void onClickNewMeasurement() {
        measureCircleHeart.startAnimation(animationHeart);
        analyzer = new OutputAnalyzer(this, graphTextureView, mainHandler);

//        char[] empty = new char[0];
//        tvPulse.setText(empty, 0, 0);

        setViewState(VIEW_STATE.MEASUREMENT);

        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();

        if (previewSurfaceTexture != null) {
            Surface previewSurface = new Surface(previewSurfaceTexture);

            cameraService.start(previewSurface);
            analyzer.measurePulse(cameraTextureView, cameraService);
        }
    }
}
