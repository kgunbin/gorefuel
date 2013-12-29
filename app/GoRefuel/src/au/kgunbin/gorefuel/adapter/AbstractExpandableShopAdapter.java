package au.kgunbin.gorefuel.adapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.res.Resources;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import au.kgunbin.gorefuel.GoRefuelApplication;
import au.kgunbin.gorefuel.R;
import au.kgunbin.gorefuel.domain.Shop;
import au.kgunbin.gorefuel.fragments.ResultFragmentListener;

public abstract class AbstractExpandableShopAdapter extends
		BaseExpandableListAdapter implements ResultFragmentListener {

	private final SparseArray<List<Shop>> shops;
	private final List<String> groups;
	private final ShopAdapterViewGenerator viewGenerator;
	private final Activity activity;
	private final Set<Shop> nowVisible = new HashSet<Shop>();

	protected <T> AbstractExpandableShopAdapter(final Activity a) {
		this.viewGenerator = new ShopAdapterViewGenerator(a);
		this.activity = a;
		List<Shop> allShops = GoRefuelApplication.getData();
		List<Float> boundaries = getBoundaries(allShops, a.getResources());
		this.groups = arrangeGroups(boundaries, allShops, a.getResources());
		this.shops = arrangeShops(boundaries, allShops);

	}

	protected abstract SparseArray<List<Shop>> arrangeShops(
			List<Float> boundaries, List<Shop> allShops);

	protected abstract List<Float> getBoundaries(List<Shop> allShops,
			Resources resources);

	protected abstract List<String> arrangeGroups(final List<Float> bounds,
			List<Shop> allShops, Resources resources);

	protected abstract String getInformation(final Shop shop);

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final Shop shop = (Shop) getChild(groupPosition, childPosition);
		return viewGenerator.getShopView(shop, convertView);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return shops.get(groupPosition).get(childPosition);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return shops.indexOfKey(groupPosition) >= 0 ? shops.get(groupPosition)
				.size() : 0;
	}

	@Override
	public Object getGroup(int arg0) {
		return groups.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		nowVisible.addAll(shops.get(groupPosition));		
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		nowVisible.removeAll(shops.get(groupPosition));
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			View convertView, final ViewGroup parent) {
		RowCache vc;

		if (convertView == null) {
			vc = new RowCache();
			convertView = activity.getLayoutInflater().inflate(
					R.layout.shopgroupitem, null);
			vc.text = (TextView) convertView
					.findViewById(R.id.distanceBoundary);
			vc.information = (TextView) convertView
					.findViewById(R.id.information);
			convertView.setTag(vc);
		} else
			vc = (RowCache) convertView.getTag();

		vc.text.setText(getGroup(groupPosition).toString() + " ("
				+ getChildrenCount(groupPosition) + ")");

		if (getChildrenCount(groupPosition) > 0) {
			Shop shop = (Shop) getChild(groupPosition, 0);
			vc.information.setText(getInformation(shop));
		}

		return convertView;
	}

	protected ShopAdapterViewGenerator getViewGenerator() {
		return viewGenerator;
	}

	protected Activity getActivity() {
		return activity;
	}

	@Override
	public Set<Shop> nowVisible() {
		return nowVisible;
	}

	static class RowCache {
		TextView text;
		TextView information;
	}
}