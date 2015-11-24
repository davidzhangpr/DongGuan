/**
 * AsyncSforce.java
 *
 * Main class that executes the Sforce SOAP requests asynchronously
 * 
 */

package markettracker.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.GZIPInputStream;

import markettracker.data.LoginConfig;
import markettracker.data.QueryConfig;
import markettracker.data.SObject;
import markettracker.util.Constants;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.CoreConnectionPNames;

import android.net.http.AndroidHttpClient;
import android.os.Bundle;

public class AsyncWeb
{
	private static final String ANDROID = "Android";
	private static final String SOAP_RESPONSE = "response";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String RESPONSE_TYPE = "responseType";
	
	private final Market mMarket;
	
	public AsyncWeb(Market market)
	{
		this.mMarket = market;
	}
	
	/**
	 * Gets the request parameters in HashMap. Sends the appropriate SOAP Sforce
	 * SOAP request and then returns the appropriate SOAP response. Based on
	 * success or failure, it invokes fault processing or success processing in
	 * the listener.
	 * 
	 * @return void
	 */
	
	public void request(final QueryConfig requestFields, final RequestListener listener)
	{
		request(Constants.SyncType.QUERY, requestFields, listener);
	}
	
	public void request(final RequestListener listener)
	{
		request(Constants.SyncType.GETSERVERTIME, null, listener);
	}
	
	public void request(final List<SObject> list, final RequestListener listener)
	{
		request(Constants.SyncType.UPLOAD, list, listener);
	}
	
	public void request(final LoginConfig config, final RequestListener listener)
	{
		request(Constants.SyncType.LOGIN, config, listener);
	}
	
	private void request(final String type, final Object object, final RequestListener listener)
	{
		new Thread()
		{
			public void run()
			{
				// String strErr = "";
				AndroidHttpClient httpClient = AndroidHttpClient.newInstance(ANDROID);
				httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20 * 1000);
				httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60 * 1000);
				
				Response qr = null;
				Request request = MarketRequestFactory.getSoapRequest(type, object);
				String SOAPRequestXML = request.getRequest();
				
				System.out.println(SOAPRequestXML);
				try
				{
					HttpPost httppost;
					// final String requestType =
					// sessionFields.get("requestType");
					httppost = new HttpPost(mMarket.getServerURL(type));
					HttpEntity entity = new StringEntity(SOAPRequestXML);
					
					httppost.setEntity(entity);
					// httppost.setHeader(SOAP_ACTION, SOAP_ACTION_VALUE);
					httppost.setHeader(CONTENT_TYPE, "text/xml; charset=utf-8");
					// httppost.setHeader(USER_AGENT, USER_AGENT_VALUE);
					
					httppost.setHeader("Accept-Encoding", "gzip");
					
					HttpResponse response = httpClient.execute(httppost);
					
					InputStream is = response.getEntity().getContent();
					
					Header contentEncoding = response.getFirstHeader("Content-Encoding");
					if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip"))
					{
						is = new GZIPInputStream(is);
					}
					
					String soapResponseString = null;
					// HttpEntity r_entity = response.getEntity();
					// BufferedHttpEntity b_entity = new BufferedHttpEntity(
					// r_entity);
					BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF8"));
					// BufferedReader br = new BufferedReader(
					// new InputStreamReader(b_entity.getContent(), "UTF8"));
					StringBuffer sb = new StringBuffer();
					String line;
					while ((line = br.readLine()) != null)
					{
						sb.append(line).append("\n");
					}
					soapResponseString = sb.toString();
					
					Bundle bundle = new Bundle();
					if (response.getStatusLine().getStatusCode() == 200)
					{
						bundle.putString(RESPONSE_TYPE, type);
					}
					else
					{
						bundle.putString(RESPONSE_TYPE, "fault");
						bundle.putString(SOAP_RESPONSE, "网络异常！");
					}
					bundle.putString(SOAP_RESPONSE, soapResponseString);
					qr = MarketResponseFactory.getSoapResponse(bundle);
					
				}
				catch (ClientProtocolException e)
				{
					Bundle bundle = new Bundle();
					bundle.putString(RESPONSE_TYPE, "fault");
					bundle.putString(SOAP_RESPONSE, "网络异常！" + e.getMessage());
					qr = MarketResponseFactory.getSoapResponse(bundle);
				}
				catch (IOException ioe)
				{
					// strErr = ioe.getMessage();
					Bundle bundle = new Bundle();
					bundle.putString(RESPONSE_TYPE, "fault");
					bundle.putString(SOAP_RESPONSE, "网络异常！" + ioe.getMessage());
					qr = MarketResponseFactory.getSoapResponse(bundle);
				}
				catch (Exception e)
				{
					Bundle bundle = new Bundle();
					bundle.putString(RESPONSE_TYPE, "fault");
					bundle.putString(SOAP_RESPONSE, "网络异常！" + e.getMessage());
					qr = MarketResponseFactory.getSoapResponse(bundle);
					// throw new RuntimeException(ioe);
					
				}
				finally
				{
					if (httpClient != null)
					{
						httpClient.close();
						httpClient.getConnectionManager().shutdown();
						httpClient = null;
					}
				}
				if (qr instanceof FaultSoapResponse)
				{
					listener.onSforceError(type, qr);
				}
				else
				{
					listener.onComplete(qr);
				}
			}
		}.start();
	}
	
	public static interface RequestListener
	{
		public void onComplete(Object response);
		
		public void onSforceError(String faultType, Response fsr);
		
		public void onException(Exception e);
	}
}
