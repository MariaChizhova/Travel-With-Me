package com.example.demo.repositories;

import com.example.demo.models.Subscribe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribeRepository extends CrudRepository<Subscribe, Long> {

    Iterable<Subscribe> findAllByFollowingId(Long followingId);
    Iterable<Subscribe> findAllByFollowerId(Long followerId);

}
