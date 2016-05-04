package com.mushroom.hui.test.jdbc;

import com.mushroom.hui.common.jdbc.BaseDao;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yihui on 16/4/28.
 */
public class BaseDaoTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDaoTest.class);

    private BaseDao baseDao;

    @Before
    public void setUp() {
        ApplicationContext apc = new ClassPathXmlApplicationContext("classpath*:spring/jdbc.xml");
        baseDao = apc.getBean("baseDao", BaseDao.class);
    }


    @Test
    public void queryTest() {
        String sql = "select * from samp_db.TradeRate limit 1";
        Map<String, Object> ans = baseDao.queryMap(sql);
        LOGGER.info("The ans is : {}", ans);
    }

    @Test
    public void insertTest() {
        String sql = "insert into samp_bd.user (username, password, address) values (\"二灰\", \"erhui\", \"woda\")";
        baseDao.execute(sql);

        String querySql = "select * from user limit 2";
        List<Map<String, Object>> ans2 = baseDao.query(querySql, new RowMapper<Map<String, Object>>() {

            @Override
            public Map<String, Object> mapRow(ResultSet resultSet, int i) throws SQLException {
                Map<String, Object> map = new HashMap<>();
                map.put("id", resultSet.getInt("id"));
                map.put("username", resultSet.getString("username"));
                map.put("password", resultSet.getString("password"));
                map.put("address", resultSet.getString("address"));
                return map;
            }
        });
        LOGGER.info("result: : {}", ans2);
    }
}
