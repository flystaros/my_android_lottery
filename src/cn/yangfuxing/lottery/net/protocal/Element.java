package cn.yangfuxing.lottery.net.protocal;

import org.xmlpull.v1.XmlSerializer;

public abstract class Element 
{
	/*private Leaf  lotteryid =  new Leaf("lotteryid");
	private Leaf issues = new Leaf("issues","1");*/
	
	/**
	 *   处理请求的标识　
	 */
	public abstract String getTransactiontype();
/*	public String getTransactiontype()
	{
		return "12002";
		
	}*/
	
	public abstract void serializerElement(XmlSerializer serializer);
	
	/*public void serializerElement(XmlSerializer serializer)
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
	}*/

	/*public Leaf getLotteryid() {
		return lotteryid;
	}
	*/
}
