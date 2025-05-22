package homework;

public class UserAgentPojo {
    private String userAgent;
    private String expectedPlatform;
    private String expectedBrowser;
    private String expectedDevice;


    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getExpectedPlatform() {
        return expectedPlatform;
    }

    public void setExpectedPlatform(String expectedPlatform) {
        this.expectedPlatform = expectedPlatform;
    }

    public String getExpectedBrowser() {
        return expectedBrowser;
    }

    public void setExpectedBrowser(String expectedBrowser) {
        this.expectedBrowser = expectedBrowser;
    }

    public String getExpectedDevice() {
        return expectedDevice;
    }

    public void setExpectedDevice(String expectedDevice) {
        this.expectedDevice = expectedDevice;
    }
}
