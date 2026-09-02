package com.elder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.mapper.ElderTagMapper;
import com.elder.mapper.TagMapper;
import com.elder.pojo.entity.Elder;
import com.elder.mapper.ElderMapper;
import com.elder.pojo.entity.ElderTag;
import com.elder.pojo.entity.Tag;
import com.elder.pojo.query.ElderQuery;
import com.elder.pojo.vo.ElderInfoVO;
import com.elder.pojo.vo.ElderVO;
import com.elder.exception.ServiceException;
import com.elder.service.IElderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 老人表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-26
 */
@Service
public class ElderServiceImpl extends ServiceImpl<ElderMapper, Elder> implements IElderService {
    @Autowired
    private ElderMapper elderMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ElderTagMapper elderTagMapper;

    @Override
    public IPage<ElderVO> list(ElderQuery elderQuery) {
        IPage<Elder> page = new Page<>(elderQuery.getPage(), elderQuery.getLimit());

        LambdaQueryWrapper<Elder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!ObjectUtils.isEmpty(elderQuery.getName()), Elder::getName, elderQuery.getName())
                .like(!ObjectUtils.isEmpty(elderQuery.getPhone()), Elder::getPhone, elderQuery.getPhone())
                .between(!ObjectUtils.isEmpty(elderQuery.getBeginCreateTime()) && !ObjectUtils.isEmpty(elderQuery.getEndCreateTime()), Elder::getCreateTime, elderQuery.getBeginCreateTime(), elderQuery.getEndCreateTime())
                .orderByDesc(Elder::getCreateTime);

        //按标签筛选：先查出同时拥有所有选中标签的老人id，作为额外查询条件
        if (!ObjectUtils.isEmpty(elderQuery.getTagIds())) {
            List<Long> elderIds = selectElderIdsByAllTagIds(elderQuery.getTagIds());
            if (ObjectUtils.isEmpty(elderIds)) {
                //没有老人同时拥有这些标签，直接返回空页
                Page<ElderVO> emptyPage = new Page<>(elderQuery.getPage(), elderQuery.getLimit(), 0);
                emptyPage.setRecords(List.of());
                return emptyPage;
            }
            lambdaQueryWrapper.in(Elder::getId, elderIds);
        }

        IPage<Elder> elderPage = elderMapper.selectPage(page, lambdaQueryWrapper);

        //批量回填当前页老人的标签名：elderId -> 标签名列表
        Map<Long, List<String>> elderIdToTagNamesMap = selectTagNamesByElderIds(
                elderPage.getRecords().stream().map(Elder::getId).toList());

        //Elder -> ElderVO，塞入标签名
        List<ElderVO> voList = elderPage.getRecords().stream().map(elder -> {
            ElderVO elderVO = new ElderVO();
            // property、field、attribute
            BeanUtils.copyProperties(elder, elderVO);
            elderVO.setTagNames(elderIdToTagNamesMap.get(elder.getId()));
            return elderVO;
        }).toList();

        Page<ElderVO> voPage = new Page<>(elderPage.getCurrent(), elderPage.getSize(), elderPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    /**
     * 批量查询老人对应的标签名：elderId -> 标签名列表
     * 一页固定两条SQL：先查中间表关联，再批量查tag（selectBatchIds自动过滤逻辑删除）
     */
    private Map<Long, List<String>> selectTagNamesByElderIds(List<Long> elderIds) {
        if (ObjectUtils.isEmpty(elderIds)) {
            return Map.of();
        }
        //查询这些老人的所有关联记录
        LambdaQueryWrapper<ElderTag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(ElderTag::getElderId, elderIds);
        List<ElderTag> elderTagList = elderTagMapper.selectList(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(elderTagList)) {
            return Map.of();
        }
        //批量查询涉及的标签，得到 tagId -> tagName
        List<Long> tagIdList = elderTagList.stream().map(ElderTag::getTagId).distinct().toList();
        Map<Long, String> tagIdToName = tagMapper.selectBatchIds(tagIdList).stream()
                .collect(Collectors.toMap(Tag::getId, Tag::getName));
        //按老人分组；关联的标签已被删除（查不到名字）时跳过
        Map<Long, List<String>> map = new HashMap<>();
        for (ElderTag elderTag : elderTagList) {
            String tagName = tagIdToName.get(elderTag.getTagId());
            if (tagName == null) {
                continue;
            }
            List<String> tagNames = map.computeIfAbsent(elderTag.getElderId(), k -> new ArrayList<>());
            tagNames.add(tagName);
        }
        return map;
    }

    /**
     * 查询同时拥有所有选中标签的老人id
     * SQL：SELECT elder_id FROM elder_tag WHERE tag_id IN (...) GROUP BY elder_id HAVING COUNT(DISTINCT tag_id) = 标签个数
     */
    private List<Long> selectElderIdsByAllTagIds(List<Long> tagIds) {
        QueryWrapper<ElderTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("elder_id")
                .in("tag_id", tagIds)
                .groupBy("elder_id")
                .having("COUNT(DISTINCT tag_id) = {0}", tagIds.size());
        return elderTagMapper.selectObjs(queryWrapper).stream()
                .map(elderId -> (Long) elderId).toList();
    }

    @Override
    public Map<String, Object> selectAssignedTag(Long elderId) {
        //查询所有的tag
        List<Tag> tagList = tagMapper.selectList(null);
        //查询这个老人已分配的tag id
        LambdaQueryWrapper<ElderTag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ElderTag::getElderId, elderId);
        /*List<ElderTag> elderTagList = elderTagMapper.selectList(lambdaQueryWrapper);
        List<Long> assignedTagIdList = new ArrayList<>();
        for (ElderTag elderTag : elderTagList) {
            assignedTagIdList.add(elderTag.getTagId());
        }*/
        List<Long> assignedTagIdList = elderTagMapper.selectList(lambdaQueryWrapper).stream()
                .map(ElderTag::getTagId).toList();

        Map<String, Object> map = new HashMap<>();
        map.put("tagList", tagList);
        map.put("assignedTagIdList", assignedTagIdList);
        return map;
    }

    @Override
    public void assignTag(Long elderId, Long[] tagIds) {
        //在elder_tag表中删除这个老人原来的标签
        LambdaQueryWrapper<ElderTag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ElderTag::getElderId, elderId);
        elderTagMapper.delete(lambdaQueryWrapper);
        //在elder_tag表中添加这个老人新的标签
        for (Long tagId : tagIds) {
            ElderTag elderTag = new ElderTag();
            elderTag.setElderId(elderId);
            elderTag.setTagId(tagId);
            elderTagMapper.insert(elderTag);
        }
    }

    @Override
    public ElderInfoVO getElderInfo(Long elderId) {
        Elder elder = elderMapper.selectById(elderId);
        if (elder == null) {
            throw new ServiceException("老人信息不存在");
        }

        ElderInfoVO elderInfoVO = new ElderInfoVO();
        elderInfoVO.setId(elder.getId());
        elderInfoVO.setName(elder.getName());
        elderInfoVO.setAvatar(elder.getAvatar());
        elderInfoVO.setPhone(elder.getPhone());
        elderInfoVO.setIdCardNo(elder.getIdCardNo());
        elderInfoVO.setAddress(elder.getAddress());
        if (elder.getBirthday() != null) {
            elderInfoVO.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(elder.getBirthday()));
            elderInfoVO.setAge(calcAge(elder.getBirthday()));
        }
        return elderInfoVO;
    }

    /**
     * 根据出生日期计算周岁年龄
     */
    private Integer calcAge(Date birthday) {
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthday);
        Calendar now = Calendar.getInstance();
        int age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        //生日还没到，年龄减1
        if (now.get(Calendar.MONTH) < birth.get(Calendar.MONTH)
                || (now.get(Calendar.MONTH) == birth.get(Calendar.MONTH) && now.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return age;
    }
}
