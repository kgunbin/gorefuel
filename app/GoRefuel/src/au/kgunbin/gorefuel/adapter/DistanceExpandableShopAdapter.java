package au.kgunbin.gorefuel.adapter;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.res.Resources;
import android.util.SparseArray;
import au.kgunbin.gorefuel.R;
import au.kgunbin.gorefuel.domain.Shop;
import au.kgunbin.gorefuel.util.Comparators;
import au.kgunbin.gorefuel.util.Shops;

public class DistanceExpandableShopAdapter extends
		AbstractExpandableShopAdapter {

	public DistanceExpandableShopAdapter(Activity a) {
		super(a);
	}

	@Override
	protected List<Float> getBoundaries(final List<Shop> unused,
			final Resources context) {
		final int[] is = context
				.getIntArray(au.kgunbin.gorefuel.R.array.distance_options);

		return new AbstractList<Float>() {
			public Float get(int i) {
				return Float.valueOf(is[i]);
			}

			public int size() {
				return is.length;
			}
		};
	}

	@Override
	protected List<String> arrangeGroups(final List<Float> bounds,
			final List<Shop> unused, final Resources context) {

		final String lessThan = context
				.getString(au.kgunbin.gorefuel.R.string.less_than);

		List<String> groups = new ArrayList<String>(bounds.size() + 1);

		for (int i = 0; i < bounds.size(); i++) {
			groups.add(String.format(lessThan, bounds.get(i)));
		}
		groups.add(context.getString(au.kgunbin.gorefuel.R.string.show_all));
		return groups;
	}

	@Override
	protected String getInformation(Shop shop) {
		return String.format(
				getActivity().getResources().getString(R.string.info_group),
				shop.getPrice(), shop.getAddress());
	}

	@Override
	protected SparseArray<List<Shop>> arrangeShops(List<Float> bounds,
			List<Shop> allShops) {
		SparseArray<List<Shop>> ret = new SparseArray<List<Shop>>(
				bounds.size() + 1);
		
		for (int i = 0; i < bounds.size(); i++)
			ret.put(i, Comparators.sortedCopy(
					Shops.distanceBetween(allShops, 0f, bounds.get(i)),
					Comparators.CHEAPEST));

		ret.put(bounds.size(),
				Comparators.sortedCopy(allShops, Comparators.CHEAPEST));
		return ret;
	}
}
