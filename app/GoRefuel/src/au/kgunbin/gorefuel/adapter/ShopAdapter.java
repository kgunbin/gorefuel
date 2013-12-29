package au.kgunbin.gorefuel.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import au.kgunbin.gorefuel.domain.Shop;

public class ShopAdapter extends BaseAdapter {

	private final List<Shop> data;
	private final ShopAdapterViewGenerator viewGenerator;

	public ShopAdapter(final Activity a, final List<Shop> shops) {
		viewGenerator = new ShopAdapterViewGenerator(a);
		this.data = shops;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return viewGenerator.getShopView(data.get(position), convertView);
	}
}