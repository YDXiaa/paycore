package paydemo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import paydemo.dao.dbmodel.PayOrderDO;
import paydemo.facade.PayQueryServiceFacade;
import paydemo.facade.model.PayQueryRequestDTO;
import paydemo.manager.db.PayOrderManager;
import paydemo.util.PayStatusEnum;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

/**
 * @auther YDXiaa
 * <p>
 * mockTest.
 */
public class MockTest extends BaseSpringBootSupport {


    @Autowired
    private PayQueryServiceFacade payQueryServiceFacade;

    @MockBean
    private PayOrderManager payOrderManager;

    @Test
    public void testQueryOrder() {


        PayOrderDO payOrderDO = new PayOrderDO();
        payOrderDO.setPayStatus(PayStatusEnum.SUCCESS.getStatusCode());

        // mock.
        doReturn(payOrderDO).when(payOrderManager).queryPayOrder(any(), any(), any());

        PayQueryRequestDTO requestDTO = new PayQueryRequestDTO();

        payQueryServiceFacade.payQuery(requestDTO);

    }

}
