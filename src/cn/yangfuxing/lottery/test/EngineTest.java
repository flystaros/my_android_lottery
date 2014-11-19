package cn.yangfuxing.lottery.test;

import cn.yangfuxing.lottery.bean.User;
import cn.yangfuxing.lottery.engine.UserEngine;
import cn.yangfuxing.lottery.engine.impl.UserEngineImpl;
import cn.yangfuxing.lottery.net.protocal.Message;
import cn.yangfuxing.lottery.util.BeanFactory;
import android.test.AndroidTestCase;
import android.util.Log;

public class EngineTest extends AndroidTestCase 
{
	public void testUserLogin()
	{
//		UserEngineImpl  engineImpl = new UserEngineImpl();
		UserEngine userEngine = BeanFactory.getImpl(UserEngine.class);
		User  user = new User();
		user.username = "fly";
		user.password = "fll";
		
		Message login = userEngine.login(user);
		
		String errorcode = login.getBody().oelement.errorcode;
		Log.i("FLY", "result:-----"+errorcode);
	}
}
