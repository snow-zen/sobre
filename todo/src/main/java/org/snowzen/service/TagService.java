package org.snowzen.service;

import org.snowzen.model.assembler.TagAssembler;
import org.snowzen.model.dto.TagDTO;
import org.snowzen.repository.dao.TagRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 标签服务
 *
 * @author snow-zen
 */
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TagAssembler tagAssembler;

    public TagService(TagRepository tagRepository, TagAssembler tagAssembler) {
        this.tagRepository = tagRepository;
        this.tagAssembler = tagAssembler;
    }

    /**
     * 通过标签id列表查询匹配标签
     *
     * @param tagIdList 标签id列表
     * @return 匹配标签列表
     */
    public List<TagDTO> findAllTagById(Collection<Integer> tagIdList) {
        return StreamSupport.stream(tagRepository.findAllById(tagIdList).spliterator(), false)
                .map(tagAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
