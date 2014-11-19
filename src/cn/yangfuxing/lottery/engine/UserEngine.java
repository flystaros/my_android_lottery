package cn.yangfuxing.lottery.engine;

import cn.yangfuxing.lottery.bean.User;
import cn.yangfuxing.lottery.net.protocal.Message;

/**
 * 用户业务
 * @author flystar
 *
 */
public interface UserEngine 
{
	/**
	 * 处理　登录　
	 * @param user
	 * @return
	 */
	Message login(User user);
	
	/**
	 * 查询余额
	 * @param user
	 * @return
	 */
	Message balance(User user);

	/**
	 * 获取用户余额
	 * @param user
	 * @return
	 */
	Message getBalance(User user);

	Message bet(User user);
}
