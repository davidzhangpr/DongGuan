package markettracker.comm;

import java.util.ArrayList;

import markettracker.data.LoginConfig;
import markettracker.data.QueryConfig;
import markettracker.data.SObject;
import markettracker.util.Constants;

public final class MarketRequestFactory extends RequestFactory
{
	
	@SuppressWarnings("unchecked")
	public static Request getSoapRequest(String type, Object object)
	{
		if (type.equalsIgnoreCase(Constants.SyncType.LOGIN))
		{
			return new LoginSoapRequest((LoginConfig) object);
		}
		else if (type.equalsIgnoreCase(Constants.SyncType.QUERY))
		{
			return new QuerySoapRequest((QueryConfig) object);
		}
		else if (type.equalsIgnoreCase(Constants.SyncType.UPLOAD))
		{
			return new UpsertSObjectSoapRequest((ArrayList<SObject>) object);
		}
		else if (type.equalsIgnoreCase(Constants.SyncType.UPLOADMSG))
		{
			return new UpsertSObjectSoapRequest((ArrayList<SObject>) object);
		}
		else if (type.equalsIgnoreCase(Constants.SyncType.GETSERVERTIME))
		{
			return new QuerySoapRequest(null);
		}
		return null;
	}
}
