package paydemo.dao.dbmodel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @auther YDXiaa
 * baseDO.
 */
@Data
public class BaseDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建人.
     */
    @TableField("create_user")
    private String createUser;

    /**
     * 创建时间.
     */
    @TableField("create_date")
    private Date createDate;

    /**
     * 更新人.
     */
    @TableField("update_user")
    private String updateUser;

    /**
     * 更新时间.
     */
    @TableField("update_date")
    private Date updateDate;

}
