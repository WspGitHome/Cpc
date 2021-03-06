package com.wanfangdata.cpc.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wanfangdata.cpc.module.admin.model.BizLink;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public interface BizLinkMapper extends BaseMapper<BizLink> {

    List<BizLink> selectLinks(@Param("page") IPage<BizLink> page, @Param("bizLink") BizLink bizLink);

    int deleteBatch(Integer[] ids);

}
