package com.ashutosh.blog.service;

import com.ashutosh.blog.dao.PostRepository;
import com.ashutosh.blog.dao.TagRepository;
import com.ashutosh.blog.dao.UserRepository;
import com.ashutosh.blog.entity.Comments;
import com.ashutosh.blog.entity.Post;
import com.ashutosh.blog.entity.Tag;
import com.ashutosh.blog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.metrics.StartupStep;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    @Autowired
    public PostServiceImpl(PostRepository thePostRepository, UserRepository theUserRepository, TagRepository theTagRepository){
        postRepository = thePostRepository;
        userRepository = theUserRepository;
        tagRepository = theTagRepository;
    }

    @Override
    @Transactional
    public void save(Post post, String[] tagStringArray) {
//        User user=new User("ashutosh","ashutosh3@gmail.com","51545");
        // user.setId(2);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(dateTimeFormatter);
        User user = userRepository.findById(1).orElse(null);
        post.setAuthor(user);
        post.setTags(null);

        List<Tag> tagsInDb = tagRepository.findAll();
        Set<String> tagNameInDb = new HashSet<>();
        for(Tag tag : tagsInDb){
            tagNameInDb.add(tag.getName());
        }
        for(String tag : tagStringArray){
            if(!tagNameInDb.contains(tag)){
                Tag newTag = new Tag(tag);
                post.addTags(newTag);
            }
            else{
                Tag newTag = tagRepository.findTagByName(tag);
                post.addTags(newTag);
            }
        }
        post.setIsPublished(true);
        if(post.getId()==0){
            post.setUpdatedAt(formattedDateTime);
        }
        else{
            post.setPublishedAt(formattedDateTime);
            post.setUpdatedAt(formattedDateTime);
        }
        if(postRepository.findById(post.getId()).isPresent()){
            post.setTitle(post.getTitle());
            post.setContent(post.getContent());
        }
        String content = post.getContent();
        post.setExcerpt(content.length()>300?content.substring(0,300):content);
        postRepository.save(post);
    }

    @Override
    public Post findById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public void readAll(Model theModel) {
        List<Post> posts = postRepository.findAll();
        theModel.addAttribute("posts", posts);
    }

    @Override
    public void delete(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public void create(Model theModel) {
        Post post = new Post();
        theModel.addAttribute("post",post);
    }

    @Override
    public void update(int id, Model theModel) {
        Post post = postRepository.findById(id).orElse(null);
        theModel.addAttribute("post" , post);
        assert post != null;
        List<Tag> tags = post.getTags();
        StringBuilder tagString  = new StringBuilder();
        int i=1;
        for(Tag tag :tags){
            if(i == tags.size()){
                tagString.append(tag.getName());
            }
            else{
                tagString = new StringBuilder(tag.getName() + ",");
            }
            i++;
        }
        theModel.addAttribute("tags", tagString.toString());
    }

    @Override
    public void read(int id, Model theModel) {
        Post post = postRepository.findById(id).orElse(null);
        theModel.addAttribute("post" , post);
        Comments comment = new Comments();
        theModel.addAttribute("Comment", comment);
    }
    public List<Post> getListOfTitleContentTag(String data){
        data = data.trim();
        data = data.toLowerCase();
        return postRepository.findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrTagsNameContainingIgnoreCase(data, data, data);
    }
    public List<Post> getListOfSortedPosts(List<Integer> postId, String sortBy){
        List<Post> sortedPosts = postRepository.getPostsSorted(sortBy);
        if(postId==null){
            return sortedPosts;
        }
        List<Post> posts = new ArrayList<>();
        for(int id : postId){
            posts.add(postRepository.findById(id).orElse(null));
        }
        List<Post> sortedRequiredPosts = new ArrayList<>();
        for(Post sortedPost : sortedPosts){
            for(Post post : posts){
                if(post.getId() == sortedPost.getId()){
                    sortedRequiredPosts.add(post);
                }
            }
        }
        return sortedRequiredPosts;
    }
    public List<Post> getListOfFilteredPosts( List<Integer> tags, List<Integer> authors, List<Integer> postsIds) {
        List<Post> posts = new ArrayList<>();
        List<Post> tagsPosts = postRepository.findPostsByTagsIn(tags);
        for (Post post : tagsPosts) {
            if (!posts.contains(post)) {
                posts.add(post);
            }
        }
        List<Post> authorsPosts = postRepository.findPostsByAuthorIdIn(authors);
        for (Post post : authorsPosts){
            if (!posts.contains(post)) {
                posts.add(post);
            }
        }
        List<Post> selectedPosts =new ArrayList<>();
        if(postsIds != null){
            for(Post post : posts){
                for(Integer postId : postsIds){
                    if(post.getId() == postId){
                        selectedPosts.add(post);
                    }
                }
            }
            return selectedPosts;
        }
        return posts;
    }
}
