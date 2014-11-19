package cn.yangfuxing.lottery.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

public abstract class ShakeListener implements SensorEventListener 
{
	private Context context;
	private Vibrator vibrator;
	
	public ShakeListener(Context context) {
	        super();
	        this.context = context;
	        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }

	private float lastX;
	private float lastY;
	private float lastZ;
	private long lasttime;
	private long duration = 100;
	private float total;  //累加的值
	private  float switchValue = 200;  //判断手机摇晃的阈值

	@Override
	public void onSensorChanged(SensorEvent event) {
		//判断是否是第一个店
		if(lasttime == 0)
		{
			//得到三个轴线上的加速度值
			lastX = event.values[SensorManager.DATA_X];
			lastY = event.values[SensorManager.DATA_Y];
			lastZ = event.values[SensorManager.DATA_Z];
			
			lasttime = System.currentTimeMillis();
		}
		else
		{
			long currenttime = System.currentTimeMillis();
			//尽可能的屏蔽掉不同手机之间的差异
			if((currenttime - lasttime) >= duration)
			{
				//第二个点以及以后的点
				float x = event.values[SensorManager.DATA_X];
				float y = event.values[SensorManager.DATA_Y];
				float z = event.values[SensorManager.DATA_Z];
				
				//计算两个点之间的增量
				
				float dx = Math.abs(x-lastX);
				float dy = Math.abs(y - lastY);
				float dz = Math.abs(z - lastZ);
				
				//屏蔽掉微小的增量
				if(dx < 1)
				{
					dx = 0;
				}
				if(dy < 1)
				{
					dy = 0;
				}
				if(dz < 0)
				{
					dz = 0;
				}
				
				if(dx == 0 | dy == 0 | dz == 0)
				{
					init();
				}
				
				//获取到总的增量
				float shake = dx + dy + dz;
				
				if(shake == 0)
				{
					init();
				}
				
				total += shake ;
				
				if(total >= switchValue)
				{
					//判断是摇晃手机，进行处理　
					//１，机选一注彩票
					randomCure();
					//２，提示用户，　声音或震动
					vibrator.vibrate(100);
					//３，所有数据的初始化　
					init();
				}
				else
				{
					//得到三个轴线上的加速度值
					lastX = event.values[SensorManager.DATA_X];
					lastY = event.values[SensorManager.DATA_Y];
					lastZ = event.values[SensorManager.DATA_Z];
					
					lasttime = System.currentTimeMillis();
				}
			}
			
			
		}
	}

	public  abstract void randomCure() ;

	private void init() {

		lastX = 0;
	        lastY = 0;
		lastZ = 0;
	        lasttime = 0;
		total = 0;  //累加的值
        }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

}
