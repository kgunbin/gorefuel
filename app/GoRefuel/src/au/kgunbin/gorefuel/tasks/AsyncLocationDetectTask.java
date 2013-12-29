package au.kgunbin.gorefuel.tasks;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class AsyncLocationDetectTask extends
		AbstractSynchronizedAsyncTask<Location> {

	private Location location = null;

	@Override
	protected Location doInBackgroundInternal(String... arg0) {		
		LocationManager manager = (LocationManager) getContext()
				.getSystemService(Context.LOCATION_SERVICE);
		location = manager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location == null) {
			location = new Location("Perth Centre");
			location.setLatitude(-31.952854d);
			location.setLongitude(115.857561d);
		}
		return location;
	}
}
