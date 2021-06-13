package org.snowzen.repository.dao;

import org.snowzen.model.po.TagPO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link TagPO}对应JPA库
 *
 * @author snow-zen
 */
@Repository
public interface TagRepository extends CrudRepository<TagPO, Integer> {
}
