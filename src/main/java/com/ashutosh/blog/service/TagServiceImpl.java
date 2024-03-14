package com.ashutosh.blog.service;

import com.ashutosh.blog.dao.TagRepository;
import com.ashutosh.blog.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;
    @Autowired
    public TagServiceImpl(TagRepository theTagRepository){
        tagRepository= theTagRepository;
    }
    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findByName(String name) {
        return tagRepository.findTagByName(name);
    }
}
