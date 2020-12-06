package com.backend.server.mapper;

import com.backend.server.entity.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorMapper extends MongoRepository<Author, Object> {

}
