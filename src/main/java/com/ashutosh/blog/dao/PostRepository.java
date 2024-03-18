package com.ashutosh.blog.dao;

import com.ashutosh.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {

    List<Post> findAllByTitleContainingOrContentContainingOrTagsNameContaining(String title, String content, String tag);

}
