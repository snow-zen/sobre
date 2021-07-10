package org.snowzen.repository.dao;

import org.snowzen.model.po.CategoryPO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link CategoryPO}对应JPA库
 *
 * @author snow-zen
 */
@Repository
public interface CategoryRepository extends CrudRepository<CategoryPO, Integer> {

    /**
     * 判断指定分类id列表对应的实际存在分类数量
     *
     * @param categoryIdList 分类id列表
     * @return 实际存在分类数量
     */
    int countAllByIdIn(List<Integer> categoryIdList);
}
