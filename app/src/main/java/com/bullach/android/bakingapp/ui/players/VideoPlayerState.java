package com.bullach.android.bakingapp.ui.players;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoPlayerState implements Parcelable {

    public int window;
    public long position;
    public boolean whenReady;
    public String videoUrl;

    public VideoPlayerState() {
    }

    public VideoPlayerState(int window, long position, boolean whenReady, String videoUrl) {
        this.window = window;
        this.position = position;
        this.whenReady = whenReady;
        this.videoUrl = videoUrl;
    }

    protected VideoPlayerState(Parcel in) {
        window = in.readInt();
        position = in.readLong();
        whenReady = in.readByte() != 0;
        videoUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(window);
        dest.writeLong(position);
        dest.writeByte((byte) (whenReady ? 1 : 0));
        dest.writeString(videoUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoPlayerState> CREATOR = new Creator<VideoPlayerState>() {
        @Override
        public VideoPlayerState createFromParcel(Parcel in) {
            return new VideoPlayerState(in);
        }

        @Override
        public VideoPlayerState[] newArray(int size) {
            return new VideoPlayerState[size];
        }
    };
}
