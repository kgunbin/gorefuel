package au.kgunbin.gorefuel.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import au.kgunbin.gorefuel.R;

public class FuelTypePreferenceFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_fueltype);
	}
}
