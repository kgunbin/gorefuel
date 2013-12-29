package au.kgunbin.gorefuel.adapter;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.res.Resources;
import android.util.SparseArray;
import au.kgunbin.gorefuel.GoRefuelApplication;
import au.kgunbin.gorefuel.R;
import au.kgunbin.gorefuel.domain.Shop;
import au.kgunbin.gorefuel.util.Comparators;
import au.kgunbin.gorefuel.util.Shops;

public class PriceExpandableShopAdapter extends AbstractExpandableShopAdapter {

	public PriceExpandableShopAdapter(Activity a) {
		super(a);
	}

	@Override
	protected List<Float> getBoundaries(final List<Shop> shops,
			Resources resources) {
		return GoRefuelApplication.getPriceRange();
	}

	@Override
	protected List<String> arrangeGroups(List<Float> bounds,
			final List<Shop> unused, Resources resources) {
		final String cheaperThan = resources.getString(R.string.cheaper_than);
		List<String> groups = new ArrayList<String>(bounds.size());
		for (double d : bounds)
			groups.add(String.format(cheaperThan, d));
		return groups;
	}

	@Override
	protected String getInformation(Shop shop) {
		return String.format(
				getActivity().getResources().getString(R.string.info_price),
				shop.getAddress(), shop.getDistance());
	}

	@Override
	protected SparseArray<List<Shop>> arrangeShops(List<Float> bounds,
			List<Shop> allShops) {
		SparseArray<List<Shop>> ret = new SparseArray<List<Shop>>(bounds.size());

		for (int i = 0; i < bounds.size(); i++)
			ret.put(i, Comparators.sortedCopy(
					Shops.priceBetween(allShops, 0d, bounds.get(i)),
					Comparators.NEAREST));
		return ret;
	}
}
