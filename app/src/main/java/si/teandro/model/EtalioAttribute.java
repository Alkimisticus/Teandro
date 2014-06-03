package si.teandro.model;

/**
 * Created by jernej on 3.6.14.
 */
public class EtalioAttribute {

    private String color;
    private String kind;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "EtalioAttribute{" +
                "color='" + color + '\'' +
                ", kind='" + kind + '\'' +
                '}';
    }
}
