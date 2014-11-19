package cn.yangfuxing.lottery.net.protocal;

/**
 *  回复结果的封装　
 * @author flystar
 *
 */
public class Oelement 
{
	public String errorcode;   // 错误码
	public String errormsg;   //错误信息
	public String getErrorcode() {
		return errorcode;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	
	
}
