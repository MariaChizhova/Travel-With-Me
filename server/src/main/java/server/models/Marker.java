package server.models;

import server.requests.MarkerCreateRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Marker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long postId;
    private String name;
    private String description;
    private double latitude;
    private double longitude;

    private int step = 0;

    public Marker() {}

    public Marker(MarkerCreateRequest markerCreateRequest, Long postId, int step) {
        this.postId = postId;
        this.name = markerCreateRequest.getName();
        this.description = markerCreateRequest.getDescription();
        this.latitude = markerCreateRequest.getLatitude();
        this.longitude = markerCreateRequest.getLongitude();
        this.step = step;
    }

    public Long getId() {
        return id;
    }
}
