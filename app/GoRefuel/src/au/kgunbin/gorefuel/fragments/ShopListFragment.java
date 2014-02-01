package au.kgunbin.gorefuel.fragments;

import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import au.kgunbin.gorefuel.adapter.ShopAdapter;
import au.kgunbin.gorefuel.domain.Shop;

public abstract class ShopListFragment extends ListFragment implements
		ResultFragmentListener {

	private List<Shop> content;
	private ShopAdapter adapter;

	public ShopListFragment() {
		super();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		content = getContent();
		adapter = new ShopAdapter(getActivity(), content);
		setListAdapter(adapter);
		super.onActivityCreated(savedInstanceState);
	}

	protected abstract List<Shop> getContent();

	@Override
	public final List<Shop> nowVisible() {
		return content;
	}
}
