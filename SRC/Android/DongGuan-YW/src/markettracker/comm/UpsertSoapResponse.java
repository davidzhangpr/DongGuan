package markettracker.comm;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import markettracker.data.UpsertResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class UpsertSoapResponse implements Response {
	private ArrayList<UpsertResult> resultList = new ArrayList<UpsertResult>();
	private static final String RESULT = "ReportInfo";
	private static final String RETURN = "SubReportListResult";
	private static final String MSGRESULT = "SubMsgStatusResult";

	public UpsertSoapResponse() {
	}

	public UpsertSoapResponse(String response1) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(response1));
			UpsertResult currentResult = null;
			int eventType = xpp.getEventType();
			boolean done = false;
			boolean inRecord = false;
			String name = null;
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					name = xpp.getName();
					if (name.equalsIgnoreCase(RESULT)) {
						currentResult = new UpsertResult();
						inRecord = true;
					} else if (name.equalsIgnoreCase(MSGRESULT)) {
						currentResult = new UpsertResult();
						inRecord = true;
					} else if (!(currentResult == null) && inRecord) {
						currentResult.setField(name, xpp.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					name = xpp.getName();
					if (name.equalsIgnoreCase(RESULT)) {
						resultList.add(currentResult);
						inRecord = false;
						currentResult = null;
					} else if (name.equalsIgnoreCase(RETURN)) {
						done = true;
					} else if (name.equalsIgnoreCase(MSGRESULT)) {
						resultList.add(currentResult);
						inRecord = false;
						currentResult = null;
						done = true;
					}
					break;
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException xppe) {
			throw new RuntimeException(xppe);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	public ArrayList<UpsertResult> getResult() {
		return this.resultList;
	}

	public void setResult(ArrayList<UpsertResult> resultArray) {
		this.resultList = resultArray;
	}

	public Response getSoapResponse() {
		return this;
	}
}
