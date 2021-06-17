package org.snowzen.repository.dao;

import org.snowzen.model.po.TaskPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * {@link TaskPO}对应JPA库
 *
 * @author snow-zen
 */
@Repository
public interface TaskRepository extends JpaRepository<TaskPO, Integer> {

    /**
     * 通过分类id查询对应关联任务
     *
     * @param categoryId 分类id
     * @return 查询到的关联任务列表
     */
    @Query(value = "SELECT TT.id AS id, title, content, finish_time, is_active, review_strategy, gmt_create, gmt_modified, TTC.id, task_id, category_id FROM TODO_TASK TT LEFT JOIN TODO_TASK_CATEGORY TTC on TT.id = TTC.task_id WHERE TT.id = :categoryId",
            nativeQuery = true)
    Iterable<TaskPO> findAllByCategoryId(@Param("categoryId") int categoryId);
}
