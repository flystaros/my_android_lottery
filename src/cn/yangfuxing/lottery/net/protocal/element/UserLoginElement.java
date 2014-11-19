package cn.yangfuxing.lottery.net.protocal.element;

import org.xmlpull.v1.XmlSerializer;

import cn.yangfuxing.lottery.net.protocal.Element;
import cn.yangfuxing.lottery.net.protocal.Leaf;

public class UserLoginElement extends Element 
{

	private Leaf actpassword = new Leaf("actpassword");
	
	@Override
	public String getTransactiontype() 
	{
		return "14001";
	}

	@Override
	public void serializerElement(XmlSerializer serializer) 
	{
		try {
			serializer.startTag(null, "element");
			actpassword.serializerLeaf(serializer);
			serializer.endTag(null, "element");
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public Leaf getActpassword() {
		return actpassword;
	}
	
	

}
