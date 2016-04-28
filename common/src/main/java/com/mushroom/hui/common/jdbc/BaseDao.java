package com.mushroom.hui.common.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by yihui on 16/4/28.
 */
public class BaseDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);

    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleJdbcInsert;

    public BaseDao() {
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Map<String, Object> queryMap(String sql) {
        try {
            return jdbcTemplate.queryForMap(sql);
        } catch (Exception e) {
            LOGGER.error("query sql error! sql: {} e: {}", sql, e);
            return Collections.emptyMap();
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> mapper) {
        try {
            return jdbcTemplate.query(sql, mapper);
        } catch (Exception e) {
            LOGGER.error("query sql error! sql: {} e: {}", sql, e);
            return Collections.emptyList();
        }
    }

    public void execute(String sql) {
        jdbcTemplate.execute(sql);
    }

}

