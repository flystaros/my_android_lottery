package cn.yangfuxing.lottery.engine;

import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;
import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.net.HttpUtils;
import cn.yangfuxing.lottery.net.protocal.Message;
import cn.yangfuxing.lottery.util.DES;

/**
 * 　　业务的公共部分　
 * @author flystar
 *
 */
public abstract class BaseEngine 
{
	protected Message  getResult(String xml)
	{
		HttpUtils util = new HttpUtils();
		InputStream is = util.sendXML(ConstantValue.LOTTERY_URI, xml);
		if(is != null)
		{

			Message result = new Message();

			//解析返回的XML数据
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(is, ConstantValue.ENCODING);
				int eventtype = parser.getEventType();
				String name = "";
				while(eventtype != XmlPullParser.END_DOCUMENT)
				{
					switch (eventtype) 
					{
					case XmlPullParser.START_TAG:
						name = parser.getName();
						if("timestamp".equals(name))
						{
							result.getHeader().getTimestamp().tagvalue = parser.nextText();
						}
						if("body".equals(name))
						{
							result.getBody().serviceBodyInsideDes = parser.nextText();
						}
						if("digest".equals(name))
						{
							result.getHeader().getDigest().tagvalue = parser.nextText();
						}
						
						break;

					default:
						break;
					}
					eventtype = parser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//把body数据拼接　
			DES des = new DES();
			String wholeBody = "<body>"+des.authcode(result.getBody().serviceBodyInsideDes, "ENCODE", ConstantValue.DES_PASSWORD)+"</body>";  //解密数据
			//拼接ＭＤ５的原始数据
			String md5Data = result.getHeader().getTimestamp().tagvalue + ConstantValue.AGENTER_PASSWORD+wholeBody;
			
			String md5Mobile = DigestUtils.md5Hex(md5Data);
			if(md5Mobile.equals(result.getHeader().getDigest().tagvalue))
			{
				return result;
			}
			
			}
		return null;
        }
}
