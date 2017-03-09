package rieger.alarmsmsapp.model;

/**
 * Version model
 * Created by sebastian on 27.09.15.
 * @deprecated will be deleted in v3.0
 */
public class Version {

    private int version;

    public Version() {
    }

    public Version(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
