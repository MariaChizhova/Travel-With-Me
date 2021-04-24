package com.example.demo.repositories;

import com.example.demo.models.MarkerPhoto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkerPhotoRepository extends CrudRepository<MarkerPhoto, Long> {
}
