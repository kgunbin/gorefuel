package au.kgunbin.gorefuel.domain;

import android.os.Parcel;
import android.os.Parcelable;

public final class Shop implements Parcelable {
	private String title;
	private String description;
	private Brand brand;
	private double price;
	private String tradingName;
	private String location;
	private String address;
	private String phone;
	private double latitude;
	private double longitude;
	private String features;

	private Float distance;
	private boolean favorite;
	private byte priceRange;

	public static final Parcelable.Creator<Shop> CREATOR = new Parcelable.Creator<Shop>() {
		@Override
		public Shop[] newArray(int size) {
			return new Shop[size];
		}

		@Override
		public Shop createFromParcel(Parcel source) {
			return new Shop(source);
		}
	};

	public static class Builder {
		private String title;
		private String description;
		private Brand brand;
		private double price;
		private String tradingName;
		private String location;
		private String address;
		private String phone;
		private double latitude;
		private double longitude;
		private String features;

		public static Builder get() {
			return new Builder();
		}

		public final Builder title(String t) {
			title = t;
			return this;
		}

		public final Builder description(String t) {
			description = t;
			return this;
		}

		public final Builder brand(Brand t) {
			brand = t;
			return this;
		}

		public final Builder price(double t) {
			price = t;
			return this;
		}

		public final Builder tradingName(String t) {
			tradingName = t;
			return this;
		}

		public final Builder location(String t) {
			location = t;
			return this;
		}

		public final Builder address(String t) {
			address = t;
			return this;
		}

		public final Builder phone(String t) {
			phone = t;
			return this;
		}

		public final Builder latitude(double t) {
			latitude = t;
			return this;
		}

		public final Builder longitude(double t) {
			longitude = t;
			return this;
		}

		public final Builder features(String t) {
			features = t;
			return this;
		}

		public final Shop build() {
			return new Shop(this);
		}
	}

	private Shop(Builder b) {
		this.title = b.title;
		this.description = b.description;
		this.brand = b.brand;
		this.price = b.price;
		this.tradingName = b.tradingName;
		this.location = b.location;
		this.address = b.address;
		this.phone = b.phone;
		this.latitude = b.latitude;
		this.longitude = b.longitude;
		this.features = b.features;
	}

	private Shop(Parcel in) {
		title = in.readString();
		description = in.readString();
		brand = (Brand) in.readSerializable();
		price = in.readDouble();
		tradingName = in.readString();
		location = in.readString();
		address = in.readString();
		phone = in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
		features = in.readString();
		distance = in.readFloat();
		favorite = in.readByte() != 0;
		priceRange = in.readByte();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(description);
		dest.writeSerializable(brand);
		dest.writeDouble(price);
		dest.writeString(tradingName);
		dest.writeString(location);
		dest.writeString(address);
		dest.writeString(phone);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeString(features);
		dest.writeFloat(distance);
		dest.writeByte((byte) (favorite ? 1 : 0));
		dest.writeByte(priceRange);
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Brand getBrand() {
		return brand;
	}

	public double getPrice() {
		return price;
	}

	public String getTradingName() {
		return tradingName;
	}

	public String getLocation() {
		return location;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getFeatures() {
		return features;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public String toString() {
		return new StringBuilder(tradingName).append(":")
				.append(String.format("%.2f", price)).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Shop))
			return false;
		Shop s = (Shop) o;

		return this.latitude == s.latitude && this.longitude == s.longitude
				&& this.tradingName.equals(s.tradingName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 27;
		long l = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (l ^ (l >>> 32));
		l = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (l ^ (l >>> 32));
		result = prime * result + tradingName.hashCode();
		return result;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public byte getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(byte priceRange) {
		this.priceRange = priceRange;
	}
}
