package cn.yangfuxing.lottery.net.protocal.element;

import org.xmlpull.v1.XmlSerializer;

import cn.yangfuxing.lottery.net.protocal.Element;
import cn.yangfuxing.lottery.net.protocal.Leaf;

public class BetElement extends Element 
{
	//期号
	private Leaf issue = new Leaf("issue");
	//玩法编号
	private Leaf lotteryid = new Leaf("lotteryid");
	//投注号码，　注与注之间用＾分割
	private Leaf lotterycode = new Leaf("lotterycode");
	//注数
	private Leaf lotterynumber = new Leaf("lotterynumber");
	//方案金额
	private Leaf lotteryvalue = new Leaf("lotteryvalue");
	//倍数
	private Leaf appnumbers = new Leaf("appnumbers");
	//追期
	private Leaf issuesnumbers = new Leaf("issuesnumbers");
	//是否多期追号，0否，１多期
	private Leaf issueflag = new Leaf("issueflag");
	//
	private Leaf bonusstop = new Leaf("bonusstop","1");
	
	
	
	
	public Leaf getIssue() {
		return issue;
	}

	public Leaf getLotterynumber() {
		return lotterynumber;
	}

	public Leaf getLotteryvalue() {
		return lotteryvalue;
	}

	public Leaf getAppnumbers() {
		return appnumbers;
	}

	public Leaf getIssuesnumbers() {
		return issuesnumbers;
	}

	public Leaf getIssueflag() {
		return issueflag;
	}

	public Leaf getBonusstop() {
		return bonusstop;
	}

	public void setLotteryid(Leaf lotteryid) {
		this.lotteryid = lotteryid;
	}

	public void setLotterycode(Leaf lotterycode) {
		this.lotterycode = lotterycode;
	}

	public Leaf getLotteryid() {
		return lotteryid;
	}

	public Leaf getLotterycode() {
		return lotterycode;
	}

	@Override
	public String getTransactiontype() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*********************************************/
//	actvalue int * 用户账户余额
	private String actvalue;
	
	
	public String getActvalue() {
		return actvalue;
	}

	public void setActvalue(String actvalue) {
		this.actvalue = actvalue;
	}

	/*********************************************/

	@Override
	public void serializerElement(XmlSerializer serializer) {
		try {
			serializer.startTag(null, "element");
			lotteryid.serializerLeaf(serializer);
			issue.serializerLeaf(serializer);
			lotteryvalue.serializerLeaf(serializer);
			lotterynumber.serializerLeaf(serializer);
			appnumbers.serializerLeaf(serializer);
			issuesnumbers.serializerLeaf(serializer);
			lotterycode.serializerLeaf(serializer);
			issueflag.serializerLeaf(serializer);
			bonusstop.serializerLeaf(serializer);
			serializer.endTag(null, "element");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
