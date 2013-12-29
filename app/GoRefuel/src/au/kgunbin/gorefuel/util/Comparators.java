package au.kgunbin.gorefuel.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import au.kgunbin.gorefuel.domain.Shop;

public abstract class Comparators {

	public final static Comparator<Shop> CHEAPEST = new Comparator<Shop>() {
		@Override
		public int compare(Shop lhs, Shop rhs) {
			return ((Double) lhs.getPrice()).compareTo(rhs.getPrice());
		}
	};
	public final static Comparator<Shop> NEAREST = new Comparator<Shop>() {
		@Override
		public int compare(Shop arg0, Shop arg1) {
			return Float.compare(arg0.getDistance(), arg1.getDistance());
		}
	};

	public final static Comparator<Shop> FAVORITES = new Comparator<Shop>() {
		@Override
		public int compare(Shop lhs, Shop rhs) {
			return rhs.isFavorite() == lhs.isFavorite() ? 0
					: rhs.isFavorite() ? 1 : -1;
		}
	};

	public final static Comparator<Shop> RECOMMENDED = new Comparator<Shop>() {
		@Override
		public int compare(Shop arg0, Shop arg1) {
			return Double.compare(Shops.effective(arg0), Shops.effective(arg1));
		}
	};

	private Comparators() {
	}

	public static List<Shop> cheapestCopy(final Collection<Shop> source) {
		return sortedCopy(source, CHEAPEST);
	}

	public static List<Shop> sortedCopy(final Collection<Shop> source,
			final Comparator<Shop> comparator) {
		List<Shop> ret = new ArrayList<Shop>(source);
		Collections.sort(ret, comparator);
		return ret;
	}
}
