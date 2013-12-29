package au.kgunbin.gorefuel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import au.kgunbin.gorefuel.domain.Shop;
import au.kgunbin.gorefuel.navigation.Navigator;
import au.kgunbin.gorefuel.util.Constants;

public class MapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Map<Marker, Shop> events = new HashMap<Marker, Shop>();

		setContentView(R.layout.activity_map);

		@SuppressWarnings("unchecked")
		List<Shop> shops = (List<Shop>) getIntent().getExtras().get(
				Constants.DATA);
		Location me = GoRefuelApplication.getLocation();
		LatLng coords = new LatLng(me.getLatitude(), me.getLongitude());

		MapFragment frag = (MapFragment) getFragmentManager().findFragmentById(
				R.id.map);
		GoogleMap map = frag.getMap();

		if (map != null) {
			map.addMarker(new MarkerOptions().icon(
					BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
					.position(coords));

			for (int i = 0; i < shops.size(); i++) {
				Shop s = shops.get(i);
				float hue = s.getPriceRange() == 0 ? BitmapDescriptorFactory.HUE_GREEN
						: s.getPriceRange() == 1 ? BitmapDescriptorFactory.HUE_YELLOW
								: BitmapDescriptorFactory.HUE_RED;

				events.put(
						map.addMarker(new MarkerOptions()
								.icon(BitmapDescriptorFactory
										.defaultMarker(hue))
								.position(
										new LatLng(s.getLatitude(), s
												.getLongitude()))
								.title(String.format(
										getResources()
												.getString(R.string.price), s
												.getPrice()))), s);
			}

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 10));
			map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

			map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

				private final View view = getLayoutInflater().inflate(
						R.layout.view_mapmarker, null);

				@Override
				public View getInfoContents(Marker marker) {

					String title = marker.getTitle();

					TextView txtTitle = ((TextView) view
							.findViewById(R.id.txtInfoWindowTitle));

					if (title != null) {
						txtTitle.setText(title);
					} else {
						txtTitle.setText("");
					}
					return view;
				}

				@Override
				public View getInfoWindow(Marker marker) {
					return null;
				}
			});

			map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
				@Override
				public void onInfoWindowClick(Marker arg0) {
					Navigator.navigateToGeo(MapActivity.this, events.get(arg0));
				}
			});
		}
	}
}
