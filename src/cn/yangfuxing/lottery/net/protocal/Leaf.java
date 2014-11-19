package cn.yangfuxing.lottery.net.protocal;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

/**
 * 简单的叶子节点
 * @author flystar
 *
 */
public class Leaf
{
	//１，节点的包含的内容
	//２，节点的序列化
	
	private String tagName;
	public String tagvalue;
	//优化：android如何处理javabean　　不建议使用getter setter
	//把能设为public 的字段设为public 
	
	public String getTagName()
	{
		return tagName;
	}
	
	public Leaf(String tagName, String tagvalue) {
		super();
		this.tagName = tagName;
		this.tagvalue = tagvalue;
	}

	public Leaf(String tagName) {
		super();
		this.tagName = tagName;
	}


	public void serializerLeaf(XmlSerializer serializer)
	{
		try {
			serializer.startTag(null, tagName);
			if(tagvalue == null)
			{
				tagvalue = "";
			}
			serializer.text(tagvalue);
			serializer.endTag(null, tagName);
		}
		catch (IllegalArgumentException | IllegalStateException | IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void setTagvalue(String tagvalue) {
		this.tagvalue = tagvalue;
	}
	
}
