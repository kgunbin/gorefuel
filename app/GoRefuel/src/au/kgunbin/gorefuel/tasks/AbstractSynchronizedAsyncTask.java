package au.kgunbin.gorefuel.tasks;

import android.content.Context;
import android.os.AsyncTask;

public abstract class AbstractSynchronizedAsyncTask<O> extends
		AsyncTask<String, Integer, O> {

	private AsyncTaskCompletionListener<O> listener;
	private Context context;

	public final static <O, T extends AbstractSynchronizedAsyncTask<O>> T getTask(
			final Context c, final Class<T> clazz,
			final AsyncTaskCompletionListener<O> a) {
		T object;
		try {
			object = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		object.listener = a;
		object.context = c;
		return object;
	}

	protected AbstractSynchronizedAsyncTask() {

	}

	protected Context getContext() {
		return context;
	}

	protected abstract O doInBackgroundInternal(String... params);

	@Override
	public final O doInBackground(String... params) {
		O res = doInBackgroundInternal(params);
		return res;
	};

	@Override
	protected final void onPostExecute(O result) {
		listener.onComplete(result);
	}
}
