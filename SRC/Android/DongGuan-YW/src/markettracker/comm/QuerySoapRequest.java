package markettracker.comm;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import org.xmlpull.v1.XmlSerializer;
import markettracker.data.QueryConfig;
import markettracker.util.Constants;
import android.util.Xml;

public class QuerySoapRequest implements Request {
	static final String ENV = "http://schemas.xmlsoap.org/soap/envelope/";
	static final String URN = "http://tempuri.org/";
	static final String ENVELOPE = "Envelope";
	static final String BODY = "Body";
	private String soapRequest = null;

	public QuerySoapRequest(QueryConfig config) {
		if (config == null)
			this.soapRequest = getServerTimeSoapRequest(config);
		else
			this.soapRequest = createSoapRequest(config);
	}

	public String getRequest() {
		return this.soapRequest;
	}

	public void setRequest(String soapRequest) {
		this.soapRequest = soapRequest;
	}

	private String getServerTimeSoapRequest(QueryConfig config) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startTag(ENV, ENVELOPE);
			serializer.startTag(ENV, BODY);

			serializer.startTag(URN, "GetServerTimes");

			//
			serializer.endTag(URN, "GetServerTimes");
			serializer.endTag(ENV, BODY);
			serializer.endTag(ENV, ENVELOPE);
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String createSoapRequest(QueryConfig config) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startTag(ENV, ENVELOPE);
			serializer.startTag(ENV, BODY);

			serializer.startTag(URN, config.getType());

			Iterator<String> itrFields = null;
			Set<String> fields = null;

			fields = config.keySet();
			itrFields = fields.iterator();
			String fieldName = null;
			while (itrFields.hasNext()) {
				fieldName = itrFields.next();
				if (!fieldName.equals(Constants.QueryConfig.TYPE)) {

					// if (config.getType().equalsIgnoreCase("GetClientList")
					// && fieldName.equalsIgnoreCase("isall")) {
					// serializer.startTag(URN, "isAll");
					// serializer.text(0 + "");
					// serializer.endTag(URN, "isAll");
					// } else {
					serializer.startTag(URN, fieldName);
					serializer.text(config.get(fieldName));
					serializer.endTag(URN, fieldName);
					// }
				}
			}

			// if (config.getType().equals("GetSurveyHeadList")
			// || config.getType().equals("GetSurveyQuestionList")
			// || config.getType().equals("GetSurveyQuestionOptionsList")
			// || config.getType().equals("GetSurveyRltClientList")) {
			// serializer.startTag(URN, "displaytype");
			// serializer.text("1");
			// serializer.endTag(URN, "displaytype");
			// }

			serializer.endTag(URN, config.getType());
			serializer.endTag(ENV, BODY);
			serializer.endTag(ENV, ENVELOPE);
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
