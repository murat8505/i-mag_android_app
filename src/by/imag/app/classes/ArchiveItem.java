package by.imag.app.classes;


public class ArchiveItem {
    private String archName;
    private String archUrl;
    private int archId;

    public ArchiveItem(String archName, String archUrl, int archId) {
        this.archName = archName;
        this.archUrl = archUrl;
        this.archId = archId;
    }

    public ArchiveItem(String archName, String archUrl) {
        this.archName = archName;
        this.archUrl = archUrl;
        String[] splitStr = archUrl.split("=");
        this.archId = Integer.parseInt(splitStr[1]);
    }

    public String getArchName() {
        return archName;
    }

    public void setArchName(String archName) {
        this.archName = archName;
    }

    public String getArchUrl() {
        return archUrl;
    }

    public void setArchUrl(String archUrl) {
        this.archUrl = archUrl;
    }

    public int getArchId() {
        return archId;
    }

    public void setArchId(int archId) {
        this.archId = archId;
    }
}
