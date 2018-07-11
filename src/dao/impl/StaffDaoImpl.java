package dao.impl;

import bean.Staff;
import dao.StaffDao;
import utils.DBUtils;
import utils.PageBean;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by  waiter on 18-7-9  下午9:18.
 *
 * @author waiter
 */
public class StaffDaoImpl implements StaffDao {
    private static final StaffDaoImpl staffDao = new StaffDaoImpl();
    private StaffDaoImpl(){}
    @Override
    public PageBean findByPageAndDepartment(PageBean pageBean,int department) {
        return DBUtils.getPageBySome(pageBean,Staff.class,"department",department+"");
    }

    @Override
    public Staff findByUserName(String userName) {
        return (Staff) DBUtils.getListBySome(Staff.class,"userName",userName).remove(0);
    }

    @Override
    public PageBean findByPageAndDepartmentAndIsWork(PageBean pageBean, int department, boolean iswork) {
        Map<String,String> map = new TreeMap<>();
        map.put("department",department+"");
        map.put("iswork",String.valueOf(iswork));
        return DBUtils.getPageBySome(pageBean,Staff.class,map);
    }

    @Override
    public Staff findById(int id) {
        return (Staff) DBUtils.getObjectById(Staff.class,id);
    }

    @Override
    public boolean save(Staff staff) {
        if(staff.getId()>0) {
            return DBUtils.update(staff);
        }else {
            return DBUtils.insert(staff);
        }
    }

    public synchronized static StaffDaoImpl getStaffDao() {
        return staffDao;
    }
}
