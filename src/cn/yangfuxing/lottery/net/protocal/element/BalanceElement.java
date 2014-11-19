package cn.yangfuxing.lottery.net.protocal.element;

import org.xmlpull.v1.XmlSerializer;

import cn.yangfuxing.lottery.net.protocal.Element;

public class BalanceElement extends Element {

	@Override
	public String getTransactiontype() {
		return "11007";
	}

	@Override
	public void serializerElement(XmlSerializer serializer) {

	}
	
	/********服务器返回的数据*********************/
	
	private String  investvalues;

	public String getInvestvalues() {
		return investvalues;
	}

	public void setInvestvalues(String investvalues) {
		this.investvalues = investvalues;
	}
	
	
	/*******************************************/
	

}
