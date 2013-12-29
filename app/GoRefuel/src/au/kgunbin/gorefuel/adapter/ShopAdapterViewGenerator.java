package au.kgunbin.gorefuel.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import au.kgunbin.gorefuel.R;
import au.kgunbin.gorefuel.domain.Shop;
import au.kgunbin.gorefuel.navigation.Navigator;

public final class ShopAdapterViewGenerator {
	protected final Activity activity;

	public ShopAdapterViewGenerator(final Activity a) {
		this.activity = a;
	}

	public View getShopView(final Shop shop, View convertView) {

		ViewCache vc;

		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(
					R.layout.shopitem, null);

			vc = new ViewCache();
			vc.fav = (CheckBox) convertView.findViewById(R.id.favShop);
			vc.address = (TextView) convertView.findViewById(R.id.shopAddress);
			vc.price = (TextView) convertView.findViewById(R.id.shopPrice);
			vc.descr = (TextView) convertView.findViewById(R.id.shopTradeName);
			vc.image = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(vc);

		} else {
			vc = (ViewCache) convertView.getTag();
		}

		vc.image.setImageResource(shop.getBrand().getDrawable());
		vc.price.setText(String.format(
				activity.getResources().getString(R.string.price),
				shop.getPrice()));
		vc.price.setTextColor(shop.getPriceRange() == 0 ? Color.GREEN : shop.getPriceRange()==1 ? Color.YELLOW : Color.RED);
		vc.descr.setText(shop.getTradingName());
		vc.address.setText(String.format(
				activity.getResources().getString(R.string.address),
				shop.getAddress(), shop.getDistance()));

		vc.fav.setChecked(shop.isFavorite());

		vc.fav.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shop.setFavorite(((CheckBox) v).isChecked());
			}
		});
		vc.address.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Navigator.navigateToGeo(activity, shop);
			}
		});
		return convertView;
	}

	static class ViewCache {
		CheckBox fav;
		TextView address;
		TextView price;
		TextView descr;
		ImageView image;
	}
}