package au.kgunbin.gorefuel.fragments;

import java.util.Collection;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import au.kgunbin.gorefuel.R;
import au.kgunbin.gorefuel.adapter.AbstractExpandableShopAdapter;
import au.kgunbin.gorefuel.domain.Shop;

public abstract class AbstractExpandableShopFragment extends Fragment implements
		ResultFragmentListener {
	private AbstractExpandableShopAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_postlist, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ExpandableListView listView = (ExpandableListView) getActivity()
				.findViewById(R.id.shopListView);
		adapter = getAdapter(getActivity());
		listView.setAdapter(adapter);
	}

	@Override
	public final Collection<Shop> nowVisible() {
		return adapter.nowVisible();
	}

	protected abstract AbstractExpandableShopAdapter getAdapter(
			Activity activity);
	
	public final void update() {
		adapter.notifyDataSetChanged();
	}
}