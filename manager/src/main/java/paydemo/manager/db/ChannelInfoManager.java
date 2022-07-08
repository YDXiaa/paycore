package paydemo.manager.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import paydemo.dao.dbmodel.ChannelInfoDO;
import paydemo.dao.mapper.ChannelInfoMapper;

/**
 * @auther YDXiaa
 * <p>
 * 渠道信息.
 */
@Repository
public class ChannelInfoManager {

    @Autowired
    private ChannelInfoMapper channelInfoMapper;

    /**
     * 添加渠道信息.
     *
     * @param channelInfoDO 渠道信息.
     */
    public void addChannelInfo(ChannelInfoDO channelInfoDO) {
        channelInfoMapper.insert(channelInfoDO);
    }


}
