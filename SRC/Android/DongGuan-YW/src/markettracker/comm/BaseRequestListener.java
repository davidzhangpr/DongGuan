package markettracker.comm;


import markettracker.comm.AsyncWeb.RequestListener;
import markettracker.comm.MarketTracker.ResponseListener;
import android.os.Bundle;
import android.util.Log;

public abstract class BaseRequestListener implements RequestListener {
	private static final String MARKET="Market";
	private static ResponseListener listener;
	
	public abstract void onComplete(Object response);
		
	public void onSforceError(String faultType, Response response) {
		ApiFault apiFault=MarketFaultFactory.getSoapFault(faultType, response);
        listener.onSforceError(apiFault);
    }    

	public void onException(Exception e) {
		Log.e(MARKET, e.getMessage());
		Bundle bundle=new Bundle();
		bundle.putString("exceptionMessage", e.getMessage());
	}
	
	public ResponseListener getResponseListener(){
		return BaseRequestListener.listener;
	}
	
	public void setResponseListener(ResponseListener listener){
		BaseRequestListener.listener=listener;
	}
}
