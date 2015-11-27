package markettracker.comm;

import android.content.Context;

public class Market
{
	private String serverURL = "http://223.4.23.60:8413/DataWebService.asmx";
	Context context;
	public Market(Context context)
	{
		this.context = context;
	}
	
	public String getServerURL(String type)
	{
		return serverURL;
	}
	
	public void setServerURL(String serverURL)
	{
		this.serverURL = serverURL;
	}
}
