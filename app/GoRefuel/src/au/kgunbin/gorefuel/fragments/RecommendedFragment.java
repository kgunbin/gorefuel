package au.kgunbin.gorefuel.fragments;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import au.kgunbin.gorefuel.GoRefuelApplication;
import au.kgunbin.gorefuel.R;
import au.kgunbin.gorefuel.domain.Shop;
import au.kgunbin.gorefuel.util.Comparators;

public class RecommendedFragment extends ShopListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_recommended, null);
	}

	@Override
	protected List<Shop> getContent() {
		List<Shop> list = Comparators.sortedCopy(GoRefuelApplication.getData(),
				Comparators.RECOMMENDED);
		list = list.subList(0, Math.min(11, list.size()));
		return list;
	}
}