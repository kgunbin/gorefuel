package au.kgunbin.gorefuel.tasks;

public interface AsyncTaskCompletionListener<A> {
	public void onComplete(A result);
}
