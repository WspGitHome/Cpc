package com.wanfangdata.cpc.module.admin.controller.db;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wanfangdata.cpc.common.util.CoreConst;
import com.wanfangdata.cpc.common.util.ResultUtil;
import com.wanfangdata.cpc.config.ApplicationInitialize;
import com.wanfangdata.cpc.module.admin.model.db.DbTemplate;
import com.wanfangdata.cpc.module.admin.model.db.DbTemplateInfo;
import com.wanfangdata.cpc.module.admin.service.db.DbLibService;
import com.wanfangdata.cpc.module.admin.service.db.DbTemplateInfoService;
import com.wanfangdata.cpc.module.admin.service.db.DbTemplateService;
import com.wanfangdata.cpc.module.admin.vo.base.PageResultVo;
import com.wanfangdata.cpc.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Packagename com.wanfangdata.cpc.module.admin.controller.db
 * @Classname TemplateController
 * @Description
 * @Authors Mr.Wu
 * @Date 2020/08/11 17:44
 * @Version 1.0
 */
@RequestMapping("template")
@Controller
@AllArgsConstructor
public class TemplateController {

    private final DbLibService dbLibService;
    private final DbTemplateService dbTemplateService;
    private final ApplicationInitialize applicationInitialize;
    private final DbTemplateInfoService dbTemplateInfoService;

    @GetMapping("/addTemplate")
    public String addTemplate(Model model) {

        model.addAttribute("libraries", applicationInitialize.getLibraries());
        model.addAttribute("templates", dbTemplateInfoService.list());
        return CoreConst.ADMIN_PREFIX + "library/addTemplate";
    }

    @GetMapping("/listTemplate")
    public String listTemplate(Model model) {
        model.addAttribute("libraries", applicationInitialize.getLibraries());
        model.addAttribute("templates", dbTemplateInfoService.list());
        return CoreConst.ADMIN_PREFIX + "library/listTemplate";
    }

    @PostMapping("/listTemplate")
    @ResponseBody
    public PageResultVo loadTemplate(DbTemplate template, Integer pageNumber, Integer pageSize) {
        IPage<DbTemplate> list = dbTemplateService.findByCondition(template, pageNumber, pageSize);
        return ResultUtil.table(list.getRecords(), list.getTotal());
    }

    @PostMapping("/addTemplate")
    @ResponseBody
    public ResponseVo templateAdd(DbTemplate template) {
        if (template.getId() == null) {
            //??????????????????????????????
            List<Integer> list = dbTemplateService.getDBIdsInTemplate();
            Set<Integer> set = new HashSet<>(list);
            if (set.contains(template.getDbId())) {
                return ResultUtil.error("??????????????????????????????????????????");
            }
            template.setCreateTime(new Date());
        }
        template.setUpdateTime(new Date());
        try {
            if (dbTemplateService.toSaveOrUpdate(template)) {
                return template.getId() != null ? ResultUtil.success("???????????????????????????") : ResultUtil.success("???????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("???????????????????????????");
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        DbTemplate template = dbTemplateService.getById(id);
        model.addAttribute("libraries", applicationInitialize.getLibraries());
        model.addAttribute("template", template);
        model.addAttribute("templates", dbTemplateInfoService.list());
        return CoreConst.ADMIN_PREFIX + "library/editTemplate";
    }

    @PostMapping("/templateInfo")
    @ResponseBody
    public DbTemplateInfo templateInfo(Integer id) {
        DbTemplateInfo templateInfo=dbTemplateInfoService.getById(id);
        return templateInfo;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo templateDelete(Integer id) {
        //?????????????????????????????????????????????????????????
        try {
            if (dbTemplateService.removedById(id)) {
                return ResultUtil.success("??????????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("??????????????????????????????");
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo templateBatchDelete(@RequestParam("ids[]") Integer[] ids) {
        //?????????????????????????????????????????????????????????
        for (Integer id : ids) {
            try {
                dbTemplateService.removedById(id);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultUtil.error("??????????????????????????????");
            }
        }
        return ResultUtil.success("??????????????????????????????");
    }
}
