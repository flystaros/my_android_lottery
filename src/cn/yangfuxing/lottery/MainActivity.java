package cn.yangfuxing.lottery;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.yangfuxing.lottery.ui.FirstUI;
import cn.yangfuxing.lottery.ui.Hall;
import cn.yangfuxing.lottery.ui.SecondUI;
import cn.yangfuxing.lottery.ui.manager.BaseUI;
import cn.yangfuxing.lottery.ui.manager.BottomManager;
import cn.yangfuxing.lottery.ui.manager.MiddleManager;
import cn.yangfuxing.lottery.ui.manager.TitleManager;
import cn.yangfuxing.lottery.util.FadeUtil;
import cn.yangfuxing.lottery.util.PromptManager;

public class MainActivity extends Activity 
{

	private RelativeLayout middle;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			changeUI(new SecondUI(MainActivity.this));
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//获取屏幕的宽度
		DisplayMetrics metrics = new  DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics );
		GlobalParams.WINDOW_WIDTH = metrics.widthPixels;
		
		init();
	}



	private void init() {
		TitleManager.getInstance().init(this);
	        TitleManager.getInstance().showUnloginTitle();
	        BottomManager.getInstance().init(this);
	        BottomManager.getInstance().showGameBottom();
	        
	        middle = (RelativeLayout) this.findViewById(R.id.ii_middle);
	        MiddleManager.getInstance().setMiddle(middle);
	        
	        MiddleManager.getInstance().addObserver(TitleManager.getInstance());
	        MiddleManager.getInstance().addObserver(BottomManager.getInstance());
	        
	        addFirstUi();
        }
	private View child1;
	private void addFirstUi() {
//		FirstUI firstUI = new FirstUI(this);
//		child1 = firstUI.getChild();
//	        middle.addView(child1);
//	        //两秒后切换第二个页面
//	        handler.sendEmptyMessageDelayed(10, 2000);
//		changeUI(new FirstUI(this));
//		MiddleManager.getInstance().changeUI(FirstUI.class);
		MiddleManager.getInstance().changeUI(Hall.class);
        }
	protected void changeUI(BaseUI targetUI) {
		middle.removeAllViews(); //删除上一个界面
		//跳转到第二个界面　
		View child2 = targetUI.getChild();
		middle.addView(child2);
		child2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.ia_view_change));  //开始切换动画
		
        }
	
	

	@Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			//操作返回键的集合，删除第一个，重新获取第一个元素，切换界面　
			boolean result = MiddleManager.getInstance().goBack();
			if(!result)
			{
				PromptManager.showExitSystem(this);
			}
			
			return false;
		}
	        return super.onKeyDown(keyCode, event);
        }



	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		return super.onCreateOptionsMenu(menu);
	}
	

}
