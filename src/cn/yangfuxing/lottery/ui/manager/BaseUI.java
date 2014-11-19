package cn.yangfuxing.lottery.ui.manager;

import cn.yangfuxing.lottery.net.NetUtils;
import cn.yangfuxing.lottery.net.protocal.Message;
import cn.yangfuxing.lottery.util.PromptManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 *  所有需要显示在中间容器中内容的基类　
 * @author flystar
 *
 */
public abstract class  BaseUI implements View.OnClickListener {
	protected Context context;
	protected Bundle bundle;
	protected ViewGroup showInMiddle;   //显示在中间容器

	public BaseUI(Context context) {
	        super();
	        this.context = context;
	        init();
	        setListener();
        }
	
	
	
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}



	/**
	 * 需要显示在中间容器的内容
	 * @return
	 */
	public View getChild(){
		if(showInMiddle.getLayoutParams() == null)
		{
			showInMiddle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		}
		return showInMiddle;
	}
	
	public abstract int getId();
	
	
	@Override
        public void onClick(View v) {
        }
	
	/**
	 * 初始化：加载布局，　初始化控件
	 */
	protected abstract void init();
	
	/**
	 * 设置监听
	 */
	protected abstract void setListener();
	
	protected View findViewById(int id)
	{
		return showInMiddle.findViewById(id);
	}
	
	/**
	 * 访问网络的工具
	 * @author flystar
	 *
	 * @param <Params>
	 */
	public abstract class MyHttpTask<Params> extends AsyncTask<Params, Void, Message>
	{
		//无法复写父类的方法时的一个技巧
		//用于判断是否有网络
		public final AsyncTask<Params, Void, Message> executeProxy(Params... params) 
		{
			if(NetUtils.checkNet(context))
			{
				return super.execute(params);
			}
			else
			{
				PromptManager.showNoNetWork(context);
			}
			return null;
		  }			
	}

	/**
	 * 　要出去的时候调用
	 */
	public void onPause() {
	        // TODO Auto-generated method stub
	        
        }
	
	/**
	 * 进入到界面之后
	 */
	public void onResume() {
	        // TODO Auto-generated method stub
	        
        }
}
