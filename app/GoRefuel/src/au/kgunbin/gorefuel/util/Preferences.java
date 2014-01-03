package au.kgunbin.gorefuel.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.preference.PreferenceManager;
import au.kgunbin.gorefuel.R;

public class Preferences {

	private final Map<String, Map<String, String>> settings = new HashMap<String, Map<String, String>>();
	private final SharedPreferences prefs;

	private final String[] regionsLats;
	private final String[] regionsLons;
	private final String[] regionsCodes;
	private static Preferences instance;

	private Preferences(final Context context) {
		Resources res = context.getResources();
		// Cache the Lists
		{
			String[] vals = res.getStringArray(R.array.fuel_types_values);
			String[] codes = res.getStringArray(R.array.fuel_types);
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < vals.length; i++)
				map.put(vals[i], codes[i]);
			settings.put(Constants.FUEL_TYPE, map);
		}
		{
			String[] vals = res.getStringArray(R.array.regions_values);
			String[] codes = res.getStringArray(R.array.regions);
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < vals.length; i++)
				map.put(vals[i], codes[i]);
			settings.put(Constants.REGION, map);
		}
		prefs = PreferenceManager.getDefaultSharedPreferences(context);

		regionsLats = res.getStringArray(R.array.regions_lat);
		regionsLons = res.getStringArray(R.array.regions_lon);
		regionsCodes = res.getStringArray(R.array.regions_values);
	}

	private String getLabelInternal(final String settingCode) {
		String val = prefs.getString(settingCode, "");
		return settings.get(settingCode).get(val);
	}

	public static String getLabel(final String settingCode) {
		if (instance == null)
			throw new IllegalStateException(
					"Preferences.init() was never called");
		return instance.getLabelInternal(settingCode);
	}

	public static void init(final Context res) {
		if (instance != null)
			throw new IllegalStateException(
					"Preferences.init() called more than once");

		instance = new Preferences(res);
	}

	public static List<String> calculateRegions(final Location location) {
		if (instance == null)
			throw new IllegalStateException(
					"Preferences.init() was never called");
		return instance.calculateRegionsInternal(location);
	}

	private List<String> calculateRegionsInternal(final Location location) {
		class Distance implements Comparable<Distance> {
			private float distance;
			private int index;

			public Distance(float d, int i) {
				this.distance = d;
				this.index = i;
			}

			@Override
			public int compareTo(Distance arg0) {
				return ((Float) distance).compareTo(arg0.distance);
			}
		}

		List<Distance> distances = new ArrayList<Distance>(
				new AbstractList<Distance>() {
					float[] results = new float[1];

					@Override
					public Distance get(int index) {
						if ("0".equals(regionsLats[index]))
							results[0] = Float.MAX_VALUE;
						else
							Location.distanceBetween(location.getLatitude(),
									location.getLongitude(),
									Double.parseDouble(regionsLats[index]),
									Double.parseDouble(regionsLons[index]),
									results);
						return new Distance(results[0], index);
					}

					@Override
					public int size() {
						return regionsLats.length;
					}
				});
		Collections.sort(distances);

		// If the nearest is 20 kms or less, get only closer than 30 km
		// Otherwise, get nearset and all + 100 km to it
		List<String> ret = new ArrayList<String>();
		float threshold = 30000F;
		if (distances.get(0).distance > 20000)
			threshold = distances.get(0).distance + 100000F;
		for (Iterator<Distance> i = distances.iterator(); i.hasNext();) {
			Distance dist = i.next();
			if (dist.distance > threshold)
				break;
			ret.add(regionsCodes[dist.index]);
		}
		return ret;
	}
}
