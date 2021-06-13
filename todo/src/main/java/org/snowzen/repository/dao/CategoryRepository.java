package org.snowzen.repository.dao;

import org.snowzen.model.po.CategoryPO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link CategoryPO}对应JPA库
 *
 * @author snow-zen
 */
@Repository
public interface CategoryRepository extends CrudRepository<CategoryPO, Integer> {
}
