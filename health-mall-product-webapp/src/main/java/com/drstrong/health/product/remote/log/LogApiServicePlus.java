package com.drstrong.health.product.remote.log;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.drstrong.health.common.model.PageResult;
import com.drstrong.health.common.model.Result;
import com.drstrong.health.log.api.LogFacade;
import com.drstrong.health.log.vo.HealthLogPageQueryVO;
import com.drstrong.health.log.vo.HealthLogVO;
import com.drstrong.health.product.model.response.PageVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * log api 的远程接口封装
 *
 * @author zzw
 * @date 2023/6/9
 */
@Slf4j
@Service
public class LogApiServicePlus {

    @Autowired
    private LogFacade logFacade;

    /**
     * 西药操作日志
     *
     * @author zzw
     * @date 2023/6/9
     */
    public PageVO<HealthLogVO> queryWesternMedicineUpdateLog(HealthLogPageQueryVO queryVO) {
        try {
            Result<PageResult<HealthLogVO>> pageResultResult = logFacade.queryLogListByObjectIdAndPage(queryVO);
            if (ObjectUtil.isNull(pageResultResult) || !pageResultResult.isSuccess()) {
                log.error("调用日志服务获取西药操作日志，返回值为空或者不是成功状态，请求参数为：{}，返回值为：{}", queryVO, JSONUtil.toJsonStr(pageResultResult));
                return PageVO.newBuilder().pageNo(queryVO.getPage()).pageSize(queryVO.getPageSize()).result(Lists.newArrayList()).build();
            }
            return PageVO.newBuilder().pageNo(queryVO.getPage()).pageSize(queryVO.getPageSize()).totalCount(pageResultResult.getData().getTotalCount()).result(pageResultResult.getData().getList()).build();
        } catch (Exception e) {
            log.error("调用日志服务获取西药操作日志失败，请求参数为：{}", queryVO, e);
            return PageVO.newBuilder().pageNo(queryVO.getPage()).pageSize(queryVO.getPageSize()).result(Lists.newArrayList()).build();
        }
    }
}
