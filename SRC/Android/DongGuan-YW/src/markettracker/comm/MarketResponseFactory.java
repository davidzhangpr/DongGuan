package markettracker.comm;

import markettracker.util.Constants;
import android.os.Bundle;

public final class MarketResponseFactory extends ResponseFactory
{
	
	public static Response getSoapResponse(Bundle bundle)
	{
		final String responseType = bundle.getString("responseType");
		System.out.println("ResponseType=" + responseType);
		final String response = bundle.getString("response");
		System.out.println("response=" + response);
		if (responseType.equals(Constants.SyncType.LOGIN))
		{
			return new LoginSoapResponse(response);
		}
		else if (responseType.equals(Constants.SyncType.UPLOAD))
		{
			return new UpsertSoapResponse(response);
		}
		else if (responseType.equals(Constants.SyncType.GETSERVERTIME))
		{
			return new getServerTimeSoapResponse(response);
		}
		else if (responseType.equals(Constants.SyncType.UPLOADMSG))
		{
			return new UpsertSoapResponse(response);
		}
		else if (responseType.equals("fault"))
		{
			return new FaultSoapResponse(response);
		}
		else if (responseType.equals("Exception"))
		{
			return new ExceptionResponse(response);
		}
		else
		{
			return new QuerySoapResponse(response);
		}
	}
}
