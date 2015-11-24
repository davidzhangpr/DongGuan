package markettracker.comm;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import org.xmlpull.v1.XmlSerializer;
import markettracker.data.LoginConfig;
import android.util.Xml;

public class LoginSoapRequest implements Request {
	static final String ENV = "http://schemas.xmlsoap.org/soap/envelope/";
	static final String URN="http://tempuri.org/";
	static final String ENVELOPE = "Envelope";
	static final String BODY = "Body";
	static final String LOGIN = "UserLogin";

	private String soapRequest = null;

	public LoginSoapRequest(LoginConfig config) {
		this.soapRequest = createSoapRequest(config);
	}

	public String getRequest() {
		return this.soapRequest;
	}

	public void setRequest(String soapRequest) {
		this.soapRequest = soapRequest;
	}

	private String createSoapRequest(LoginConfig config) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {

			serializer.setOutput(writer);
			serializer.startTag(ENV, ENVELOPE);
			serializer.startTag(ENV, BODY);
			serializer.startTag(URN, LOGIN);

			Iterator<String> itrFields = null;
			Set<String> fields = null;

			fields = config.keySet();
			itrFields = fields.iterator();
			String fieldName = null;
			while (itrFields.hasNext()) {
				fieldName = itrFields.next();

				serializer.startTag(URN, fieldName);
				serializer.text(config.get(fieldName));
				serializer.endTag(URN, fieldName);
			}

			serializer.endTag(URN, LOGIN);
			serializer.endTag(ENV, BODY);
			serializer.endTag(ENV, ENVELOPE);
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
