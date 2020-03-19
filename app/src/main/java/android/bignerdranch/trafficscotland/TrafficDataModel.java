package android.bignerdranch.trafficscotland;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;

public class TrafficDataModel implements Parcelable {
    private String title;
    private String description;
    private String link;
    private String georss;
    private String pubDate;
    private Calendar startDate;
    private Calendar endDate;
    private String startDateAsString;
    private String endDateAsString;
    private long roadworksLength;

    protected TrafficDataModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        link = in.readString();
        georss = in.readString();
        pubDate = in.readString();
        startDateAsString = in.readString();
        endDateAsString = in.readString();
        roadworksLength = in.readLong();
    }

    public TrafficDataModel() {
    }

    public static final Creator<TrafficDataModel> CREATOR = new Creator<TrafficDataModel>() {
        @Override
        public TrafficDataModel createFromParcel(Parcel in) {
            return new TrafficDataModel(in);
        }

        @Override
        public TrafficDataModel[] newArray(int size) {
            return new TrafficDataModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.link);
        dest.writeString(this.georss);
        dest.writeString(this.pubDate);
        //dest.w(this.startDate);
        //dest.writeString(this.endDate);
        dest.writeString(this.startDateAsString);
        dest.writeString(this.endDateAsString);
        dest.writeLong(this.roadworksLength);
    }

    public long getRoadworksLength() {
        return roadworksLength;
    }

    public void setRoadworksLength(long roadworksLength) {
        this.roadworksLength = roadworksLength;
    }

    public String getStartDateAsString() {
        return startDateAsString;
    }

    public void setStartDateAsString(String startDateAsString) {
        this.startDateAsString = startDateAsString;
    }

    public String getEndDateAsString() {
        return endDateAsString;
    }

    public void setEndDateAsString(String endDateAsString) {
        this.endDateAsString = endDateAsString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGeorss() {
        return georss;
    }

    public void setGeorss(String georss) {
        this.georss = georss;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }


}
