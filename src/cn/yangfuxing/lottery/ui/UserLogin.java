package cn.yangfuxing.lottery.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.GlobalParams;
import cn.yangfuxing.lottery.R;
import cn.yangfuxing.lottery.bean.User;
import cn.yangfuxing.lottery.engine.UserEngine;
import cn.yangfuxing.lottery.net.protocal.Message;
import cn.yangfuxing.lottery.net.protocal.Oelement;
import cn.yangfuxing.lottery.net.protocal.element.BalanceElement;
import cn.yangfuxing.lottery.ui.manager.BaseUI;
import cn.yangfuxing.lottery.ui.manager.MiddleManager;
import cn.yangfuxing.lottery.util.BeanFactory;
import cn.yangfuxing.lottery.util.PromptManager;

/**
 * 用户登录Ｖｉｅｗ
 * @author flystar
 *
 */

public class UserLogin extends BaseUI 
{
	private EditText username;
	private ImageView clear;    // 清空用户名
	private EditText password;
	private Button login;
	
	public UserLogin(Context context) {
	        super(context);
	        // TODO Auto-generated constructor stub
        }

	@Override
	public int getId() {
		return ConstantValue.VIEW_LOGIN;
	}

	@Override
	protected void init() {
		showInMiddle = (ViewGroup) ViewGroup.inflate(context, R.layout.il_user_login, null);
		
		username = (EditText) findViewById(R.id.ii_user_login_username);
		clear = (ImageView) findViewById(R.id.ii_clear);
		password = (EditText) findViewById(R.id.ii_user_login_password);
		
		login = (Button) findViewById(R.id.ii_user_login);
		
	}

	@Override
	protected void setListener() {
		username.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(username.getText().toString().length() > 0)
				{
					clear.setVisibility(View.VISIBLE);
				}
			}
		});
		
		clear.setOnClickListener(this);
		login.setOnClickListener(this);
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ii_clear:
			username.setText("");
			clear.setVisibility(View.INVISIBLE);
			break;
		case R.id.ii_user_login:
			//数据检查　
			if(checkUserInfo())
			{
				//登录
				User user = new User();
				user.setUsername(username.getText().toString());
				user.setPassword(password.getText().toString());
				new MyHttpTask<User>() 
				{
					@Override
					protected void onPreExecute() {
						PromptManager.showProgressDialog(context);
					        super.onPreExecute();
					}
					
					@Override
                                        protected Message doInBackground(User... params) {
						UserEngine engine = BeanFactory.getImpl(UserEngine.class);
						Message login = engine.login(params[0]);
						
						if(login != null)
						{
							Oelement oelement = login.getBody().getOelement();
							if(ConstantValue.SUCCESS.equals(oelement.getErrorcode()))
							{
								//登录成功
								GlobalParams.isLogin = true;
								GlobalParams.USERNAME=params[0].getUsername();
								//获取余额　
								Message balance = engine.getBalance(params[0]);			
								
								if(balance != null)
								{
									oelement = balance.getBody().getOelement();
									if(ConstantValue.SUCCESS.equals(oelement.getErrorcode()))
									{
										BalanceElement element = (BalanceElement) balance.getBody().getElements().get(0);
										GlobalParams.MONEY = Float.parseFloat(element.getInvestvalues());
										return balance;
									}
								}
							}
						}
						
	                                        return null;
                                        }
					
					@Override
					protected void onPostExecute(Message result) {
						PromptManager.closeProgressDialog();
						if(result != null)
						{
							//登录跳转
							PromptManager.showToast(context, "登录成功");
							MiddleManager.getInstance().goBack();
							
						}
						else
						{
							PromptManager.showToast(context, "服务器忙．．．．");
						}
					        super.onPostExecute(result);
					}
				}.executeProxy(user);
			}
			break;

		default:
			break;
		}
	}

	private boolean checkUserInfo() {
	        return false;
        }

}
