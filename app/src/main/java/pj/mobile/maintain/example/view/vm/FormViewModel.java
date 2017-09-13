package pj.mobile.maintain.example.view.vm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import pj.mobile.maintain.BR;

/**
 * 2017-09-13.
 */

public class FormViewModel extends BaseObservable {

    private String phone;
    private String sms;
    private boolean submit;

    @Bindable
    public String getPhone() {
        return phone;
    }

    @Bindable
    public boolean isSubmit() {
        return submit;
    }

    @Bindable
    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
        notifyPropertyChanged(BR.sms);
        setSubmit();
    }

    public void setSubmit() {
        this.submit = !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(sms);
        notifyPropertyChanged(BR.submit);
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
        setSubmit();
    }
}
