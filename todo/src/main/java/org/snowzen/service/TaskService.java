package org.snowzen.service;

import org.snowzen.base.IdUtil;
import org.snowzen.exception.NotFoundDataException;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.model.po.TaskPO;
import org.snowzen.model.po.relation.CategoryTaskRelationPO;
import org.snowzen.model.po.relation.TagTaskRelationPO;
import org.snowzen.repository.dao.TaskRepository;
import org.snowzen.repository.dao.relation.CategoryTaskRelationRepository;
import org.snowzen.repository.dao.relation.TagTaskRelationRepository;
import org.snowzen.review.ReviewTimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;

/**
 * 任务服务
 *
 * @author snow-zen
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final TagTaskRelationRepository tagTaskRelationRepository;

    private final CategoryTaskRelationRepository categoryTaskRelationRepository;

    public TaskService(TaskRepository taskRepository, TagTaskRelationRepository tagTaskRelationRepository, CategoryTaskRelationRepository categoryTaskRelationRepository) {
        this.taskRepository = taskRepository;
        this.tagTaskRelationRepository = tagTaskRelationRepository;
        this.categoryTaskRelationRepository = categoryTaskRelationRepository;
    }

    /**
     * 添加任务
     *
     * @param taskDTO 任务DTO，不能为null
     */
    @Transactional(rollbackFor = Exception.class)
    public void addTask(TaskDTO taskDTO) {
        checkNotNull(taskDTO);

        taskDTO.setId(null);
        TaskPO taskPO = new TaskPO();
        taskPO.reverse(taskDTO);
        // 任务保存
        taskPO = taskRepository.save(taskPO);

        taskDTO.setId(taskPO.getId());
        // 标签关联关系保存
        List<TagTaskRelationPO> tagTaskRelationPOList = fetchTagTaskRelation(taskDTO);
        if (!CollectionUtils.isEmpty(tagTaskRelationPOList)) {
            tagTaskRelationRepository.saveAll(tagTaskRelationPOList);
        }

        // 分类关联关系保存
        List<CategoryTaskRelationPO> categoryTaskRelationPOList = fetchCategoryTaskRelation(taskDTO);
        if (!CollectionUtils.isEmpty(categoryTaskRelationPOList)) {
            categoryTaskRelationRepository.saveAll(categoryTaskRelationPOList);
        }
    }

    /**
     * 通过id查询任务
     *
     * @param taskId 任务id
     * @return 查询到的任务DTO对象
     */
    public TaskDTO findTask(int taskId) {
        checkArgument(IdUtil.checkId(taskId));

        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundDataException("任务不存在"))
                .convert();
    }

    /**
     * 通过分类id查询关联的任务
     *
     * @param categoryId 分类id
     * @return 关联任务列表
     */
    public List<TaskDTO> findAllByCategoryId(int categoryId) {
        checkArgument(IdUtil.checkId(categoryId));

        return taskRepository.findAllByCategoryId(categoryId).stream()
                .map(TaskPO::convert).collect(Collectors.toList());
    }

    /**
     * 通过key进行模糊匹配
     *
     * @param key 关键字，不能为null，可以为空字符
     * @return 模糊匹配到的任务列表
     */
    public List<TaskDTO> findAllByKey(String key) {
        checkArgument(key != null);

        List<TaskPO> result = !StringUtils.hasText(key) ?
                taskRepository.findAll() :
                taskRepository.findAllByTitleContaining(key);

        return result.stream().map(TaskPO::convert).collect(Collectors.toList());
    }

    /**
     * 查询所有需要复习的任务
     *
     * @return 需要复习的任务列表
     */
    public List<TaskDTO> findAllNeedReview() {
        LocalDateTime now = LocalDateTime.now();

        List<TaskPO> taskPOList = taskRepository.findAllByFinishTimeBefore(now);
        return taskPOList.stream().map(TaskPO::convert).collect(Collectors.toList());
    }

    /**
     * 完成id对应任务的复习
     *
     * @param taskId 任务id
     */
    @Transactional(rollbackFor = Exception.class)
    public void finishReview(int taskId) {
        checkArgument(IdUtil.checkId(taskId));

        TaskPO taskPO = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundDataException("任务不存在"));
        Boolean active = Optional.ofNullable(taskPO.getActive()).orElse(Boolean.FALSE);

        if (!active) {
            // TODO 考虑抛出异常提示
            return;
        }

        LocalDateTime nextReviewTime = ReviewTimeUtil.nextReviewTime(taskPO.getFinishTime(), taskPO.getReviewStrategy());

        if (nextReviewTime == null) {
            taskPO.setActive(false);
        } else {
            taskPO.setFinishTime(nextReviewTime);
        }
        taskRepository.save(taskPO);
    }

    /**
     * 修改任务信息
     *
     * @param taskDTO 任务DTO，不能为null
     */
    @Transactional(rollbackFor = Exception.class)
    public void modify(TaskDTO taskDTO) {
        checkNotNull(taskDTO);
        checkArgument(IdUtil.checkId(taskDTO.getId()));
        checkState(taskRepository.existsById(taskDTO.getId()), "任务不存在");

        // 保存任务
        TaskPO taskPO = new TaskPO();
        taskPO.reverse(taskDTO);
        taskRepository.save(taskPO);

        Integer taskId = taskDTO.getId();

        List<TagTaskRelationPO> tagTaskRelationPOList = fetchTagTaskRelation(taskDTO);
        List<TagTaskRelationPO> tagTaskRelationPOListFromDB = tagTaskRelationRepository.findAllByTaskId(taskId);

        // 删除多余标签关联
        List<TagTaskRelationPO> removeTagTaskRelationPOList = new ArrayList<>(tagTaskRelationPOListFromDB);
        removeTagTaskRelationPOList.removeAll(tagTaskRelationPOList);

        if (!CollectionUtils.isEmpty(removeTagTaskRelationPOList)) {
            tagTaskRelationRepository.deleteAll(removeTagTaskRelationPOList);
        }

        // 插入新增标签关联
        List<TagTaskRelationPO> insertTagTaskRelationPOList = new ArrayList<>(tagTaskRelationPOList);
        insertTagTaskRelationPOList.removeAll(tagTaskRelationPOListFromDB);

        if (!CollectionUtils.isEmpty(insertTagTaskRelationPOList)) {
            tagTaskRelationRepository.saveAll(insertTagTaskRelationPOList);
        }

        List<CategoryTaskRelationPO> categoryTaskRelationPOList = fetchCategoryTaskRelation(taskDTO);
        List<CategoryTaskRelationPO> categoryTaskRelationPOListFromDB = categoryTaskRelationRepository.findAllByTaskId(taskId);

        // 删除多余分类关联
        List<CategoryTaskRelationPO> removeCategoryTaskRelationPOList = new ArrayList<>(categoryTaskRelationPOListFromDB);
        removeCategoryTaskRelationPOList.removeAll(categoryTaskRelationPOList);

        if (!CollectionUtils.isEmpty(removeCategoryTaskRelationPOList)) {
            categoryTaskRelationRepository.deleteAll(removeCategoryTaskRelationPOList);
        }

        // 插入新增分类关联
        List<CategoryTaskRelationPO> insertCategoryTaskRelationPOList = new ArrayList<>(categoryTaskRelationPOList);
        insertCategoryTaskRelationPOList.removeAll(categoryTaskRelationPOListFromDB);

        if (!CollectionUtils.isEmpty(insertCategoryTaskRelationPOList)) {
            categoryTaskRelationRepository.saveAll(insertCategoryTaskRelationPOList);
        }
    }

    /**
     * 删除任务
     *
     * @param taskId 任务id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(int taskId) {
        checkArgument(IdUtil.checkId(taskId));

        if (!taskRepository.existsById(taskId)) {
            return;
        }
        taskRepository.deleteById(taskId);
        tagTaskRelationRepository.deleteAllByTaskId(taskId);
        categoryTaskRelationRepository.deleteAllByTaskId(taskId);
    }

    /**
     * 从{@link TaskDTO}提取标签关联关系
     */
    private List<TagTaskRelationPO> fetchTagTaskRelation(TaskDTO taskDTO) {
        checkArgument(IdUtil.checkId(taskDTO.getId()));
        Integer taskId = taskDTO.getId();
        return Optional.ofNullable(taskDTO.getTags())
                .orElse(Collections.emptyList())
                .stream()
                .map(tagDTO -> {
                    TagTaskRelationPO tagTaskRelationPO = new TagTaskRelationPO();
                    tagTaskRelationPO.setTaskId(taskId);
                    tagTaskRelationPO.setTagId(tagDTO.getId());
                    return tagTaskRelationPO;
                }).collect(Collectors.toList());
    }

    /**
     * 从{@link TaskDTO}提取分类关联关系
     */
    private List<CategoryTaskRelationPO> fetchCategoryTaskRelation(TaskDTO taskDTO) {
        checkArgument(IdUtil.checkId(taskDTO.getId()));
        Integer taskId = taskDTO.getId();
        return Optional.ofNullable(taskDTO.getCategories())
                .orElse(Collections.emptyList())
                .stream()
                .map(categoryDTO -> {
                    CategoryTaskRelationPO categoryTaskRelationPO = new CategoryTaskRelationPO();
                    categoryTaskRelationPO.setTaskId(taskId);
                    categoryTaskRelationPO.setCategoryId(categoryDTO.getId());
                    return categoryTaskRelationPO;
                }).collect(Collectors.toList());
    }
}
