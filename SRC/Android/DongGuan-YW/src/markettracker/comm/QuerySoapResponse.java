package markettracker.comm;

import java.io.IOException;
import java.io.StringReader;

import markettracker.data.QueryResult;
import markettracker.data.SObject;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class QuerySoapResponse implements Response {

	private QueryResult queryResult;

	private static final String RESULT = "Result";
	private static final String ERRCODE = "errorCode";
	private static final String SUCCESS = "success";
	private static final String DONE = "done";
	private static final String ERRMSG = "errorMsg";
	private static final String ISALL = "isAll";
	private static final String NEXTSTARTROW = "nextStartRow";

	private static final String DATATABLE = "Table";
	private static final String TYPE = "type";

	public QuerySoapResponse() {
	}

	public QuerySoapResponse(String responseXML) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(responseXML));
			int eventType = xpp.getEventType();
			boolean inRecord = false;
			SObject sObject = null;
			String name, value;
			boolean done = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					name = xpp.getName();
//					System.out.println(name);
					if (name.endsWith(RESULT)) {
						queryResult = new QueryResult();
					} else if (name.equalsIgnoreCase(DATATABLE)) {
						sObject = new SObject();
						inRecord = true;
					} else if (sObject != null && inRecord) {
						value = xpp.nextText();
						sObject.setField(name, value);
					} else if (name.equalsIgnoreCase(SUCCESS)) {
						queryResult
								.setSuccess(Integer.parseInt(xpp.nextText()));
					} else if (name.equalsIgnoreCase(ISALL)) {
						queryResult.setAll(xpp.nextText());
					} else if (name.equalsIgnoreCase(NEXTSTARTROW)) {
						queryResult.setNextStartRow(xpp.nextText());
					} else if (name.equalsIgnoreCase(ERRCODE)) {
						queryResult.setErrorCode(xpp.nextText());
					} else if (name.equalsIgnoreCase(TYPE)) {
						queryResult.setType(xpp.nextText());
					} else if (name.equalsIgnoreCase(ERRMSG)||name.equalsIgnoreCase("msg")) {
						queryResult.setErrorMsg(xpp.nextText());
					} else if (name.equalsIgnoreCase(DONE)) {
						queryResult.setDone(Integer.parseInt(xpp.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					name = xpp.getName();
					if (name.endsWith(RESULT)) {
						done = true;
					} else if (name.equalsIgnoreCase(DATATABLE)) {
						inRecord=false;
						queryResult.setClientTable(sObject);
					}
					break;
				}
				eventType = xpp.next();
			}
			this.setResult(queryResult);
		} catch (XmlPullParserException xppe) {
			xppe.printStackTrace();
			throw new RuntimeException(xppe);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException(ioe);
		}

	}

	public QueryResult getResult() {
		return this.queryResult;
	}

	public void setResult(QueryResult queryResult) {
		this.queryResult = queryResult;
	}

	public Response getSoapResponse() {
		return this;
	}
}
