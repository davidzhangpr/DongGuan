package markettracker.comm;

import java.util.ArrayList;
import java.util.List;

import markettracker.data.LoginConfig;
import markettracker.data.LoginResult;
import markettracker.data.QueryConfig;
import markettracker.data.QueryResult;
import markettracker.data.SObject;
import markettracker.data.UpsertResult;
import markettracker.util.Tool;

import android.content.Context;

public class MarketTracker
{
	private static Market mMarket;
	private static AsyncWeb asf;
	
	public static void init(Context context)
	{
		mMarket = new Market(context);
		asf = new AsyncWeb(mMarket);
	}
	
	public static void query(QueryConfig config, ResponseListener queryResponseListener)
	{
		BaseRequestListener listener = new QueryRequestListener();
		listener.setResponseListener(queryResponseListener);
		asf.request(config, listener);
	}
	
	public static void getServerTime(ResponseListener responseListener)
	{
		BaseRequestListener listener = new timeRequestListener();
		listener.setResponseListener(responseListener);
		asf.request(listener);
	}
	
	public static void login(LoginConfig config, ResponseListener loginResponseListener)
	{
		BaseRequestListener listener = new LoginRequestListener();
		listener.setResponseListener(loginResponseListener);
		asf.request(config, listener);
	}
	
	public static void upload(List<SObject> sobject, ResponseListener upsertRequestListener)
	{
		BaseRequestListener listener = new UpsertRequestListener();
		listener.setResponseListener(upsertRequestListener);
		asf.request(sobject, listener);
	}
	
	public static class UpsertRequestListener extends BaseRequestListener
	{
		public void onComplete(final Object qresponse)
		{
			UpsertSoapResponse queryresponse = null;
			ArrayList<UpsertResult> resut = null;
			try
			{
				queryresponse = (UpsertSoapResponse) qresponse;
				resut = (ArrayList<UpsertResult>) queryresponse.getResult();
			}
			catch (Exception ex)
			{
				// resut = new QueryResult();
				// qresult.setDone(-1);
				// qresult.setErrorMsg(ex.getMessage());
			}
			getResponseListener().onComplete(resut);
		}
	}
	
	public static class QueryRequestListener extends BaseRequestListener
	{
		public void onComplete(final Object qresponse)
		{
			QuerySoapResponse queryresponse = null;
			QueryResult qresult = null;
			try
			{
				queryresponse = (QuerySoapResponse) qresponse;
				qresult = (QueryResult) queryresponse.getResult();
			}
			catch (Exception ex)
			{
				qresult = new QueryResult();
				qresult.setDone(-1);
				qresult.setErrorMsg(ex.getMessage());
			}
			getResponseListener().onComplete(qresult);
		}
	}
	
	public static class timeRequestListener extends BaseRequestListener
	{
		public void onComplete(final Object qresponse)
		{
			getServerTimeSoapResponse queryresponse = null;
			queryresponse = (getServerTimeSoapResponse) qresponse;
			Tool.setServerTimes(queryresponse.getResult());
			getResponseListener().onComplete(queryresponse.getResult());
		}
	}
	
	public static class LoginRequestListener extends BaseRequestListener
	{
		
		public void onComplete(final Object uresponse)
		{
			LoginResult loginResponse = null;
			try
			{
				loginResponse = ((LoginSoapResponse) uresponse).getLoginResult();
			}
			catch (Exception ex)
			{
				loginResponse = new LoginResult();
				loginResponse.setSuccess(-1);
				loginResponse.setErrorMsg("服务器连接失败，请稍候重试！");
			}
			getResponseListener().onComplete(loginResponse);
			
		}
	}
	
	public static interface ResponseListener
	{
		public void onComplete(Object response);
		
		public void onException(Exception e);
		
		public void onSforceError(ApiFault apiFault);
	}
	
	public static Market getMarket()
	{
		return mMarket;
	}
	
	public static void setMarket(Market market)
	{
		MarketTracker.mMarket = market;
	}
	
}
