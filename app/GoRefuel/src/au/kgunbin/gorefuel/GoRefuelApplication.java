package au.kgunbin.gorefuel;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Application;
import android.location.Location;
import au.kgunbin.gorefuel.domain.Shop;
import au.kgunbin.gorefuel.util.Shops;

public class GoRefuelApplication extends Application {

	private List<Shop> shops = new ArrayList<Shop>();
	private Location location;
	private final Set<String> favorites = new HashSet<String>();
	private List<Float> priceRange;

	private static GoRefuelApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public static boolean isListSet() {
		return !instance.shops.isEmpty();
	}

	public static List<Shop> getData() {
		return instance.shops;
	}

	public static void storeData(List<Shop> list) {
		instance.shops = list;
	}

	public static void reset() {
		instance.shops.clear();
	}

	public static void setLocation(final Location loc) {
		instance.location = loc;
	}

	public static Location getLocation() {
		return instance.location;
	}

	public static void calculate() {
		instance.calculateInternal();
	}

	private void calculateInternal() {
		float[] results = new float[1];
		calculatePriceRange();
		for (Shop s : shops) {
			Location.distanceBetween(location.getLatitude(),
					location.getLongitude(), s.getLatitude(), s.getLongitude(),
					results);
			s.setDistance(results[0] / 1000);
			s.setFavorite(favorites.contains(s.getTradingName()));

			if (s.getPrice() < priceRange.get(0))
				s.setPriceRange((byte) 0);
			else if (s.getPrice() < priceRange.get(1))
				s.setPriceRange((byte) 1);
			else
				s.setPriceRange((byte) 2);
		}
	}

	public static Set<String> getFavorites() {
		instance.favorites.clear();
		for (Shop s : Shops.favorites(getData()))
			instance.favorites.add(s.getTradingName());
		return instance.favorites;
	}

	public static void setFavorites(final Set<String> fav) {
		instance.favorites.clear();
		instance.favorites.addAll(fav);
	}

	public static List<Float> getPriceRange() {
		return instance.priceRange;
	}

	private void calculatePriceRange() {
		List<Float> prices = new AbstractList<Float>() {
			public Float get(int i) {
				return (float) (shops.get(i).getPrice());
			}

			public int size() {
				return shops.size();
			}
		};

		final double cheap = Collections.min(prices), expensive = Collections
				.max(prices) + .1f, step = (expensive - cheap) / 3.0;
		priceRange = new ArrayList<Float>(3);

		for (double price = cheap; price < expensive; price += step) {
			priceRange.add((float) (price + step));
		}
	}
}
