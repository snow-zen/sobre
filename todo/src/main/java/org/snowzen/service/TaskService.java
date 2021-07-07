package org.snowzen.service;

import org.snowzen.base.IdUtil;
import org.snowzen.exception.NotFoundDataException;
import org.snowzen.model.assembler.TaskAssembler;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.model.dto.TagDTO;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.model.po.TaskPO;
import org.snowzen.model.po.relation.CategoryTaskRelationPO;
import org.snowzen.model.po.relation.TagTaskRelationPO;
import org.snowzen.repository.dao.TaskRepository;
import org.snowzen.repository.dao.relation.CategoryTaskRelationRepository;
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

    private final CategoryTaskRelationRepository categoryTaskRelationRepository;

    private final TaskAssembler taskAssembler;

    @Autowired
    private AssociatedTagLoader tagLoader;

    @Autowired
    private AssociatedCategoryLoader categoryLoader;

    public TaskService(TaskRepository taskRepository,
                       TagTaskRelationRepository tagTaskRelationRepository,
                       CategoryTaskRelationRepository categoryTaskRelationRepository,
                       TaskAssembler taskAssembler) {
        this.taskRepository = taskRepository;
        this.tagTaskRelationRepository = tagTaskRelationRepository;
        this.categoryTaskRelationRepository = categoryTaskRelationRepository;
        this.taskAssembler = taskAssembler;
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
        TaskPO taskPO = taskAssembler.toPO(taskDTO);
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
        TaskPO taskPO = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundDataException("任务不存在"));

        TaskDTO taskDTO = taskAssembler.toDTO(taskPO);
        tagLoader.injectTag(taskDTO);
        categoryLoader.injectCategory(taskDTO);
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
        categoryLoader.injectCategory(taskDTOList);

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
        categoryLoader.injectCategory(taskDTOList);
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
        categoryLoader.injectCategory(taskDTOList);
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

    /**
     * 关联分类装载器
     */
    @Component
    static class AssociatedCategoryLoader {

        private final CategoryService categoryService;

        private final CategoryTaskRelationRepository categoryTaskRelationRepository;

        public AssociatedCategoryLoader(CategoryService categoryService, CategoryTaskRelationRepository categoryTaskRelationRepository) {
            this.categoryService = categoryService;
            this.categoryTaskRelationRepository = categoryTaskRelationRepository;
        }

        public void injectCategory(TaskDTO taskDTO) {
            injectCategory(Collections.singletonList(taskDTO));
        }

        public void injectCategory(List<TaskDTO> taskDTOList) {
            // 查询关联关系
            List<CategoryTaskRelationPO> categoryTaskRelationPOList = categoryTaskRelationRepository.findAllByTaskIdIn(
                    taskDTOList.stream().map(TaskDTO::getId).collect(Collectors.toList()));

            // 聚合categoryId查询category
            List<CategoryDTO> categoryDTOList = categoryService.findAllCategoryById(
                    categoryTaskRelationPOList.stream().map(CategoryTaskRelationPO::getCategoryId).collect(Collectors.toList()));

            // 建立map
            Map<Integer, TaskDTO> taskDTOMap = taskDTOList.stream().collect(Collectors.toMap(TaskDTO::getId, taskDTO -> taskDTO));
            Map<Integer, CategoryDTO> categoryDTOMap = categoryDTOList.stream().collect(Collectors.toMap(CategoryDTO::getId, categoryDTO -> categoryDTO));

            // 遍历关联关系，注入
            categoryTaskRelationPOList.forEach(relation -> {
                TaskDTO taskDTO = taskDTOMap.get(relation.getTaskId());
                CategoryDTO categoryDTO = categoryDTOMap.get(relation.getCategoryId());
                if (taskDTO.getCategories() == null) {
                    taskDTO.setCategories(Collections.singletonList(categoryDTO));
                } else {
                    taskDTO.getCategories().add(categoryDTO);
                }
            });
        }
    }

    /**
     * 关联标签装载器
     */
    @Component
    static class AssociatedTagLoader {

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
