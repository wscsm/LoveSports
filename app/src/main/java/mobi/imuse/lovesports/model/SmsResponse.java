package mobi.imuse.lovesports.model;

/**
 * Created by suyanlu on 15/8/2.
 */
public class SmsResponse {
    /**
     * Sms : 1
     * Error : 0
     */
    private int Sms;
    private int Error;

    public void setSms(int Sms) {
        this.Sms = Sms;
    }

    public void setError(int Error) {
        this.Error = Error;
    }

    public int getSms() {
        return Sms;
    }

    public int getError() {
        return Error;
    }
}
