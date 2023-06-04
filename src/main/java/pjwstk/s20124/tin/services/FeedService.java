package pjwstk.s20124.tin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s20124.tin.configuration.properties.ApplicationProperties;
import pjwstk.s20124.tin.model.AbstractEntity;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.dto.FeedDto;
import pjwstk.s20124.tin.model.mapper.FeedMapper;
import pjwstk.s20124.tin.repository.InfoChangeRepository;
import pjwstk.s20124.tin.repository.PostRepository;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final InfoChangeRepository infoChangeRepository;
    private final UserService userService;
    private final FeedMapper feedMapper;

    @Transactional
    public Collection<FeedDto> getList(int offset) {

        User user = SecurityUtils.getCurrentUserLogin()
            .flatMap(userService::findByUsername)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Set<User> users = new HashSet<>(user.getFriends());
        users.add(user);

        List<Long> friendsId = users
            .stream()
            .map(User::getId)
            .toList();

       return Stream.concat(postRepository.findAllByAuthorIdInOrderByCreateDateDesc(friendsId), infoChangeRepository.findAllByUserIdInOrderByLastModifiedBy(friendsId))
            .skip(offset)
            .sorted(Comparator.comparing(AbstractEntity::getLastModifiedDate).reversed())
            .map(feedMapper::mapToFeed)
            .limit(5)
            .toList();
    }
}
