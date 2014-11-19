package cn.yangfuxing.lottery.net.protocal;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;
import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.util.DES;

public class Body 
{
	private ArrayList<Element> elements = new ArrayList<Element>();
	
	public String serviceBodyInsideDes;    //服务器回复body里面采用DES加密的数据
	public Oelement oelement = new Oelement();  //服务器返回
	public ArrayList<Element> getElements() {
		return elements;
	}


	public Oelement getOelement() {
		return oelement;
	}


	public void serializerBody(XmlSerializer serializer)
	{
		try {
			
			serializer.startTag(null, "body");
			serializer.startTag(null, "elements");
			
			for (Element item : elements)
			{
				item.serializerElement(serializer);
			}
			
			serializer.endTag(null, "elements");
			serializer.endTag(null, "body");
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	//   digest生成需要完整的ｂｏｄｙ
	public String getWholeBody()
	{
		XmlSerializer tempSerializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			tempSerializer.setOutput(writer);
			serializerBody(tempSerializer);
			tempSerializer.flush();
			Log.i("FLY", "writer"+writer.toString());
			return writer.toString();
			
		} catch (IllegalArgumentException | IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *  加密body里面的数据
	 */
	public String getBodyInsideDes()
	{
		Log.i("FLY","wholeBody++++++++++++++"+getWholeBody());
		String bodyInside  = StringUtils.substringBetween(getWholeBody(), "<body>","</body>");
		Log.i("FLY", "body++++++---------"+bodyInside);
		DES des = new DES();
		return des.authcode(bodyInside, "DECODE", ConstantValue.DES_PASSWORD);  //加密
	}
	
}
