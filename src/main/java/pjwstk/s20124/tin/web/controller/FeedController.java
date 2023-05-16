package pjwstk.s20124.tin.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.s20124.tin.model.dto.FeedDto;
import pjwstk.s20124.tin.services.FeedService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    @GetMapping
    public Collection<FeedDto> getFeed(
        @RequestParam(required = false, defaultValue = "0") int offset
    ){
        return feedService.getList(offset);
    }
}
