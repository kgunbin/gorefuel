package au.kgunbin.gorefuel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import au.kgunbin.gorefuel.domain.Shop;
import au.kgunbin.gorefuel.fragments.CheapestFragment;
import au.kgunbin.gorefuel.fragments.FavoritesFragment;
import au.kgunbin.gorefuel.fragments.NearestFragment;
import au.kgunbin.gorefuel.fragments.ExecutableFragment;
import au.kgunbin.gorefuel.fragments.RecommendedFragment;
import au.kgunbin.gorefuel.fragments.ResultFragmentListener;
import au.kgunbin.gorefuel.util.Constants;

public class ResultListActivity extends Activity implements
		AsyncFragmentListener {

	private final static String EXEC_TAG = ExecutableFragment.class.getName();
	private static final String SAVED_INDEX = "SAVED_INDEX";

	private SharedPreferences prefs;
	private ExecutableFragment executableFragment;
	
	private final Fragment defaultFragment = new DefaultFragment();
	private ResultFragmentListener actualFragment = (ResultFragmentListener) defaultFragment;

	// Avoid early GC
	private final SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if (Constants.FUEL_TYPE.equals(key) || Constants.REGION.equals(key))
				GoRefuelApplication.reset();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager.setDefaultValues(this, R.xml.pref_fueltype, false);
		PreferenceManager.setDefaultValues(this, R.xml.pref_region, false);
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(prefListener);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		GoRefuelApplication.setFavorites(prefs.getStringSet(
				Constants.FAVORITES, Collections.<String> emptySet()));

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.addTab(actionBar
				.newTab()
				.setText(R.string.tab_recommended)
				.setTabListener(
						new TabListener<RecommendedFragment>(
								RecommendedFragment.class)));
		actionBar.addTab(actionBar
				.newTab()
				.setText(R.string.tab_favorites)
				.setTabListener(
						new TabListener<FavoritesFragment>(
								FavoritesFragment.class)));
		actionBar
				.addTab(actionBar
						.newTab()
						.setText(R.string.tab_nearest)
						.setTabListener(
								new TabListener<NearestFragment>(
										NearestFragment.class)));

		actionBar.addTab(actionBar
				.newTab()
				.setText(R.string.tab_cheapest)
				.setTabListener(
						new TabListener<CheapestFragment>(
								CheapestFragment.class)));

		if (savedInstanceState != null) {
			int selectedTab = savedInstanceState.getInt(SAVED_INDEX);
			actionBar.setSelectedNavigationItem(selectedTab);
		}
		FragmentManager fm = getFragmentManager();
		executableFragment = (ExecutableFragment) fm
				.findFragmentByTag(EXEC_TAG);

		if (executableFragment == null) {
			executableFragment = new ExecutableFragment();
			getFragmentManager().beginTransaction()
					.add(executableFragment, EXEC_TAG).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_map:
			Intent i = new Intent(this, MapActivity.class);
			i.putExtra(Constants.DATA,
					new ArrayList<Shop>(actualFragment.nowVisible()));
			startActivity(i);
			return true;
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(SAVED_INDEX, getActionBar()
				.getSelectedNavigationIndex());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!GoRefuelApplication.isListSet()) {
			getFragmentManager().beginTransaction()
					.replace(android.R.id.content, defaultFragment).commit();
			if (findViewById(R.id.progressBar1) != null)
				findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
			executableFragment.requestData();
		}
	}

	@Override
	protected void onStop() {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putStringSet(Constants.FAVORITES,
				GoRefuelApplication.getFavorites());
		editor.commit();
		super.onStop();
	}

	@Override
	public void onPostExecute() {
		if (GoRefuelApplication.isListSet()) {
			GoRefuelApplication.calculate();
			getActionBar().getSelectedTab().select();
		} else if (findViewById(R.id.foundNothing) != null)
			findViewById(R.id.foundNothing).setVisibility(View.VISIBLE);

		if (findViewById(R.id.progressBar1) != null)
			findViewById(R.id.progressBar1).setVisibility(View.GONE);
	}

	private class TabListener<T extends Fragment> implements
			ActionBar.TabListener {

		private final String tag;

		public TabListener(Class<T> cls) {
			this.tag = cls.getName();
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (!GoRefuelApplication.isListSet())
				return;

			Fragment myFragment = ResultListActivity.this.getFragmentManager()
					.findFragmentByTag(tag);

			// Check if the fragment is already initialized
			if (myFragment == null) {
				// If not, instantiate and add it to the activity
				myFragment = Fragment.instantiate(ResultListActivity.this, tag);

				ft.replace(android.R.id.content, myFragment, tag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(myFragment);
			}

			if (myFragment instanceof ResultFragmentListener)
				actualFragment = (ResultFragmentListener) myFragment;
			else
				actualFragment = (ResultFragmentListener) defaultFragment;
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {

			Fragment myFragment = ResultListActivity.this.getFragmentManager()
					.findFragmentByTag(tag);

			if (myFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(myFragment);
			}
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			onTabSelected(tab, ft);
		}
	}

	public static class DefaultFragment extends Fragment implements
			ResultFragmentListener {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.activity_dataretrieve, container,
					false);
		}

		@Override
		public Collection<Shop> nowVisible() {
			return Collections.emptyList();
		}
	}
}