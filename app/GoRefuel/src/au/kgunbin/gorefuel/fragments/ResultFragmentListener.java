package au.kgunbin.gorefuel.fragments;

import java.util.Collection;
import au.kgunbin.gorefuel.domain.Shop;

public interface ResultFragmentListener {
	public Collection<Shop> nowVisible();
	public void update();
}
