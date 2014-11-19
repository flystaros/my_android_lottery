package cn.yangfuxing.lottery.util;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * 淡入淡出的切换效果
 * @author flystar
 *
 */
public class FadeUtil {
	
	private static  Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			View view = (View) msg.obj;
			ViewGroup parent = (ViewGroup) view.getParent();
			parent.removeView(view);
		}
		
	};
	
	/**
	 * 淡出效果
	 * @param view
	 * @param duration
	 */
	public static void fadeOut(final View view, long duration)
	{
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Message message = Message.obtain();
				message.obj = view;
				handler.sendMessage(message);
				/*ViewGroup parent = (ViewGroup) view.getParent();
				parent.removeView(view);*/
			}
		});
		alphaAnimation.setDuration(duration);
		alphaAnimation.setFillAfter(true);
		view.setAnimation(alphaAnimation);
	}
	
	public static void fadeIn(View view, long duration, long delay)
	{
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		//延时一段时间
		alphaAnimation.setStartOffset(delay);
		alphaAnimation.setDuration(duration);
		view.startAnimation(alphaAnimation);
	}
}
