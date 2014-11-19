package cn.yangfuxing.lottery.engine.impl;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;
import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.engine.BaseEngine;
import cn.yangfuxing.lottery.engine.CommonInfoEngine;
import cn.yangfuxing.lottery.net.protocal.Message;
import cn.yangfuxing.lottery.net.protocal.element.CurrentIssueElement;
import cn.yangfuxing.lottery.util.DES;

public class CommonInfoEngineImpl extends BaseEngine implements CommonInfoEngine {

	@Override
	public Message getCurrentIssueInfo(Integer lotteryId) 
	{
		//xml
		CurrentIssueElement element = new CurrentIssueElement();
		element.getLotteryid().setTagvalue(lotteryId.toString());
		//Message
		Message message = new  Message();
		String xml = message.getXML(element);
		Message result = super.getResult(xml);
		if(result != null)
		{
			DES des = new DES();
			//解密
			String wholeBody = "<body>"+des.authcode(result.getBody().serviceBodyInsideDes, "ENCODE", ConstantValue.DES_PASSWORD)+"</body>";  //解密数据
			//解析返回的Body
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new StringReader(wholeBody));
				int eventType = parser.getEventType();
				String name = "";
				
				CurrentIssueElement resultElement = null;
				
				while(eventType != XmlPullParser.END_DOCUMENT)
				{
					switch (eventType)
					{
					case XmlPullParser.START_TAG:
						name = parser.getName();
						if("errorcode".equals(name))
						{
							result.getBody().oelement.errorcode = parser.nextText();
						}
						if("errormsg".equals(name))
						{
							result.getBody().oelement.errormsg = parser.nextText();
						}
						//判断是否含有element标签,如果有的话创建resultElement
						if("element".equals(name))
						{
							resultElement = new CurrentIssueElement();
							result.getBody().getElements().add(resultElement);
						}
						
						//解析特殊数据
						if("issue".equals(name))
						{
							if(resultElement != null)
							{
								resultElement.setIssue(parser.nextText());
							}
						}
						if("lasttime".equals(name))
						{
							if(resultElement != null)
							{
								resultElement.setLasstime(parser.nextText());
							}
						}
						break;

					default:
						break;
					}
					eventType = parser.next();
				}
				return result;
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return null;
	}

}
