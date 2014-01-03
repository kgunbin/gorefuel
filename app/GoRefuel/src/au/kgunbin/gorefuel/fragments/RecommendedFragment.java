package au.kgunbin.gorefuel.fragments;

import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import au.kgunbin.gorefuel.GoRefuelApplication;
import au.kgunbin.gorefuel.R;
import au.kgunbin.gorefuel.adapter.ShopAdapter;
import au.kgunbin.gorefuel.domain.Shop;
import au.kgunbin.gorefuel.util.Comparators;

public class RecommendedFragment extends ListFragment implements
		ResultFragmentListener {
	private List<Shop> recomm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_recommended, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		List<Shop> list = Comparators.sortedCopy(GoRefuelApplication.getData(),
				Comparators.RECOMMENDED);
		recomm = list.subList(0, Math.min(11, list.size()));
		setListAdapter(new ShopAdapter(getActivity(), recomm));
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public List<Shop> nowVisible() {
		return recomm;
	}
}