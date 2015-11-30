package com.example.phone2dbox;

        import android.content.Context;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;

        import java.util.List;


public class SwingListener
        implements SensorEventListener {
    private SensorManager mSensorManager;
    private OnSwingListener mListener;
    private Sensor mAccelerometer;

    private long mPreTime;
    private float[] nValues = new float[3];
    private float[] oValues = {0.0f,0.0f,0.0f};

    private int mSwingCount=0;

    private static final int LI_SWING = 50;
    private static final int CNT_SWING = 3;

    public SwingListener(Context context) {

        mSensorManager = (SensorManager)context.getSystemService(
                Context.SENSOR_SERVICE);
    }
    public interface OnSwingListener {
        void onSwing();
    }


    public void setOnSwingListener(OnSwingListener listener) {
        mListener = listener;
    }
    public void registSensor() {
        List<Sensor> list = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (list.size()>0) {
            mAccelerometer = list.get(0);
        }
        if (mAccelerometer!=null) {
            mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_GAME);
        }
    }
    public void unregistSensor() {
        if (mAccelerometer!=null) {
            mSensorManager.unregisterListener(this);
        }
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO 自動生成されたメソッド・スタブ

    }

    public void onSensorChanged(SensorEvent event) {
        // TODO 自動生成されたメソッド・スタブ
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

        long curTime = System.currentTimeMillis();	// ミリ秒
        long diffTime = curTime - mPreTime;
        // 物凄い頻度でイベントが発生するので
        // 100msに1回計算するように間引く
        if (diffTime > 100) {
            // 現在の値をとって
            nValues[0] = event.values[0];
            nValues[1] = event.values[1];
            nValues[2] = event.values[2];
            float speed = (Math.abs(nValues[0] - oValues[0]) + Math.abs(nValues[1] - oValues[1])
                    + Math.abs(nValues[2] - oValues[2])) / diffTime * 1000;

            if (speed > LI_SWING) {
                mSwingCount++;
                if (mSwingCount > CNT_SWING) {
                    if (mListener != null) {
                        mListener.onSwing();
                    }
                    mSwingCount = 0;
                }
            } else {
                mSwingCount = 0;

            }
            mPreTime = curTime;
            oValues[0]=nValues[0];
            oValues[1]=nValues[1];
            oValues[2]=nValues[2];
        }
    }
}

