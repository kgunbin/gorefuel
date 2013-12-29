package au.kgunbin.gorefuel.navigation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import au.kgunbin.gorefuel.domain.Shop;

public class Navigator {
	public static void navigateToGeo(final Context context, final Shop shop) {
		String uri = "geo:0,0?q=" + Double.toString(shop.getLatitude()) + ","
				+ Double.toString(shop.getLongitude()) + "("
				+ shop.getAddress() + ")";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		context.startActivity(intent);
	}
}
