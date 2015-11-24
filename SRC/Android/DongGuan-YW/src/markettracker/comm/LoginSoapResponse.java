package markettracker.comm;

import java.io.IOException;
import java.io.StringReader;

import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.LoginResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class LoginSoapResponse implements Response {
	private static final String USERID = "userId";
	private static final String ERRMSG = "errorMsg";
	private static final String RESULT = "UserLoginResult";
	private static final String SERVERTIME = "serverTime";
	private static final String ERRCODE = "errorCode";
	private static final String SUCCESS = "success";
	private static final String MSGLIST = "msgTypeList";

	private static final String MSGITEM = "msgTypeItem";
	private static final String MSGTYPE = "MsgType";
	private static final String MSGINFO = "MsgInfo";

	static final String LOGIN_RESPONSE = "UserLoginResponse";

	private LoginResult lr = null;

	public LoginSoapResponse() {
	}

	public LoginSoapResponse(String responseXML) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(responseXML));
			int eventType = xpp.getEventType();
			boolean done = false, include = false;
			String name = null;
			FieldsList list = null;
			Fields data = null;

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					name = xpp.getName();
					if (name.equalsIgnoreCase(RESULT)) {
						lr = new LoginResult();
						include = true;
					} else if (name.equalsIgnoreCase(USERID)) {
						lr.setUserId(xpp.nextText());
					} else if (name.equalsIgnoreCase(ERRMSG)) {
						lr.setErrorMsg(xpp.nextText());
					} else if (name.equalsIgnoreCase(SERVERTIME)) {
						lr.setServerTime(xpp.nextText());
					} else if (name.equalsIgnoreCase(ERRCODE)) {
						lr.setErrorCode(xpp.nextText());
					} else if (name.equalsIgnoreCase(SUCCESS)) {
						lr.setSuccess(Integer.parseInt(xpp.nextText()));
					} else if (name.equalsIgnoreCase(MSGLIST)) {
						list = new FieldsList();
					} else if (name.equalsIgnoreCase(MSGITEM)) {
						data = new Fields();
					} else if (name.equalsIgnoreCase(MSGTYPE)) {
						data.put(MSGTYPE, xpp.nextText());
					} else if (name.equalsIgnoreCase(MSGINFO)) {
						data.put(MSGINFO, xpp.nextText());
					} else if (include && lr != null) {
						lr.setField(name, xpp.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					name = xpp.getName();
					if (name.equalsIgnoreCase(LOGIN_RESPONSE)) {
						done = true;
					} else if (name.equalsIgnoreCase(MSGITEM)) {
						list.setFields(data);
					} else if (name.equalsIgnoreCase(RESULT)) {
						include = false;
					} else if (name.equalsIgnoreCase(MSGLIST)) {
						lr.setMsgTypeList(list);
					}
					break;
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException xppe) {
			xppe.printStackTrace();
			throw new RuntimeException(xppe);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException(ioe);
		}
	}

	public LoginResult getLoginResult() {
		return this.lr;
	}

	public void setLoginResult(LoginResult lr) {
		this.lr = lr;
	}

	public Response getSoapResponse() {
		return this;
	}
}
