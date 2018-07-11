package dao;

import bean.Depart;
import utils.PageBean;

import java.util.LinkedList;

/**
 * 部门Dao层接口
 */
public interface DepartDao {
    /**
     * 列出全部部门信息
     * @return 部门信息列表
     */
    LinkedList departList();


    /**
     * 通过员工id查询部门名称
     * @param staffid
     * @return 部门实体类，包含所查找元组
     */
    Depart findById(int staffid);

    /**
     * 通过部门名称查询部门信息
     * @param departname
     * @return 部门实体类，包含所查找元组
     */
    Depart findByName(String departname);

    /**
     * 向部门信息表插入一条新的数据
     * @param depart
     * @return 是否成功执行插入操作
     */
    boolean departInsert(Depart depart);

    /**
     * 通过部门名删除一条信息
     * @param departname
     * @return 是否成功删除成功
     */
    boolean deleteByName(String departname);

    /**
     * 更新部门信息
     * @param departname,dp
     * @return 是否更新成功
     */
    boolean updateByName(String departname,Depart dp);

    /**
     * 分页展示部门信息
     * @param pageBean
     * @return PageBean
     */
    PageBean listPage(PageBean pageBean);
}
