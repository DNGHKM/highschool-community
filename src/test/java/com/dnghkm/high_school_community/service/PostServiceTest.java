package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.dto.PostDto;
import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.entity.Post;
import com.dnghkm.high_school_community.entity.Role;
import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.exception.UserNotMatchException;
import com.dnghkm.high_school_community.repository.PostRepository;
import com.dnghkm.high_school_community.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;


    @Test
    @DisplayName("게시글 작성")
    void write() {
        // given
        PostDto postDto = new PostDto("테스트제목", "테스트내용", BoardType.GLOBAL);
        String username = "테스트유저";

        User findUser = User.builder().id(1L).username(username).role(Role.ROLE_USER).build();
        given(userRepository.findByUsername(username)).willReturn(Optional.of(findUser));

        // when
        Post post = postService.write(postDto, username);

        // then
        assertEquals(post.getTitle(), postDto.getTitle());
        assertEquals(post.getContent(), postDto.getContent());
        assertEquals(post.getUser().getUsername(), username);
        assertEquals(post.getBoardType(), postDto.getBoardType());
    }

    @Test
    @DisplayName("게시글 수정 - 성공")
    void updateSuccess() {
        // given
        Long postId = 1L;
        PostDto postDto = new PostDto("수정제목", "수정내용", BoardType.GLOBAL);
        String username = "테스트유저";

        User user = User.builder()
                .username(username)
                .role(Role.ROLE_USER)
                .build();

        Post postBeforeUpdate = Post.builder()
                .id(postId)
                .title("수정 전 제목")
                .content("수정 전 내용")
                .user(User.builder().username(username).build())
                .build();


        given(postRepository.findById(postId)).willReturn(Optional.of(postBeforeUpdate));
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

        // when
        Post updatedPost = postService.update(postId, postDto, username);

        // then
        assertEquals(updatedPost.getTitle(), postDto.getTitle());
        assertEquals(updatedPost.getContent(), postDto.getContent());
    }

    @Test
    @DisplayName("게시글 수정 - 실패(게시글 찾을 수 없음)")
    void updatePostNotFound() {
        // given
        Long postId = 1L;
        PostDto postDto = new PostDto("수정제목", "수정내용", BoardType.GLOBAL);
        String username = "테스트유저";

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> postService.update(postId, postDto, username));
    }

    @Test
    @DisplayName("게시글 수정 - 실패(유저 찾을 수 없음)")
    void updateUserNotFound() {
        // given
        Long postId = 1L;
        PostDto postDto = new PostDto("수정제목", "수정내용", BoardType.GLOBAL);
        String username = "테스트유저";
        Post postBeforeUpdate = Post.builder()
                .id(postId)
                .title("수정 전 제목")
                .content("수정 전 내용")
                .user(User.builder().username(username).build())
                .build();

        given(postRepository.findById(postId)).willReturn(Optional.of(postBeforeUpdate));
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        // when & then
        assertThrows(UsernameNotFoundException.class, () -> postService.update(postId, postDto, username));
    }

    @Test
    @DisplayName("게시글 수정 - 실패(유저 불일치)")
    void updateUserNotMatch() {
        // given
        Long postId = 1L;
        PostDto postDto = new PostDto("수정제목", "수정내용", BoardType.GLOBAL);
        String writeUsername = "작성유저";
        String anotherUsername = "다른유저";


        User writeUser = User.builder()
                .username(writeUsername)
                .role(Role.ROLE_USER)
                .build();

        User anotherUser = User.builder()
                .username(anotherUsername)
                .role(Role.ROLE_USER)
                .build();

        Post postBeforeUpdate = Post.builder()
                .id(postId)
                .title("수정 전 제목")
                .content("수정 전 내용")
                .user(writeUser)
                .build();


        given(postRepository.findById(postId)).willReturn(Optional.of(postBeforeUpdate));
        given(userRepository.findByUsername(anotherUsername)).willReturn(Optional.of(anotherUser));

        // when & then
        assertThrows(UserNotMatchException.class, () -> postService.update(postId, postDto, anotherUsername));
    }

//    @Test
//    void delete() {
//    }
//
//    @Test
//    void getAllGlobalPosts() {
//    }
//
//    @Test
//    void getGlobalPost() {
//    }
//
//    @Test
//    void getAllSchoolPosts() {
//    }
//
//    @Test
//    void getSchoolPost() {
//    }
//
//    @Test
//    void upvotePost() {
//    }
//
//    @Test
//    void downvotePost() {
//    }
}