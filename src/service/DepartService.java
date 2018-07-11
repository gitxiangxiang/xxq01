package service;

import bean.Depart;
import dao.DepartDao;
import dao.impl.DepartDaoImpl;
import utils.PageBean;

import java.util.LinkedList;

/**
 * @ Author     ：Bzy.
 * @ Date       ：Created in 下午4:28 18-7-10
 */
public class DepartService {
    private final DepartDao departDao = DepartDaoImpl.getDepartDaoImpl();
    private static final DepartService departService=new DepartService();
    private DepartService(){}

    /**
     * 列出全部部门信息
     * @return 部门信息列表
     */
    public LinkedList departList(){
        return departDao.departList();
    }


    /**
     * 通过员工id查询部门名称
     * @param staffid
     * @return 部门实体类，包含所查找元组
     */
    public Depart findById(int staffid){
        return departDao.findById(staffid);
    }

    /**
     * 通过部门名称查询部门信息
     * @param departname
     * @return 部门实体类，包含所查找元组
     */
    public Depart findByName(String departname){
        return departDao.findByName(departname);
    }

    /**
     * 向部门信息表插入一条新的数据
     * @param depart
     * @return 是否成功执行插入操作
     */
    public boolean departInsert(Depart depart){
        return departDao.departInsert(depart);
    }

    /**
     * 通过部门名删除一条信息
     * @param departname
     * @return 是否成功删除成功
     */
    public boolean deleteByName(String departname){
        return departDao.deleteByName(departname);
    }

    /**
     * 更新部门信息
     * @param departname,dp
     * @return 是否更新成功
     */
    public boolean updateByName(String departname,Depart dp){
        return departDao.updateByName(departname,dp);
    }

    /**
     * 分页展示部门信息
     * @param pageBean
     * @return PageBean
     */
    public PageBean listPage(PageBean pageBean){
        return departDao.listPage(pageBean);
    }

    public static DepartService getDepartService(){
        return departService;
    }
}
