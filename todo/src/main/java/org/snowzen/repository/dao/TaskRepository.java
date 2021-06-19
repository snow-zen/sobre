package org.snowzen.repository.dao;

import org.snowzen.model.po.TaskPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
    @Query(value = "SELECT TT.id AS id, title, content, finish_time, is_active, review_strategy, gmt_create, gmt_modified, TTC.id, task_id, category_id FROM TODO_TASK TT LEFT JOIN TODO_TASK_CATEGORY TTC on TT.id = TTC.task_id WHERE TTC.category_id = :categoryId",
            nativeQuery = true)
    List<TaskPO> findAllByCategoryId(@Param("categoryId") int categoryId);

    /**
     * 通过关键字匹配对应任务
     *
     * @param key 关键字
     * @return 匹配到的任务列表
     */
    List<TaskPO> findAllByTitleContaining(String key);

    /**
     * 查询复习时间在指定时间前的任务
     *
     * @param time 指定时间
     * @return 查询到的任务列表
     */
    List<TaskPO> findAllByFinishTimeBefore(LocalDateTime time);
}
