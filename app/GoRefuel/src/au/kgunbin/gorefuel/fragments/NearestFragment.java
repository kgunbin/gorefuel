package au.kgunbin.gorefuel.fragments;

import android.app.Activity;
import au.kgunbin.gorefuel.adapter.AbstractExpandableShopAdapter;
import au.kgunbin.gorefuel.adapter.DistanceExpandableShopAdapter;

public class NearestFragment extends AbstractExpandableShopFragment {
	@Override
	protected AbstractExpandableShopAdapter getAdapter(Activity activity) {
		return new DistanceExpandableShopAdapter(activity);
	};
}
