package au.kgunbin.gorefuel.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import au.kgunbin.gorefuel.domain.Brand;
import au.kgunbin.gorefuel.domain.Shop;
import au.kgunbin.gorefuel.util.Constants;
import au.kgunbin.gorefuel.util.FuelWatchRSSParser;

public class AsyncRSSDownloadTask extends
		AbstractSynchronizedAsyncTask<List<Shop>> {

	@Override
	protected List<Shop> doInBackgroundInternal(String... params) {
		try {
			Thread.sleep(3000l);
		} catch (InterruptedException e) {
		}
		if ("1".equals(params[1]))
			return getData();
		else
			return Collections.emptyList();
	}

	protected List<Shop> DONTdoInBackground(String... params) {
		if (!isNetworkAvailable())
			return Collections.emptyList();
		StringBuilder urlStr = new StringBuilder(Constants.URL).append(
				"Product=").append(params[1]);
		if (!params[0].trim().isEmpty())
			urlStr.append("&Region=").append(params[0]);

		try {
			android.util.Log.d("URL", urlStr.toString());
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
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm != null && cm.getActiveNetworkInfo() != null);
	}

	public static List<Shop> getData() {

		List<Shop> list = new ArrayList<Shop>();

		list.add(Shop.Builder
				.get()
				.title("77.5: Gull Burswood")
				.description(
						"Address: 265 Gt Eastern Hwy, BURSWOOD, Phone: (08) 9355 2999, Site features: Conditional Driveway Service")
				.brand(Brand.GULL).price(77.5d).tradingName("Gull Burswood")
				.address("265 Gt Eastern Hwy").latitude(-31.960377)
				.longitude(115.901915).build());

		list.add(Shop.Builder
				.get()
				.title("79.3: Peak Kewdale")
				.description(
						"Address: 23 Kewdale Road, KEWDALE, Phone: (08) 9458 8622")
				.brand(Brand.PEAK).price(72.3d).tradingName("Peak Kewdale")
				.address("23 Kewdale Road").latitude(-31.990032)
				.longitude(115.948239).build());

		list.add(Shop.Builder
				.get()
				.title("81.9: BP Manning")
				.description(
						"Address: 73 Manning Road, MANNING, Phone: (08) 9450 4117, Site features: Conditional Driveway Service")
				.brand(Brand.BP).price(81.9d).tradingName("BP Manning")
				.address("73 Manning Road").latitude(-32.012519)
				.longitude(115.864554).build());

		return list;
	}
}