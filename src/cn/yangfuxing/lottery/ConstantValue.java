package cn.yangfuxing.lottery;

public interface ConstantValue 
{
	/**
	 *  代理标志
	 */
	String AGENTERID="889931";
	
	/**
	 *  xml文件来源
	 */
	String SOURCE="ivr";
	
	/**
	 * body里面的数据加密算法
	 */
	String COMPERESS="DES";
	
	String ENCODING = "utf-8";
	
	/**
	 * 子代理商的密钥(.so) JNI
	 */
	String AGENTER_PASSWORD = "9ab62a694d8bf6ced1fab6acd48d02f8";
	
	/**
	 * des加密用密钥
	 */
	String DES_PASSWORD = "9b2648fcdfbad80f";
	
	//服务器地址
	String LOTTERY_URI = "http://192.168.1.254:8080";
	//服务器返回成功状态码
	String SUCCESS = "0";
	
	//ＵＩ的ＩＤ
	int VIEW_FRIST=1;
	int VIEW_SECOND=2;
	
	int VIEW_HALL=10;
	int VIEW_SHOPPING=15;
	int VIEW_USERLOGIN=20;
	int VIEW_PREBET=25;
	int VIEW_PLAYSSQ=30;
	int VIEW_LOGIN = 35;
	
	//双色球的唯一标志
	int SSQ = 118;
}
