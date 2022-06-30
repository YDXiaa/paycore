package paydemo.web.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import paydemo.common.StringsExt;
import paydemo.dao.mapper.TableNameHelper;

import java.util.Map;

/**
 * @auther YDXiaa
 * <p>
 * mybatis plus分页插件、动态表名插件配置.
 */
@Configuration
public class MyBatisPlusConfiguration {

    /**
     * mybatis插件.
     *
     * @return intercpetor.
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页插件.
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        // 动态表名插件.
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandlerMap(createTableHandlerMap());
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);

        return interceptor;
    }

    /**
     * 创建handlerMap.
     *
     * @return handlerMap.
     */
    private Map<String, TableNameHandler> createTableHandlerMap() {

        Map<String, TableNameHandler> handlerMap = Maps.newHashMap();

        // 支付主表.
        handlerMap.put("T_PAY_ORDER", ((sql, tableName) -> {
            String shardingDate = TableNameHelper.getShardingMark();

            if (Strings.isNullOrEmpty(shardingDate)) {
                return tableName;
            }

            return StringsExt.joinStr("_", tableName, shardingDate);
        }));

        // 支付资金单表.
        handlerMap.put("T_PAY_FUND", ((sql, tableName) -> {
            String shardingDate = TableNameHelper.getShardingMark();

            if (Strings.isNullOrEmpty(shardingDate)) {
                return tableName;
            }

            return StringsExt.joinStr("_", tableName, shardingDate);
        }));

        return handlerMap;
    }
}
