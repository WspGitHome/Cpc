package com.wanfangdata.cpc.module.admin.model.db;

import com.wanfangdata.cpc.module.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class DbPropertyModify  extends BaseVo {
    private static final long serialVersionUID = 7238198006412851176L;

    private String propertyId;
    private String propertyType;


}
