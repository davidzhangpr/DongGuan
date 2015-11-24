package markettracker.comm;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;


import markettracker.util.Tool;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class getServerTimeSoapResponse implements Response {

	private long result;

	private static final String RESULT = "GetServerTimesResult";

	// private static final String ERRCODE = "errorCode";
	// private static final String SUCCESS = "success";
	// private static final String DONE = "done";
	// private static final String ERRMSG = "errorMsg";
	// private static final String ISALL = "isAll";
	// private static final String NEXTSTARTROW = "nextStartRow";
	//
	// private static final String DATATABLE = "Table";
	// private static final String TYPE = "type";

	public getServerTimeSoapResponse() {
	}

	public getServerTimeSoapResponse(String responseXML) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(responseXML));
			int eventType = xpp.getEventType();
//			boolean inRecord = false;
//			SObject sObject = null;
			String name, value;
			boolean done = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					name = xpp.getName();
					// System.out.println(name);
					if (name.endsWith(RESULT)) {
						value = xpp.nextText();
						Date ddd=Tool.string2Date(value.replace("T", " "));
						result =ddd.getTime();// Long.parseLong(value);
					}
		
					break;
				case XmlPullParser.END_TAG:

					break;
				}
				eventType = xpp.next();
			}
			this.setResult(result);
		} catch (XmlPullParserException xppe) {
			xppe.printStackTrace();
			throw new RuntimeException(xppe);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException(ioe);
		}

	}

	public long getResult() {
		return this.result;
	}

	public void setResult(long queryResult) {
		this.result = queryResult;
	}

	public Response getSoapResponse() {
		return this;
	}
}
