package com.elder.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.Bed;
import com.elder.pojo.query.BedQuery;
import com.elder.pojo.vo.BedVO;
import com.elder.service.IBedService;
import com.elder.util.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/admin/beds")
public class BedController {
    private final IBedService bedService;

    public BedController(IBedService bedService) {
        this.bedService = bedService;
    }

    @GetMapping
    public Result<IPage<BedVO>> list(BedQuery query) {
        return Result.ok(bedService.list(query));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(bedService.getById(id));
    }

    @PostMapping
    public Result add(@RequestBody Bed bed) {
        bed.setElderId(null);
        if (bed.getStatus() == null || bed.getStatus() == 1) {
            bed.setStatus(0);
        }
        bedService.save(bed);
        return Result.ok("新增成功");
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Bed bed) {
        Bed current = bedService.getById(id);
        if (current == null) {
            return Result.error("床位不存在或已删除");
        }
        // 入住人与状态只能由入住、退床接口变更；空闲床位可改为停用。
        bed.setId(id);
        bed.setElderId(current.getElderId());
        if (current.getElderId() != null || bed.getStatus() == null || bed.getStatus() == 1) {
            bed.setStatus(current.getStatus());
        }
        bedService.updateById(bed);
        return Result.ok("修改成功");
    }

    @PostMapping("/{id}/assign")
    public Result assign(@PathVariable Long id, @RequestParam Long elderId) {
        bedService.assignElder(id, elderId);
        return Result.ok("办理入住成功");
    }

    @PostMapping("/{id}/release")
    public Result release(@PathVariable Long id) {
        bedService.releaseElder(id);
        return Result.ok("办理退床成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        bedService.removeBed(id);
        return Result.ok("删除成功");
    }

    @DeleteMapping
    public Result deleteBatch(@RequestBody Long[] ids) {
        bedService.removeBeds(Arrays.asList(ids));
        return Result.ok("批量删除成功");
    }
}
