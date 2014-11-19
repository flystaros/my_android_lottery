package cn.yangfuxing.lottery.ui.manager;


import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.GlobalParams;
import cn.yangfuxing.lottery.R;
import cn.yangfuxing.lottery.ui.SecondUI;
import cn.yangfuxing.lottery.ui.UserLogin;

public class TitleManager  implements Observer
{
	//单例设计模式
	//1,私有构造函数
	private TitleManager(){}
	private static TitleManager  instance = new  TitleManager();
	
	
	public static TitleManager getInstance() {
		return instance;
	}
	private RelativeLayout commonContainer;
	private RelativeLayout loginContainer;
	private RelativeLayout unLoginContainer;
	
	private ImageView login;
	private ImageView help;
	private ImageView goback;
	
	private TextView titleContent;
	private TextView userInfo;
	
	public void init(Activity activity)
	{
		commonContainer = (RelativeLayout) activity.findViewById(R.id.ii_common_container);
		unLoginContainer = (RelativeLayout) activity.findViewById(R.id.ii_unlogin_title);
		loginContainer = (RelativeLayout) activity.findViewById(R.id.ii_login_title);
		
		login = (ImageView) activity.findViewById(R.id.ii_title_login);
		help = (ImageView) activity.findViewById(R.id.ii_title_help);
		goback = (ImageView) activity.findViewById(R.id.ii_title_goback);
		
		titleContent = (TextView) activity.findViewById(R.id.ii_title_content);  
		userInfo = (TextView) activity.findViewById(R.id.ii_top_user_info);
		
		setListener();
	}
	
	
	private void setListener() {
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MiddleManager.getInstance().changeUI(UserLogin.class);
			}
		});
		
		help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		goback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		
        }
	
	private void initTitle()
	{
		commonContainer.setVisibility(View.GONE);
		unLoginContainer.setVisibility(View.GONE);
		loginContainer.setVisibility(View.GONE);
	}


	public void showCommonTitle()
	{
		initTitle();
		commonContainer.setVisibility(View.VISIBLE);
	}
	
	public void showUnloginTitle()
	{
		initTitle();
		unLoginContainer.setVisibility(View.VISIBLE);
	}
	
	public void showLoginTitle()
	{
		initTitle();
		loginContainer.setVisibility(View.VISIBLE);
	}
	
	public void changeTitle(String string)
	{
		titleContent.setText(string);
	}

	//观察者
	@Override
        public void update(Observable observable, Object data) {
	        if(data != null && StringUtils.isNumeric(data.toString()))
	        {
	        	int id = Integer.parseInt(data.toString());
	        	switch (id) {
			case ConstantValue.VIEW_FRIST:
				showUnloginTitle();
				break;
			case ConstantValue.VIEW_SECOND:
			case ConstantValue.VIEW_PLAYSSQ:
			case ConstantValue.VIEW_SHOPPING:
			case ConstantValue.VIEW_LOGIN:
			case ConstantValue.VIEW_PREBET:
				showCommonTitle();
				break;
			case ConstantValue.VIEW_HALL:
				if(GlobalParams.isLogin)
				{
					showLoginTitle();
					String info = "用户名" + GlobalParams.USERNAME + "\r\n" + "余额" + GlobalParams.MONEY;
					userInfo.setText(info);
				}
				else
				{
					showUnloginTitle();
				}
				break;
			default:
				break;
			}
	        }
        }
}
