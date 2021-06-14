package org.snowzen.service;

import org.snowzen.repository.dao.TagRepository;
import org.springframework.stereotype.Service;

/**
 * 标签服务
 *
 * @author snow-zen
 */
@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
}
