package com.basti.bug_db_server.utils;

import ch.qos.logback.core.db.dialect.DBUtil;
import com.basti.bug_db_server.bean.BugInfo;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class DBUtils {


    private static final String DATABASE_URL = "jdbc:sqlite:android_bug.db";
    private static volatile ConnectionSource cs;
    private static volatile Dao<BugInfo, Integer> bugDao;
    private static DBUtils dbUtils;

    private DBUtils() throws SQLException {
        cs = new JdbcConnectionSource(DATABASE_URL);
        bugDao = DaoManager.createDao(cs, BugInfo.class);
    }

    public static DBUtils getInstance() {
        if (dbUtils == null) {
            synchronized (DBUtil.class) {
                if (dbUtils == null) {
                    try {
                        dbUtils = new DBUtils();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return dbUtils;
    }

    /**
     * 查询指定数量的uid>startId,id中含有category的数据
     * @param startId
     * @param category
     * @param count
     * @throws SQLException
     */
    public List<BugInfo> queryLatestBug(Integer startId, String category, Integer count) throws SQLException {

        System.out.println(startId+"  " + category);
        QueryBuilder<BugInfo,Integer> bugQueryBuilder = bugDao.queryBuilder();
        bugQueryBuilder.where().eq("checked","0").
                and().gt("uid",startId).
                and().like("id","%"+category+"%");
        bugQueryBuilder.orderBy("uid", true);
        bugQueryBuilder.limit(count.longValue());

        return bugDao.query(bugQueryBuilder.prepare());

    }


    /**
     * 更新bugInfo
     * @param bugInfo
     */
    public void updateBug(BugInfo bugInfo) throws SQLException {
        bugDao.update(bugInfo);
    }
}
