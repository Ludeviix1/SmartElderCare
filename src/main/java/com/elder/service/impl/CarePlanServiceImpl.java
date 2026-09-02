package com.elder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.exception.ServiceException;
import com.elder.mapper.CareLevelMapper;
import com.elder.mapper.CarePlanMapper;
import com.elder.mapper.ElderMapper;
import com.elder.mapper.UserMapper;
import com.elder.pojo.dto.CarePlanDTO;
import com.elder.pojo.entity.CareItem;
import com.elder.pojo.entity.CareLevel;
import com.elder.pojo.entity.CarePlan;
import com.elder.pojo.entity.CarePlanItem;
import com.elder.pojo.entity.CareTask;
import com.elder.pojo.entity.Elder;
import com.elder.pojo.entity.User;
import com.elder.pojo.query.CarePlanQuery;
import com.elder.pojo.vo.CarePlanVO;
import com.elder.service.ICareItemService;
import com.elder.service.ICarePlanItemService;
import com.elder.service.ICarePlanService;
import com.elder.service.ICareTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 护理计划表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-09-01
 */
@Service
public class CarePlanServiceImpl extends ServiceImpl<CarePlanMapper, CarePlan> implements ICarePlanService {
    @Autowired
    private CarePlanMapper carePlanMapper;
    @Autowired
    private ElderMapper elderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CareLevelMapper careLevelMapper;
    @Autowired
    private ICarePlanItemService carePlanItemService;
    @Autowired
    private ICareTaskService careTaskService;
    @Autowired
    private ICareItemService careItemService;

    @Override
    public IPage<CarePlanVO> list(CarePlanQuery carePlanQuery) {
        IPage<CarePlan> page = new Page<>(carePlanQuery.getPage(), carePlanQuery.getLimit());

        LambdaQueryWrapper<CarePlan> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!ObjectUtils.isEmpty(carePlanQuery.getName()), CarePlan::getName, carePlanQuery.getName())
                .eq(!ObjectUtils.isEmpty(carePlanQuery.getElderId()), CarePlan::getElderId, carePlanQuery.getElderId())
                .eq(!ObjectUtils.isEmpty(carePlanQuery.getUserId()), CarePlan::getUserId, carePlanQuery.getUserId())
                .eq(!ObjectUtils.isEmpty(carePlanQuery.getCareLevelId()), CarePlan::getCareLevelId, carePlanQuery.getCareLevelId())
                .eq(!ObjectUtils.isEmpty(carePlanQuery.getStatus()), CarePlan::getStatus, carePlanQuery.getStatus())
                .between(!ObjectUtils.isEmpty(carePlanQuery.getBeginCreateTime()) && !ObjectUtils.isEmpty(carePlanQuery.getEndCreateTime()), CarePlan::getCreateTime, carePlanQuery.getBeginCreateTime(), carePlanQuery.getEndCreateTime())
                .orderByDesc(CarePlan::getCreateTime);

        IPage<CarePlan> carePlanPage = carePlanMapper.selectPage(page, lambdaQueryWrapper);
        List<CarePlan> carePlanList = carePlanPage.getRecords();

        //批量回填名称：老人、护理人员、护理等级（selectBatchIds自动过滤逻辑删除）
        List<Long> elderIds = selectIdList(carePlanList, CarePlan::getElderId);
        List<Long> userIds = selectIdList(carePlanList, CarePlan::getUserId);
        List<Long> careLevelIds = selectIdList(carePlanList, CarePlan::getCareLevelId);
        Map<Long, String> elderIdToNameMap = selectIdToNameMap(elderIds, elderMapper::selectBatchIds, Elder::getId, Elder::getName);
        Map<Long, String> userIdToNameMap = selectIdToNameMap(userIds, userMapper::selectBatchIds, User::getId, User::getName);
        Map<Long, String> careLevelIdToNameMap = selectIdToNameMap(careLevelIds, careLevelMapper::selectBatchIds, CareLevel::getId, CareLevel::getName);

        //CarePlan -> CarePlanVO，塞入名称
        List<CarePlanVO> voList = carePlanList.stream().map(carePlan -> {
            CarePlanVO carePlanVO = new CarePlanVO();
            BeanUtils.copyProperties(carePlan, carePlanVO);
            carePlanVO.setElderName(elderIdToNameMap.get(carePlan.getElderId()));
            carePlanVO.setUserName(userIdToNameMap.get(carePlan.getUserId()));
            carePlanVO.setCareLevelName(careLevelIdToNameMap.get(carePlan.getCareLevelId()));
            return carePlanVO;
        }).toList();

        Page<CarePlanVO> voPage = new Page<>(carePlanPage.getCurrent(), carePlanPage.getSize(), carePlanPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    /**
     * 从当前页护理计划中提取某个外键id的去重列表
     */
    private List<Long> selectIdList(List<CarePlan> carePlanList, Function<CarePlan, Long> idGetter) {
        return carePlanList.stream().map(idGetter)
                .filter(id -> !ObjectUtils.isEmpty(id)).distinct().toList();
    }

    /**
     * 批量查询并转成 id -> name 的Map
     */
    private <T> Map<Long, String> selectIdToNameMap(List<Long> idList, Function<List<Long>, List<T>> batchSelect,
                                                    Function<T, Long> idGetter, Function<T, String> nameGetter) {
        if (ObjectUtils.isEmpty(idList)) {
            return Map.of();
        }
        return batchSelect.apply(idList).stream().collect(Collectors.toMap(idGetter, nameGetter));
    }

    @Transactional
    @Override
    public void add(CarePlanDTO carePlanDTO) {
        //carePlanItemList标注了exist = false，insert时会被MyBatis-Plus忽略
        carePlanMapper.insert(carePlanDTO);
        saveItemList(carePlanDTO.getId(), carePlanDTO.getCarePlanItemList());
        generateTaskList(carePlanDTO);
    }

    @Transactional
    @Override
    public void update(Long id, CarePlanDTO carePlanDTO) {
        carePlanDTO.setId(id);
        carePlanMapper.updateById(carePlanDTO);
        //先删除这个计划原来的护理项目，再保存新的（同elder_tag的assignTag模式）
        removeItemList(id);
        saveItemList(id, carePlanDTO.getCarePlanItemList());
        //重建任务：只删待执行的，已完成/已跳过的是打卡历史记录，保留
        removeTaskList(id, true);
        generateTaskList(carePlanDTO);
    }

    @Override
    public CarePlanDTO selectByIdWithItems(Long id) {
        CarePlan carePlan = carePlanMapper.selectById(id);
        if (carePlan == null) {
            throw new ServiceException("护理计划不存在");
        }
        CarePlanDTO carePlanDTO = new CarePlanDTO();
        BeanUtils.copyProperties(carePlan, carePlanDTO);
        LambdaQueryWrapper<CarePlanItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CarePlanItem::getCarePlanId, id);
        carePlanDTO.setCarePlanItemList(carePlanItemService.list(lambdaQueryWrapper));
        return carePlanDTO;
    }

    /**
     * 删除护理计划时连带删除它的护理项目明细和护理任务
     */
    @Transactional
    @Override
    public boolean removeById(Serializable id) {
        removeItemList((Long) id);
        removeTaskList((Long) id, false);
        return super.removeById(id);
    }

    /**
     * 批量删除护理计划时连带删除它们的护理项目明细和护理任务
     */
    @Transactional
    @Override
    public boolean removeByIds(Collection<?> idList) {
        if (ObjectUtils.isEmpty(idList)) {
            return false;
        }
        LambdaQueryWrapper<CarePlanItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.in(CarePlanItem::getCarePlanId, idList);
        carePlanItemService.remove(itemWrapper);

        LambdaQueryWrapper<CareTask> taskWrapper = new LambdaQueryWrapper<>();
        taskWrapper.in(CareTask::getCarePlanId, idList);
        careTaskService.remove(taskWrapper);
        return super.removeByIds(idList);
    }

    /**
     * 保存护理计划的项目明细：回填计划id后批量插入
     */
    private void saveItemList(Long carePlanId, List<CarePlanItem> carePlanItemList) {
        if (ObjectUtils.isEmpty(carePlanItemList)) {
            return;
        }
        carePlanItemList.forEach(carePlanItem -> carePlanItem.setCarePlanId(carePlanId));
        carePlanItemService.saveBatch(carePlanItemList);
    }

    /**
     * 删除护理计划的项目明细
     */
    private void removeItemList(Long carePlanId) {
        LambdaQueryWrapper<CarePlanItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CarePlanItem::getCarePlanId, carePlanId);
        carePlanItemService.remove(lambdaQueryWrapper);
    }

    /**
     * 按护理项目明细生成对应的护理任务：每个明细一条任务，
     * 计划执行日期=计划开始日期，计划执行时间=明细的执行时间，状态=待执行
     */
    private void generateTaskList(CarePlanDTO carePlanDTO) {
        List<CarePlanItem> carePlanItemList = carePlanDTO.getCarePlanItemList();
        if (ObjectUtils.isEmpty(carePlanItemList)) {
            return;
        }
        //care_item_name冗余项目名称，防止项目改名后历史记录变动
        List<Long> careItemIds = carePlanItemList.stream().map(CarePlanItem::getCareItemId)
                .filter(careItemId -> !ObjectUtils.isEmpty(careItemId)).distinct().toList();
        Map<Long, String> careItemIdToNameMap = ObjectUtils.isEmpty(careItemIds) ? Map.of()
                : careItemService.listByIds(careItemIds).stream()
                .collect(Collectors.toMap(CareItem::getId, CareItem::getName));

        List<CareTask> careTaskList = new ArrayList<>();
        for (CarePlanItem carePlanItem : carePlanItemList) {
            String careItemName = careItemIdToNameMap.get(carePlanItem.getCareItemId());
            if (careItemName == null) {
                //护理项目已被删除，跳过
                continue;
            }
            CareTask careTask = new CareTask();
            careTask.setElderId(carePlanDTO.getElderId());
            careTask.setCarePlanId(carePlanDTO.getId());
            careTask.setCareItemId(carePlanItem.getCareItemId());
            careTask.setCareItemName(careItemName);
            careTask.setUserId(carePlanDTO.getUserId());
            careTask.setPlanExecuteDate(carePlanDTO.getStartDate());
            careTask.setPlanExecuteTime(carePlanItem.getExecuteTime());
            careTask.setStatus(0);
            careTaskList.add(careTask);
        }
        if (ObjectUtils.isEmpty(careTaskList)) {
            return;
        }
        careTaskService.saveBatch(careTaskList);
    }

    /**
     * 删除护理计划的护理任务
     *
     * @param onlyPending true只删待执行的（修改计划时重建用），false删全部（删除计划时用）
     */
    private void removeTaskList(Long carePlanId, boolean onlyPending) {
        LambdaQueryWrapper<CareTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CareTask::getCarePlanId, carePlanId);
        if (onlyPending) {
            lambdaQueryWrapper.eq(CareTask::getStatus, 0);
        }
        careTaskService.remove(lambdaQueryWrapper);
    }
}
