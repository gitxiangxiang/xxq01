package dao;

import bean.Staff;
import utils.PageBean;

/**
 * Created by  waiter on 18-7-9  下午9:17.
 *
 * @author waiter
 */
public interface StaffDao {
    /**
     * 分页查询,通过部门id
     * @param pageBean
     * @param department 部门id
     * @return
     */
    PageBean findByPageAndDepartment(PageBean pageBean,int department);

    /**
     * 通过用户名查找员工
     * @param userName
     * @return
     */
    Staff findByUserName(String userName);
    /**
     * 分页查询,通过部门id
     * @param pageBean
     * @param department 部门id
     * @param iswork
     * @return
     */
    PageBean findByPageAndDepartmentAndIsWork(PageBean pageBean,int department,boolean iswork);

    /**
     * 通过id查找员工
     * @param id
     * @return
     */
    Staff findById(int id);

    boolean save(Staff staff);
}
