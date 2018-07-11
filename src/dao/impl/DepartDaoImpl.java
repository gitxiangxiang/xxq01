package dao.impl;

import bean.Depart;
import dao.DepartDao;
import utils.DBUtils;
import utils.PageBean;

import java.util.LinkedList;


/**
 * @ Author     ：Bzy.
 * @ Date       ：Created in 下午7:50 18-7-9
 * 部门Dao层接口实现类
 */
public class DepartDaoImpl implements DepartDao{
    private static final DepartDaoImpl departDaoImpl = new DepartDaoImpl();

    DepartDaoImpl(){}

    @Override
    public LinkedList departList(){
        LinkedList list = null;
        list = DBUtils.getList(Depart.class);
        if(list==null){
            return null;
        }else{
            return list;
        }
    }
    @Override
    public Depart findById(int staffid){
        LinkedList departName = DBUtils.getListBySome(Depart.class, "staffid", staffid+"");

        Depart depart = (Depart) departName.remove(0);
        if(depart==null){
            return null;
        }else{
            return depart;
        }
    }
    public Depart findByName(String departname){
        LinkedList departName = DBUtils.getListBySome(Depart.class, "departname", departname+"");
        if(departName.size()==0){
            return new Depart(0,"not exist",0);
        }else{
            return (Depart) departName.remove(0);
        }
    }

    public boolean departInsert(Depart depart){
        return DBUtils.insert(depart);
    }

    @Override
    public boolean deleteByName(String departname){

        Depart depart=findByName(departname);
        if(depart.getDepartname()==null){
            return false;
        }else{
            return DBUtils.delete(Depart.class,depart.getId());
        }

    }
    @Override

    public boolean updateByName(String departname,Depart dp){
        Depart depart = findByName(departname);
        Depart dp2 = new Depart(depart.getId(),dp.getDepartname(),dp.getStaffid());
        return DBUtils.update(dp2);
    }

    @Override
    public PageBean listPage(PageBean pageBean) {
        return DBUtils.getPage(pageBean,Depart.class);
    }
    public static DepartDaoImpl getDepartDaoImpl() {
        return departDaoImpl;
    }
}
