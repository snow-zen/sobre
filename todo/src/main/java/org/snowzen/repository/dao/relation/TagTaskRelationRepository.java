package org.snowzen.repository.dao.relation;

import org.snowzen.model.po.relation.TagTaskRelationPO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * {@link TagTaskRelationPO}对应JPA库
 *
 * @author snow-zen
 */
@Repository
public interface TagTaskRelationRepository extends CrudRepository<TagTaskRelationPO, Integer> {

    /**
     * 通过任务id查询关联实体
     *
     * @param taskId 任务id
     * @return 关联实体列表
     */
    List<TagTaskRelationPO> findAllByTaskId(int taskId);

    /**
     * 通过任务id列表查询关联实体列表
     *
     * @param taskIdList 任务id列表
     * @return 关联实体列表
     */
    List<TagTaskRelationPO> findAllByTaskIdIn(Collection<Integer> taskIdList);

    /**
     * 通过任务id删除关联实体
     *
     * @param taskId 任务id
     */
    void deleteAllByTaskId(int taskId);

}
