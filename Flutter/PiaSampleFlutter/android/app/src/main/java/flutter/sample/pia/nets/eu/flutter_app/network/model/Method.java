package flutter.sample.pia.nets.eu.flutter_app.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


/**
 * MIT License
 * <p>
 * Copyright (c) 2019 Nets Denmark A/S
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy  of this software
 * and associated documentation files (the "Software"), to deal  in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is  furnished to do so,
 * subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class Method implements Parcelable{
    @SerializedName("id")
    private String id = null;
    @SerializedName("displayName")
    private String displayName = null;
    @SerializedName("fee")
    private Long fee = null;

    public Method() {
    }

    public Method(String id) {
        this.id = id;
    }

    protected Method(Parcel in) {
        id = in.readString();
        displayName = in.readString();
        if (in.readByte() == 0) {
            fee = null;
        } else {
            fee = in.readLong();
        }
    }

    public static final Parcelable.Creator<Method> CREATOR = new Parcelable.Creator<Method>() {
        @Override
        public Method createFromParcel(Parcel in) {
            return new Method(in);
        }

        @Override
        public Method[] newArray(int size) {
            return new Method[size];
        }
    };

    /**
     * Payment ID
     **/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Payment display name
     **/
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Fee for this payment method
     **/
    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Method{");
        sb.append("id='").append(id).append("'");
        sb.append(",displayName='").append(displayName).append("'");
        sb.append(",fee='").append(fee).append("'");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(displayName);
        if (fee == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(fee);
        }
    }
}
