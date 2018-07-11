package service;

import bean.Staff;
import dao.StaffDao;
import dao.impl.StaffDaoImpl;
import utils.PageBean;

/**
 * Created by  waiter on 18-7-9  下午9:20.
 *
 * @author waiter
 */
public class StaffService {
    private static final StaffService staffService = new StaffService();
    private StaffService(){}
    StaffDao staffDao = StaffDaoImpl.getStaffDao();

    public PageBean findByPageAndDepartment(PageBean pageBean,int department){
        return staffDao.findByPageAndDepartment(pageBean,department);
    }

    public static StaffService getStaffService() {
        return staffService;
    }

    public Staff findByUserName(String userName){
        return staffDao.findByUserName(userName);
    }

    public PageBean findByPageAndDepartmentAndIsWork(PageBean pageBean,int department,boolean iswork){
       return staffDao.findByPageAndDepartmentAndIsWork(pageBean,department,iswork);
    }

    public boolean dismissStaff(int id){
        boolean b = false;
        Staff byId = staffDao.findById(id);
        byId.setIsWork(false);
        b = staffDao.save(byId);
        return b;
    }
}
