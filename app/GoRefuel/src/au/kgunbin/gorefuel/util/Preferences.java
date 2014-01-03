package au.kgunbin.gorefuel.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import au.kgunbin.gorefuel.R;

public class Preferences {

	private final Map<String, Map<String, String>> settings = new HashMap<String, Map<String, String>>();
	private final SharedPreferences prefs;

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
}
