package paydemo;

import org.junit.Test;
import paydemo.dao.dbmodel.PayOrderDO;
import paydemo.manager.helper.BeanCopier;
import paydemo.manager.model.PayOrderBO;

/**
 * @auther YDXiaa
 * <p>
 * 对象拷贝器.
 */
public class BeanCopierTest extends BaseSpringBootSupport {


    @Test
    public void objCopy(){

        PayOrderBO payOrderBO = new PayOrderBO();
        payOrderBO.setPayNo("abc");

        PayOrderDO payOrderDO = BeanCopier.objCopy(payOrderBO, PayOrderDO.class);

        System.out.println(payOrderDO);
    }

}
