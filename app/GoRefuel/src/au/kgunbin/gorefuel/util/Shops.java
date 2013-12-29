package au.kgunbin.gorefuel.util;

import java.util.Iterator;
import java.util.List;

import au.kgunbin.gorefuel.domain.Shop;

public final class Shops {

	private Shops() {
	}

	public static List<Shop> distanceBetween(final List<Shop> all,
			final float from, final float to) {
		List<Shop> sort = Comparators.sortedCopy(all, Comparators.NEAREST);

		for (Iterator<Shop> i = sort.iterator(); i.hasNext();) {
			Shop s = i.next();
			if (s.getDistance() <= from || s.getDistance() > to)
				i.remove();
		}
		return sort;
	}

	public static List<Shop> priceBetween(final List<Shop> all,
			final double from, final double to) {
		List<Shop> sort = Comparators.sortedCopy(all, Comparators.CHEAPEST);

		for (Iterator<Shop> i = sort.iterator(); i.hasNext();) {
			Shop s = i.next();
			if (s.getPrice() <= from || s.getPrice() > to)
				i.remove();
		}
		return sort;
	}

	public static List<Shop> favorites(final List<Shop> all) {
		List<Shop> sort = Comparators.sortedCopy(all, Comparators.FAVORITES);
		for (Iterator<Shop> i = sort.iterator(); i.hasNext();) {
			Shop s = i.next();
			if (!s.isFavorite())
				i.remove();
		}
		return sort;
	}

	public static double effective(final Shop shop) {
		return Math.max(shop.getDistance(), 3f) * shop.getPrice();
	}
}
