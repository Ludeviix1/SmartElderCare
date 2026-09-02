package com.elder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.mapper.CarePlanMapper;
import com.elder.mapper.CareTaskMapper;
import com.elder.mapper.ElderMapper;
import com.elder.mapper.UserMapper;
import com.elder.pojo.entity.CarePlan;
import com.elder.pojo.entity.CareTask;
import com.elder.pojo.entity.Elder;
import com.elder.pojo.entity.User;
import com.elder.pojo.query.CareTaskQuery;
import com.elder.pojo.vo.CareTaskVO;
import com.elder.service.ICareTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 护理任务与打卡记录表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-09-01
 */
@Service
public class CareTaskServiceImpl extends ServiceImpl<CareTaskMapper, CareTask> implements ICareTaskService {
    @Autowired
    private CareTaskMapper careTaskMapper;
    @Autowired
    private ElderMapper elderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CarePlanMapper carePlanMapper;

    @Override
    public IPage<CareTaskVO> list(CareTaskQuery careTaskQuery) {
        IPage<CareTask> page = new Page<>(careTaskQuery.getPage(), careTaskQuery.getLimit());

        LambdaQueryWrapper<CareTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(careTaskQuery.getElderId()), CareTask::getElderId, careTaskQuery.getElderId())
                .eq(!ObjectUtils.isEmpty(careTaskQuery.getCareItemId()), CareTask::getCareItemId, careTaskQuery.getCareItemId())
                .eq(!ObjectUtils.isEmpty(careTaskQuery.getUserId()), CareTask::getUserId, careTaskQuery.getUserId())
                .eq(!ObjectUtils.isEmpty(careTaskQuery.getStatus()), CareTask::getStatus, careTaskQuery.getStatus())
                .between(!ObjectUtils.isEmpty(careTaskQuery.getBeginPlanExecuteDate()) && !ObjectUtils.isEmpty(careTaskQuery.getEndPlanExecuteDate()), CareTask::getPlanExecuteDate, careTaskQuery.getBeginPlanExecuteDate(), careTaskQuery.getEndPlanExecuteDate())
                .orderByAsc(CareTask::getPlanExecuteDate)
                .orderByAsc(CareTask::getPlanExecuteTime);

        IPage<CareTask> careTaskPage = careTaskMapper.selectPage(page, lambdaQueryWrapper);
        List<CareTask> careTaskList = careTaskPage.getRecords();

        //批量回填名称：老人、护理员、护理计划（selectBatchIds自动过滤逻辑删除）
        Map<Long, String> elderIdToNameMap = selectIdToNameMap(selectIdList(careTaskList, CareTask::getElderId), elderMapper::selectBatchIds, Elder::getId, Elder::getName);
        Map<Long, String> userIdToNameMap = selectIdToNameMap(selectIdList(careTaskList, CareTask::getUserId), userMapper::selectBatchIds, User::getId, User::getName);
        Map<Long, String> carePlanIdToNameMap = selectIdToNameMap(selectIdList(careTaskList, CareTask::getCarePlanId), carePlanMapper::selectBatchIds, CarePlan::getId, CarePlan::getName);

        //CareTask -> CareTaskVO，塞入名称
        List<CareTaskVO> voList = careTaskList.stream().map(careTask -> {
            CareTaskVO careTaskVO = new CareTaskVO();
            BeanUtils.copyProperties(careTask, careTaskVO);
            careTaskVO.setElderName(elderIdToNameMap.get(careTask.getElderId()));
            careTaskVO.setUserName(userIdToNameMap.get(careTask.getUserId()));
            careTaskVO.setCarePlanName(carePlanIdToNameMap.get(careTask.getCarePlanId()));
            return careTaskVO;
        }).toList();

        Page<CareTaskVO> voPage = new Page<>(careTaskPage.getCurrent(), careTaskPage.getSize(), careTaskPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    /**
     * 从当前页护理任务中提取某个外键id的去重列表
     */
    private List<Long> selectIdList(List<CareTask> careTaskList, Function<CareTask, Long> idGetter) {
        return careTaskList.stream().map(idGetter)
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
}
