package cn.yangfuxing.lottery.net.protocal.element;

import org.xmlpull.v1.XmlSerializer;

import cn.yangfuxing.lottery.net.protocal.Element;
import cn.yangfuxing.lottery.net.protocal.Leaf;

/**
 * 获取当前销售期信息
 * @author flystar
 *
 */
public class CurrentIssueElement extends Element 
{
	private Leaf  lotteryid =  new Leaf("lotteryid");
	private Leaf issues = new Leaf("issues","1");

	//服务器回复的数据
	private String issue;  //期次
	private String lasstime; //剩余时间
	
	
	
	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getLasstime() {
		return lasstime;
	}

	public void setLasstime(String lasstime) {
		this.lasstime = lasstime;
	}

	@Override
	public String getTransactiontype() 
	{
		return "12002";
	}

	@Override
	public void serializerElement(XmlSerializer serializer) 
	{
		try {
			serializer.startTag(null, "element");
			lotteryid.serializerLeaf(serializer);
			issues.serializerLeaf(serializer);
			serializer.endTag(null, "element");
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}

	}
	
	public Leaf getLotteryid() {
		return lotteryid;
	}

}
