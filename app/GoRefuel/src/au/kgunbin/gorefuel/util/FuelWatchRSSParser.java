package au.kgunbin.gorefuel.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import au.kgunbin.gorefuel.domain.Brand;
import au.kgunbin.gorefuel.domain.Shop;

public class FuelWatchRSSParser {

	private static final String ITEM = "item";
	private static final String CHANNEL = "channel";
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	private static final String BRAND = "brand";
	private static final String PRICE = "price";
	private static final String TRADING_NAME = "trading-name";
	private static final String SUBURB = "location";
	private static final String ADDRESS = "address";
	private static final String PHONE = "phone";
	private static final String LAT = "latitude";
	private static final String LON = "longitude";
	private static final String FEATURES = "site-features";

	public static List<Shop> parse(final InputStream in) {
		List<Shop> messages = null;

		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			// auto-detect the encoding from the stream
			parser.setInput(in, null);
			int eventType = parser.getEventType();
			Shop.Builder shop = null;
			boolean done = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					messages = new ArrayList<Shop>();
					break;
				case XmlPullParser.START_TAG:
					try {
						name = parser.getName();

						if (name.equalsIgnoreCase(ITEM)) {
							shop = Shop.Builder.get();
						} else if (shop != null) {
							if (name.equalsIgnoreCase(PRICE)) {
								shop.price(Double.parseDouble(parser.nextText()));
							} else if (name.equalsIgnoreCase(DESCRIPTION)) {
								shop.description(parser.nextText());
							} else if (name.equalsIgnoreCase(TRADING_NAME)) {
								shop.tradingName(parser.nextText());
							} else if (name.equalsIgnoreCase(TITLE)) {
								shop.title(parser.nextText());
							} else if (name.equalsIgnoreCase(BRAND)) {
								shop.brand(Brand.getBrand(parser.nextText()));
							} else if (name.equalsIgnoreCase(ADDRESS)) {
								shop.address(parser.nextText());
							} else if (name.equalsIgnoreCase(PHONE)) {
								shop.phone(parser.nextText());
							} else if (name.equalsIgnoreCase(LAT)) {
								shop.latitude(Double.parseDouble(parser
										.nextText()));
							} else if (name.equalsIgnoreCase(LON)) {
								shop.longitude(Double.parseDouble(parser
										.nextText()));
							} else if (name.equalsIgnoreCase(FEATURES)) {
								shop.features(parser.nextText());
							} else if (name.equalsIgnoreCase(SUBURB)) {
								shop.location(parser.nextText());
							}
						}
						break;

					} catch (NumberFormatException e) {
						android.util.Log.e("Number is wrong",
								String.valueOf(parser.getLineNumber()));
					}
				case XmlPullParser.END_TAG:
					name = parser.getName();

					if (name.equalsIgnoreCase(ITEM) && shop != null) {
						messages.add(shop.build());
					} else if (name.equalsIgnoreCase(CHANNEL)) {
						done = true;
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return messages;
	}
}
