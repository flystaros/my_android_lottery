package cn.yangfuxing.lottery.engine;

import cn.yangfuxing.lottery.net.protocal.Message;

/**
 * 公共数据处理
 * @author flystar
 *
 */
public interface CommonInfoEngine {
	/**
	 * 获取当前销售期的信息
	 * @param integer  彩票种类的标识
	 * @return
	 */
	Message getCurrentIssueInfo(Integer integer);

}
