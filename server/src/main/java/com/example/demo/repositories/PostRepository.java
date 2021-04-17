package com.example.demo.repositories;

import com.example.demo.models.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    Iterable<Post> findAllByAuthorId(Long authorId);

}
