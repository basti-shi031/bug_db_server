package com.basti.bug_db_server;

import com.basti.bug_db_server.bean.BugInfo;
import com.basti.bug_db_server.response.BaseResponse;
import com.basti.bug_db_server.response.QueryBugListResponse;
import com.basti.bug_db_server.utils.DBUtils;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

/**
 *
 */
@RestController
public class BugController {

    private static DBUtils dbUtils;

    /**
     * 查找指定uid后的id中包含category的count个数据
     * @param startId
     * @param category
     * @param count
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/queryNotCheckedBug", method = RequestMethod.GET)
    public String queryNotChecked(@RequestParam(value = "startId", required = false, defaultValue = "0") Integer startId,
                                  @RequestParam(value = "category", required = false,defaultValue = "") String category,
                                  @RequestParam(value = "count",required = false,defaultValue = "5") Integer count) throws SQLException {

        dbUtils = DBUtils.getInstance();
        List<BugInfo> bugInfoList = dbUtils.queryLatestBug(startId,category,count);
        System.out.println(bugInfoList.size());
        return new Gson().toJson(new QueryBugListResponse(200,"success",bugInfoList));
    }

    @RequestMapping(value = "/updateBug",method = RequestMethod.POST )
    public String updateBug(@RequestParam(value = "bug") String bug) throws SQLException {
        dbUtils = DBUtils.getInstance();
        BugInfo bugInfo = new Gson().fromJson(bug,BugInfo.class);
        dbUtils.updateBug(bugInfo);
        return new Gson().toJson(new BaseResponse(200,"success"));
    }
}
