package markettracker.comm;


import markettracker.comm.MarketTracker.ResponseListener;
import android.util.Log;

public abstract class BaseResponseListener implements ResponseListener {
	public static final String MARKET="Market";
	
	public abstract void onComplete(Object response);
	
//	public abstract void onComplete(QueryResult qresult);
	
	public abstract void onSforceError(ApiFault ApiFault);		
	
	public void onException(Exception e) {
		Log.e(MARKET, e.getMessage());
	}
}
