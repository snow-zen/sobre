package org.snowzen.repository.dao;

import org.snowzen.model.po.TaskPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link TaskPO}对应JPA库
 *
 * @author snow-zen
 */
@Repository
public interface TaskRepository extends JpaRepository<TaskPO, Integer> {
}
