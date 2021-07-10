package org.snowzen.service;

import org.snowzen.base.IdUtil;
import org.snowzen.exception.NotFoundDataException;
import org.snowzen.model.assembler.TaskAssembler;
import org.snowzen.model.dto.TagDTO;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.model.po.TaskPO;
import org.snowzen.model.po.relation.TagTaskRelationPO;
import org.snowzen.repository.dao.TaskRepository;
import org.snowzen.repository.dao.relation.TagTaskRelationRepository;
import org.snowzen.review.ReviewTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
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

    private final TaskAssembler taskAssembler;

    private final AssociatedTagLoader tagLoader;

    @Autowired
    private CategoryService categoryService;

    public TaskService(TaskRepository taskRepository,
                       TagTaskRelationRepository tagTaskRelationRepository,
                       TaskAssembler taskAssembler, AssociatedTagLoader tagLoader) {
        this.taskRepository = taskRepository;
        this.tagTaskRelationRepository = tagTaskRelationRepository;
        this.taskAssembler = taskAssembler;
        this.tagLoader = tagLoader;
    }


    /**
     * 添加任务
     *
     * @param taskDTO 任务DTO，不能为null
     */
    @Transactional(rollbackFor = Exception.class)
    public void addTask(TaskDTO taskDTO) {
        checkNotNull(taskDTO);
        checkRelationCategoryExist(taskDTO);

        taskDTO.setId(null);
        TaskPO taskPO = taskAssembler.toPO(taskDTO);
        // 任务保存
        taskPO = taskRepository.save(taskPO);

        taskDTO.setId(taskPO.getId());
        // 标签关联关系保存
        List<TagTaskRelationPO> tagTaskRelationPOList = fetchTagTaskRelation(taskDTO);
        if (!CollectionUtils.isEmpty(tagTaskRelationPOList)) {
            tagTaskRelationRepository.saveAll(tagTaskRelationPOList);
        }
    }

    /**
     * 通过id查询任务
     *
     * @param taskId 任务id
     * @return 查询到的任务DTO对象
     */
    public TaskDTO findTask(int taskId) {
        TaskPO taskPO = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundDataException("任务不存在"));

        TaskDTO taskDTO = taskAssembler.toDTO(taskPO);
        tagLoader.injectTag(taskDTO);
        return taskDTO;
    }

    /**
     * 通过分类id查询关联的任务
     *
     * @param categoryId 分类id
     * @return 关联任务列表
     */
    public List<TaskDTO> findAllByCategoryId(int categoryId) {
        checkArgument(IdUtil.checkId(categoryId));

        List<TaskDTO> taskDTOList = taskRepository.findAllByCategoryId(categoryId).stream()
                .map(taskAssembler::toDTO).collect(Collectors.toList());

        tagLoader.injectTag(taskDTOList);
        return taskDTOList;
    }

    /**
     * 通过key进行模糊匹配
     *
     * @param key 关键字，不能为null，可以为空字符
     * @return 模糊匹配到的任务列表
     */
    public List<TaskDTO> findAllByKey(String key) {
        List<TaskPO> result = !StringUtils.hasText(key) ?
                taskRepository.findAll() :
                taskRepository.findAllByTitleContaining(key);

        List<TaskDTO> taskDTOList = result.stream().map(taskAssembler::toDTO).collect(Collectors.toList());
        tagLoader.injectTag(taskDTOList);
        return taskDTOList;
    }

    /**
     * 查询所有需要复习的任务
     *
     * @return 需要复习的任务列表
     */
    public List<TaskDTO> findAllNeedReview() {
        LocalDateTime now = LocalDateTime.now();

        List<TaskDTO> taskDTOList = taskRepository.findAllByFinishTimeBefore(now).stream()
                .map(taskAssembler::toDTO)
                .collect(Collectors.toList());
        tagLoader.injectTag(taskDTOList);
        return taskDTOList;
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

        checkState(active, "任务已停止活动，请选择其他活动");

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
        checkRelationCategoryExist(taskDTO);

        // 保存任务
        TaskPO taskPO = taskAssembler.toPO(taskDTO);
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

    private void checkRelationCategoryExist(TaskDTO taskDTO) {
        checkState(taskDTO.getCategory() == null ||
                categoryService.hasCategory(taskDTO.getCategory().getId()));
    }

    /**
     * 关联标签装载器
     */
    @Component
    static class AssociatedTagLoader { // TODO 改为Assembler中执行注入

        private final TagService tagService;

        private final TagTaskRelationRepository tagTaskRelationRepository;

        public AssociatedTagLoader(TagService tagService, TagTaskRelationRepository tagTaskRelationRepository) {
            this.tagService = tagService;
            this.tagTaskRelationRepository = tagTaskRelationRepository;
        }

        public void injectTag(TaskDTO taskDTO) {
            injectTag(Collections.singletonList(taskDTO));
        }

        public void injectTag(List<TaskDTO> taskDTOList) {
            // 查找关联关系
            List<TagTaskRelationPO> tagTaskRelationPOList = tagTaskRelationRepository.findAllByTaskIdIn(
                    taskDTOList.stream().map(TaskDTO::getId).collect(Collectors.toList()));

            // 聚合tagId查询待处理tag
            List<TagDTO> tagDTOList = tagService.findAllTagById(
                    tagTaskRelationPOList.stream().map(TagTaskRelationPO::getTagId).collect(Collectors.toList()));

            // 建立map
            Map<Integer, TaskDTO> taskDTOMap = taskDTOList.stream().collect(Collectors.toMap(TaskDTO::getId, taskDTO -> taskDTO));
            Map<Integer, TagDTO> tagDTOMap = tagDTOList.stream().collect(Collectors.toMap(TagDTO::getId, tagDTO -> tagDTO));

            // 遍历关联关系，进行注入
            tagTaskRelationPOList.forEach(relation -> {
                TaskDTO taskDTO = taskDTOMap.get(relation.getTaskId());
                TagDTO tagDTO = tagDTOMap.get(relation.getTagId());
                if (taskDTO.getTags() == null) {
                    taskDTO.setTags(Collections.singletonList(tagDTO));
                } else {
                    taskDTO.getTags().add(tagDTO);
                }
            });
        }
    }
}
