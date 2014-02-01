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
import au.kgunbin.gorefuel.util.Shops;

public class FavoritesFragment extends ShopListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_favorites, null);
	}

	@Override
	protected List<Shop> getContent() {
		return Comparators.sortedCopy(
				Shops.favorites(GoRefuelApplication.getData()),
				Comparators.RECOMMENDED);
	}
}
