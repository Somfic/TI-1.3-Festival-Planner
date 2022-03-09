package Data.Tiles;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Layer {
    private String compression;
    private String data;
    private String encoding;
    private long height;
    private long id;
    private String name;
    private long opacity;
    private String type;
    private boolean visible;
    private long width;
    private long x;
    private long y;

    @JsonProperty("compression")
    public String getCompression() {
        return compression;
    }

    @JsonProperty("compression")
    public void setCompression(String value) {
        this.compression = value;
    }

    @JsonProperty("data")
    public String getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(String value) {
        this.data = value;
    }

    @JsonProperty("encoding")
    public String getEncoding() {
        return encoding;
    }

    @JsonProperty("encoding")
    public void setEncoding(String value) {
        this.encoding = value;
    }

    @JsonProperty("height")
    public long getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(long value) {
        this.height = value;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long value) {
        this.id = value;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String value) {
        this.name = value;
    }

    @JsonProperty("opacity")
    public long getOpacity() {
        return opacity;
    }

    @JsonProperty("opacity")
    public void setOpacity(long value) {
        this.opacity = value;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String value) {
        this.type = value;
    }

    @JsonProperty("visible")
    public boolean getVisible() {
        return visible;
    }

    @JsonProperty("visible")
    public void setVisible(boolean value) {
        this.visible = value;
    }

    @JsonProperty("width")
    public long getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(long value) {
        this.width = value;
    }

    @JsonProperty("x")
    public long getX() {
        return x;
    }

    @JsonProperty("x")
    public void setX(long value) {
        this.x = value;
    }

    @JsonProperty("y")
    public long getY() {
        return y;
    }

    @JsonProperty("y")
    public void setY(long value) {
        this.y = value;
    }
}
