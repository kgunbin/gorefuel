package au.kgunbin.gorefuel.domain;

import java.util.HashMap;
import java.util.Map;
import au.kgunbin.gorefuel.R;

public enum Brand {
	AMPOL("Ampol", R.drawable.ic_ampol), BETTERCHOICE("Better Choice",
			R.drawable.ic_betterchoice), BLACKANDWHITE("Black & White",
			R.drawable.ic_other), BOC("BOC", R.drawable.ic_boc), BP("BP",
			R.drawable.ic_bp), CALTEX("Caltex", R.drawable.ic_caltex), CALTEXWOOLWORTHS(
			"Caltex Woolworths", R.drawable.ic_caltex), COLESEXPRESS(
			"Coles Express", R.drawable.ic_coles_express), EAGLE("Eagle",
			R.drawable.ic_other), GULL("Gull", R.drawable.ic_gull), OTHER("",
			R.drawable.ic_other), KLEENHEAT("Kleenheat",
			R.drawable.ic_kleenheat), KWIKFUEL("Kwikfuel", R.drawable.ic_other), LIBERTY(
			"Liberty", R.drawable.ic_liberty), PEAK("Peak", R.drawable.ic_other), SHELL(
			"Shell", R.drawable.ic_shell), UNITED("United",
			R.drawable.ic_united), WESCO("Wesco", R.drawable.ic_other);

	private static Map<String, Brand> lookup = new HashMap<String, Brand>(
			Brand.values().length);
	static {
		for (Brand b : Brand.values())
			lookup.put(b.getName(), b);
	}

	private final String name;
	private final int drawable;

	private Brand(final String n, final int d) {
		name = n;
		drawable = d;
	}

	public int getDrawable() {
		return drawable;
	}

	public String getName() {
		return name;
	}

	public static Brand getBrand(final String name) {
		if (lookup.containsKey(name))
			return lookup.get(name);
		else
			return OTHER;
	}
}
