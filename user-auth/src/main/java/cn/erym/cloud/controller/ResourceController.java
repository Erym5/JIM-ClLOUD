package cn.erym.cloud.controller;

import cn.erym.cloud.domain.entity.ResourceEntity;
import cn.erym.cloud.service.IResourceService;
import cn.erym.cloud.support.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/resourceEntity")
public class ResourceController {

    @Autowired
    private IResourceService sysResourceService;


    @GetMapping(value = "/page")
    public Result<IPage> getResourceEntityPage(Page<ResourceEntity> page, ResourceEntity sysResource) {
        return Result.ok(sysResourceService.page(page, Wrappers.query(sysResource)));
    }


    @PostMapping(value = "/add")
    public Result create(@Valid @RequestBody ResourceEntity sysResource, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.illegalArgument();
        }
        return Result.ok(sysResourceService.save(sysResource));
    }


    @PutMapping(value = "/update")
    public Result update(@Valid @RequestBody ResourceEntity sysResource, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.illegalArgument();
        }
        return Result.ok(sysResourceService.updateById(sysResource));
    }


    @DeleteMapping(value = "/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.ok(sysResourceService.removeById(id));
    }
}
