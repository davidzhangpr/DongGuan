package markettracker.comm;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import markettracker.data.Fields;
import markettracker.data.SObject;
import markettracker.util.Constants;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class UpsertSObjectSoapRequest implements Request
{
	static final String ENV = "http://schemas.xmlsoap.org/soap/envelope/";
	static final String URN = "http://tempuri.org/";
	static final String ENVELOPE = "Envelope";
	
	static final String BODY = "Body";
	static final String UPSERT = "SubReportList";
	// static final String SOBJECTS_STRING = "request";
	
	static final String DATATABLE = "reportData";
	
	static final String HEADER = "Report_mainInfo";
	static final String DETAIL = "Report_detailInfo";
	static final String ATTACHMENT = "PhotoInfo";
	
	private String soapRequest = null;
	
	public UpsertSObjectSoapRequest(ArrayList<SObject> records)
	{
		this.soapRequest = createSoapRequest(records);
	}
	
	public String getRequest()
	{
		return this.soapRequest;
	}
	
	public void setRequest(String soapRequest)
	{
		this.soapRequest = soapRequest;
	}
	
	private String createSoapRequest(ArrayList<SObject> records)
	{
		XmlSerializer serializer = Xml.newSerializer();
		
		StringWriter writer = new StringWriter();
		// writer.
		try
		{
			
			serializer.setOutput(writer);
			serializer.startTag(ENV, ENVELOPE);
			
			serializer.startTag(ENV, BODY);
			
			if (records.get(0).getType().equals("msg"))
			{
				
				serializer.startTag(URN, "SubMsgStatus");
				serializer.startTag(URN, "empId");
				serializer.text(records.get(0).getField("userid"));
				serializer.endTag(URN, "empId");
				
				serializer.startTag(URN, "msgInfos");
//				serializer.startTag(URN, "MessageInfo");
//				
//				for (SObject data : records)
//				{
//					serializer.startTag(URN, "MsgId");
//					serializer.text(data.getField("MsgId"));
//					serializer.endTag(URN, "MsgId");
//				}
//				
//				serializer.endTag(URN, "MessageInfo");
//				
				
				
				for (SObject data : records)
				{
					serializer.startTag(URN, "MessageInfo");
					serializer.startTag(URN, "MsgId");
					serializer.text(data.getField("MsgId"));
					serializer.endTag(URN, "MsgId");
					serializer.endTag(URN, "MessageInfo");
				}
				
				serializer.endTag(URN, "msgInfos");
				
				serializer.endTag(URN, "SubMsgStatus");
				
			}
			else
			{
				serializer.startTag(URN, UPSERT);
				
				// serializer.startTag("", SOBJECTS_STRING);
				
				serializer.startTag(URN, "empId");
				serializer.text(records.get(0).getField("userid"));
				// serializer.text("1");
				serializer.endTag(URN, "empId");
				//
				serializer.startTag(URN, DATATABLE);
				Iterator<SObject> objs = records.iterator();
				Iterator<Fields> fieldList;
				// Fields fields;
				SObject obj = null;
				Fields requestFields = null;
				Iterator<String> itrFields = null;
				Set<String> fieldsString = null;
				String fieldName = null;
//				String value = "";
				while (objs.hasNext())
				{
					obj = objs.next();
					
					serializer.startTag(URN, HEADER);
					
					requestFields = obj.getFields();
					fieldsString = requestFields.keySet();
					itrFields = fieldsString.iterator();
					
					while (itrFields.hasNext())
					{
						fieldName = itrFields.next();
//						System.out.println(fieldName);
						if (!fieldName.equalsIgnoreCase("userid") && !fieldName.equalsIgnoreCase(Constants.TableInfo.TABLE_KEY))
						{
							if ((!(requestFields.getSValue(fieldName).equals(""))))
							{
//								value = requestFields.getSValue(fieldName);
								// if (!Tool.isConSpeCharacters(value))
								// {
								serializer.startTag(URN, fieldName);
//								System.out.println(value);
								try
								{
//									java.nio.charset.Charset.forName("gbk").newEncoder().canEncode("value");
									serializer.text(requestFields.getSValue(fieldName));
								}
								catch (Exception e)
								{
									// TODO: handle exception
								}
								// serializer.text(text)
								serializer.endTag(URN, fieldName);
								// }
							}
						}
					}
					serializer.startTag(URN, "EmpId");
					serializer.text(requestFields.getSValue("userid"));
					// serializer.text("1");
					serializer.endTag(URN, "EmpId");
					
					if (obj.getDetailfields() != null)
					{
						serializer.startTag(URN, "ReportDetailList");
						fieldList = obj.iteratorDetail();
						while (fieldList.hasNext())
						{
							serializer.startTag(URN, DETAIL);
							requestFields = fieldList.next();
							fieldsString = requestFields.keySet();
							itrFields = fieldsString.iterator();
							while (itrFields.hasNext())
							{
								fieldName = itrFields.next();
								if ((!(requestFields.getSValue(fieldName).equals(""))))
								{
									serializer.startTag(URN, fieldName);
									
									try
									{
//										java.nio.charset.Charset.forName("gbk").newEncoder().canEncode("value");
										serializer.text(requestFields.getSValue(fieldName));
									}
									catch (Exception e)
									{
										// TODO: handle exception
									}
									
//									serializer.text(requestFields.getSValue(fieldName));
									serializer.endTag(URN, fieldName);
								}
							}
							serializer.endTag(URN, DETAIL);
						}
						serializer.endTag(URN, "ReportDetailList");
					}
					
					if (obj.getAttfields() != null)
					{
						serializer.startTag(URN, "PhotoList");
						fieldList = obj.iteratorAtt();
						while (fieldList.hasNext())
						{
							serializer.startTag(URN, ATTACHMENT);
							requestFields = fieldList.next();
							fieldsString = requestFields.keySet();
							itrFields = fieldsString.iterator();
							while (itrFields.hasNext())
							{
								fieldName = itrFields.next();
								System.out.println(fieldName);
								if ((!(requestFields.getSValue(fieldName).equals(""))))
								{
									serializer.startTag(URN, fieldName);
									try
									{
//										java.nio.charset.Charset.forName("gbk").newEncoder().canEncode("value");
										serializer.text(requestFields.getSValue(fieldName));
									}
									catch (Exception e)
									{
										// TODO: handle exception
									}
									
//									serializer.text(requestFields.getSValue(fieldName));
									serializer.endTag(URN, fieldName);
								}
							}
							serializer.endTag(URN, ATTACHMENT);
						}
						serializer.endTag(URN, "PhotoList");
					}
					
					serializer.endTag(URN, HEADER);
					
				}
				serializer.endTag(URN, DATATABLE);
				
				serializer.endTag(URN, UPSERT);
			}
			serializer.endTag(ENV, BODY);
			serializer.endTag(ENV, ENVELOPE);
			serializer.endDocument();
			return writer.toString();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
