package paydemo.manager.db;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import paydemo.common.SysConstant;
import paydemo.dao.dbmodel.PayJobDetailDO;
import paydemo.dao.mapper.PayJobDetailMapper;

import java.util.Date;
import java.util.List;


/**
 * @auther YDXiaa
 * <p>
 * jobDetail Manager.
 */
@Repository
public class PayJobDetailManager {

    @Autowired
    private PayJobDetailMapper payJobDetailMapper;

    /**
     * 批量插入JobDetail.
     *
     * @param payJobDetailDOList 作业任务单.
     */
    @Transactional(rollbackFor = Throwable.class)
    public void batchAddJobDetail(List<PayJobDetailDO> payJobDetailDOList) {
        payJobDetailDOList.forEach(payJobDetailDO -> payJobDetailMapper.insert(payJobDetailDO));
    }

    /**
     * 查询满足条件作业单.
     *
     * @param shardingMark 分片标记位.
     * @param readCount    读取作业单数量.
     * @return 作业单列表.
     */
    public List<PayJobDetailDO> queryPayJobDetailList(int shardingMark, int readCount) {

        LambdaQueryWrapper<PayJobDetailDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayJobDetailDO::getShardingMark, shardingMark)
                .lt(PayJobDetailDO::getNextExecTime, new Date()) // 小于当前时间.
                .orderByAsc(PayJobDetailDO::getNextExecTime)
                .last("limit " + readCount);

        return payJobDetailMapper.selectList(queryWrapper);
    }

    /**
     * 修改作业任务单状态.
     *
     * @param payJobDetailDO 任务作业单.
     * @return 更新数量.
     */
    public int modifyPayJobDetailStatus(PayJobDetailDO payJobDetailDO) {

        LambdaUpdateWrapper<PayJobDetailDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PayJobDetailDO::getExecStatus, payJobDetailDO.getExecStatus())
                .set(PayJobDetailDO::getExecTimes, payJobDetailDO.getExecTimes())
                .set(PayJobDetailDO::getNextExecTime, payJobDetailDO.getNextExecTime())
                .set(PayJobDetailDO::getUpdateUser, SysConstant.SYS_USER)
                .set(PayJobDetailDO::getUpdateDate, new Date())
                .eq(PayJobDetailDO::getId, payJobDetailDO.getId());

        return payJobDetailMapper.update(payJobDetailDO, updateWrapper);
    }
}
