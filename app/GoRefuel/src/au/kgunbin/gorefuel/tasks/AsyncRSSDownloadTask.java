package au.kgunbin.gorefuel.tasks;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import au.kgunbin.gorefuel.domain.Shop;
import au.kgunbin.gorefuel.util.Constants;
import au.kgunbin.gorefuel.util.FuelWatchRSSParser;

public class AsyncRSSDownloadTask extends
		AbstractSynchronizedAsyncTask<List<Shop>> {

	@Override
	protected List<Shop> doInBackgroundInternal(String... params)
			throws AbstractSynchronizedAsyncTask.CompletionException {

		if (!isNetworkAvailable())
			throw new AbstractSynchronizedAsyncTask.CompletionException(
					new NetworkErrorException());
		
		StringBuilder urlStr = new StringBuilder(Constants.URL).append(
				"Product=").append(params[1]);
		if (!params[0].trim().isEmpty())
			urlStr.append("&Region=").append(params[0]);

		try {
			URL url = new URL(urlStr.toString());
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			try {
				connection.setReadTimeout(10 * 1000);
				connection.setConnectTimeout(10 * 1000);
				connection.setRequestMethod("GET");
				connection.setDoInput(true);
				connection.connect();
				InputStream is = null;
				try {
					is = connection.getInputStream();
					return FuelWatchRSSParser.parse(is);
				} finally {
					if (is != null)
						is.close();
				}
			} finally {
				connection.disconnect();
			}
		} catch (Exception e) {
			throw new AbstractSynchronizedAsyncTask.CompletionException(e);
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm != null && cm.getActiveNetworkInfo() != null);
	}
}