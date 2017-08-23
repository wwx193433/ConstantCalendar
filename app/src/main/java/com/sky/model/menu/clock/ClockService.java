package com.sky.model.menu.clock;

import java.util.List;

/**
 * Created by Administrator on 17-8-11.
 */
public interface ClockService {
    /**
     * 保存数据
     * @param cg
     * @return
     */
    boolean saveGetupData(Clock cg);

    /**
     * 根据主键删除记录
     * @param cgId
     * @return
     */
    boolean deleteGetupById(int cgId);

    /**
     * 全部数据查询
     * @return
     */
    List<Clock> queryGetupData();

    /**
     * 单条记录查询
     * @param id
     * @return
     */
    Clock queryClockById(int id);

    /**
     * 修改CLOCK
     * @param clock
     */
    void updateClock(Clock clock);
}
