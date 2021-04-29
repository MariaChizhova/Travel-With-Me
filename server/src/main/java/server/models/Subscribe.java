package server.models;

import server.requests.SubscribeRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long followingId;
    private Long followerId;

    public Subscribe() {}

    public Subscribe(SubscribeRequest subscribeRequest) {
        this.followingId = subscribeRequest.getFollowingId();
        this.followerId = subscribeRequest.getFollowerId();
    }

    public Long getFollowingId() {
        return followingId;
    }

    public Long getFollowerId() {
        return followerId;
    }
}
