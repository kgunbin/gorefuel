package au.kgunbin.gorefuel.fragments;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import au.kgunbin.gorefuel.AsyncFragmentListener;
import au.kgunbin.gorefuel.GoRefuelApplication;
import au.kgunbin.gorefuel.domain.Shop;
import au.kgunbin.gorefuel.tasks.AbstractSynchronizedAsyncTask;
import au.kgunbin.gorefuel.tasks.AsyncLocationDetectTask;
import au.kgunbin.gorefuel.tasks.AsyncRSSDownloadTask;
import au.kgunbin.gorefuel.tasks.AsyncTaskCompletionListener;
import au.kgunbin.gorefuel.util.Constants;
import au.kgunbin.gorefuel.util.Preferences;

public class ExecutableFragment extends Fragment {
	private AsyncFragmentListener completionListener;
	private int latch;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		completionListener = (AsyncFragmentListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Retain this fragment across configuration changes.
		setRetainInstance(true);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		completionListener = null;
	}

	public void requestData() {

		AbstractSynchronizedAsyncTask.getTask(getActivity(),
				AsyncLocationDetectTask.class,
				new AsyncTaskCompletionListener<Location>() {
					@Override
					public void onComplete(Location result) {
						GoRefuelApplication.setLocation(result);
						requestShops();
					}

					@Override
					public void onError(Exception exception) {
						// Do nothing						
					}
				}).execute(new String[] {});
	}

	private void requestShops() {

		if (!GoRefuelApplication.isListSet()) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			final boolean autoRegion = !prefs.getBoolean(Constants.REGION_AUTO,
					false);
			String fuelType = prefs.getString(Constants.FUEL_TYPE, "1");

			List<String> regions;
			if (autoRegion) {
				regions = Preferences.calculateRegions(GoRefuelApplication.getLocation());
			} else {
				regions = Arrays.asList(prefs.getString(Constants.REGION, ""));
			}
			latch = regions.size();
			for (String region : regions) {
				android.util.Log.d(Constants.REGION, region);
				AbstractSynchronizedAsyncTask.getTask(getActivity(),
						AsyncRSSDownloadTask.class,
						new AbstractAsyncTaskCompletionListener<List<Shop>>() {
							@Override
							public void onComplete(List<Shop> result) {								
								GoRefuelApplication.storeData(result);
								super.onComplete(result);
							}

							@Override
							public void onError(Exception exception) {
								GoRefuelApplication.setNetworkError();
								super.onError(exception);
							}
						}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						region, fuelType);
			}
		}
	}

	private void onTaskComplete() {
		if (--latch <= 0)
			onAllTasksComplete();
	}

	private void onAllTasksComplete() {
		if (completionListener != null)
			completionListener.onPostExecute();
	}

	abstract class AbstractAsyncTaskCompletionListener<A> implements
			AsyncTaskCompletionListener<A> {

		@Override
		public void onComplete(A result) {
			onTaskComplete();
		}

		@Override
		public void onError(Exception exception) {
			onTaskComplete();
		}
	}
}
