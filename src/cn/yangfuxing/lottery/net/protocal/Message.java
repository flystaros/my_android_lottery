package cn.yangfuxing.lottery.net.protocal;

import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import cn.yangfuxing.lottery.ConstantValue;
import android.util.Xml;

public class Message 
{
	//1,节点包含的内容
	//2,节点的序列化
	
	private Header header = new Header();
	private Body body = new Body();
		
	
	public void serializerMessage(XmlSerializer serializer)
	{
		try {
			serializer.startTag(null, "message");
			serializer.attribute(null, "version", "1.0");
			
			header.serializerHeader(serializer, body.getWholeBody());
//			body.serializerBody(serializer);
			serializer.startTag(null, "body");
			serializer.text(body.getBodyInsideDes());
			serializer.endTag(null, "body");
			
			
			serializer.endTag(null, "message");
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 *  获取某个请求用的xml
	 * @return
	 */
	public String getXML(Element element)
	{
		if(element == null)
		{
			throw new IllegalArgumentException("element != null");
		}
		XmlSerializer serializer = Xml.newSerializer();
		//处理请求数据
		header.getTransactiontype().tagvalue = element.getTransactiontype();
		body.getElements().add(element);
		try {
			StringWriter writer = new StringWriter();
			serializer.setOutput(writer);
			
			serializer.startDocument(ConstantValue.ENCODING, null);
			
			serializerMessage(serializer);
			
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	public Header getHeader() 
	{
		return header;
	}

	public Body getBody() {
		return body;
	}
	
	
	
	
	
}
