package server.requests;

public class MarkerCreateRequest {

    private String name;
    private String description;
    private double latitude;
    private double longitude;

    private Iterable<PhotoCreateRequest> photos;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Iterable<PhotoCreateRequest> getPhotos() {
        return photos;
    }
}
