package com.elder.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.FamilyMember;
import com.elder.pojo.query.FamilyMemberQuery;
import com.elder.pojo.vo.FamilyMemberVO;
import com.elder.service.IFamilyMemberService;
import com.elder.util.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/admin/family-members")
public class FamilyMemberController {
    private final IFamilyMemberService familyMemberService;

    public FamilyMemberController(IFamilyMemberService familyMemberService) {
        this.familyMemberService = familyMemberService;
    }

    @GetMapping
    public Result<IPage<FamilyMemberVO>> list(FamilyMemberQuery query) {
        return Result.ok(familyMemberService.list(query));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(familyMemberService.getById(id));
    }

    @PostMapping
    public Result add(@RequestBody FamilyMember familyMember) {
        familyMemberService.save(familyMember);
        return Result.ok("新增成功");
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody FamilyMember familyMember) {
        familyMember.setId(id);
        familyMemberService.updateById(familyMember);
        return Result.ok("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        familyMemberService.removeById(id);
        return Result.ok("删除成功");
    }

    @DeleteMapping
    public Result deleteBatch(@RequestBody Long[] ids) {
        familyMemberService.removeByIds(Arrays.asList(ids));
        return Result.ok("批量删除成功");
    }
}
