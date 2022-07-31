package paydemo.manager.db;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import paydemo.dao.dbmodel.ChannelDetailFeeDO;
import paydemo.dao.dbmodel.ChannelDetailInfoDO;
import paydemo.dao.dbmodel.ChannelInfoDO;
import paydemo.dao.mapper.ChannelDetailFeeMapper;
import paydemo.dao.mapper.ChannelDetailInfoMapper;
import paydemo.dao.mapper.ChannelInfoMapper;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.util.SwitchStatusEnum;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @auther YDXiaa
 * <p>
 * 渠道信息.
 */
@Repository
public class ChannelDetailInfoManager {

    @Autowired
    private ChannelDetailInfoMapper channelDetailInfoMapper;

    @Autowired
    private ChannelDetailFeeMapper channelDetailFeeMapper;

    @Autowired
    private ChannelInfoMapper channelInfoMapper;

    /**
     * 添加渠道详情信息.
     *
     * @param channelDetailInfoDO 渠道详情信息.
     * @param channelDetailFeeDO  渠道计费信息.
     */
    @Transactional(rollbackFor = Throwable.class)
    public void addChannelDetailInfo(ChannelDetailInfoDO channelDetailInfoDO, ChannelDetailFeeDO channelDetailFeeDO) {
        channelDetailInfoMapper.insert(channelDetailInfoDO);
        if (Objects.nonNull(channelDetailFeeDO)) {
            channelDetailFeeMapper.insert(channelDetailFeeDO);
        }
    }

    /**
     * 查询渠道.
     *
     * @param payTool    支付工具.
     * @param paySubTool 支付工具子类型.
     * @param bizType    业务类型.
     * @return 渠道列表.
     */
    public List<ChannelDetailInfoBO> queryChannelDetailInfo(String payTool, String paySubTool, List<String> selectPayTypes, String bizType) {

        LambdaQueryWrapper<ChannelDetailInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelDetailInfoDO::getPayTool, payTool)
                .eq(ChannelDetailInfoDO::getPaySubTool, paySubTool)
                .in(ChannelDetailInfoDO::getPayType, selectPayTypes)
                .eq(!Strings.isNullOrEmpty(bizType), ChannelDetailInfoDO::getBizType, bizType)
                .eq(ChannelDetailInfoDO::getChannelDetailStatus, SwitchStatusEnum.OPEN.getSwitchStatusCode());

        List<ChannelDetailInfoDO> detailInfoDOList = channelDetailInfoMapper.selectList(queryWrapper);

        return detailInfoDOList.stream().map(channelDetailInfoDO -> {

            // 渠道基本信息.
            ChannelInfoDO channelInfoDO = queryChannelInfo(channelDetailInfoDO.getChannelNo());
            // 渠道计费信息.
            ChannelDetailFeeDO channelDetailFeeDO = queryChannelDetailFee(channelDetailInfoDO.getChannelDetailNo());

            ChannelDetailInfoBO channelDetailInfoBO = new ChannelDetailInfoBO();

            BeanUtil.copyProperties(channelInfoDO, channelDetailInfoBO);
            BeanUtil.copyProperties(channelDetailInfoDO, channelDetailInfoBO);
            BeanUtil.copyProperties(channelDetailFeeDO, channelDetailInfoBO);

            return channelDetailInfoBO;
        }).collect(Collectors.toList());
    }

    /**
     * 查询渠道计费信息.
     *
     * @param channelDetailNo 渠道详情编码.
     * @return 渠道计费信息.
     */
    private ChannelDetailFeeDO queryChannelDetailFee(String channelDetailNo) {

        LambdaQueryWrapper<ChannelDetailFeeDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelDetailFeeDO::getChannelDetailNo, channelDetailNo);

        return channelDetailFeeMapper.selectOne(queryWrapper);
    }

    /**
     * 查询渠道基本信息.
     *
     * @param channelNo 渠道编码.
     * @return 渠道信息.
     */
    private ChannelInfoDO queryChannelInfo(String channelNo) {

        LambdaQueryWrapper<ChannelInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelInfoDO::getChannelNo, channelNo);

        return channelInfoMapper.selectOne(queryWrapper);
    }

    /**
     * 根据渠道详情编号查询渠道详情信息.
     *
     * @param channelDetailNo 渠道详情编号.
     * @return 渠道详情信息.
     */
    public ChannelDetailInfoDO queryChannelDetailInfoByDetailNo(String channelDetailNo) {
        LambdaQueryWrapper<ChannelDetailInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelDetailInfoDO::getChannelDetailNo, channelDetailNo);
        return channelDetailInfoMapper.selectOne(queryWrapper);
    }
}
