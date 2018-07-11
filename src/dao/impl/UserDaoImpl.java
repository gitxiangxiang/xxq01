package dao.impl;

import bean.User;
import dao.UserDao;
import utils.DBUtils;

import java.util.LinkedList;

/**
 * Created by  waiter on 18-7-8  下午7:14.
 *
 * @author waiter
 */
public class UserDaoImpl implements UserDao {
    private static final UserDaoImpl userDaoImpl=new UserDaoImpl();

    private UserDaoImpl(){}

    @Override
    public User findByUserName(String userName) {
        LinkedList userName1 = DBUtils.getListBySome(User.class, "userName", userName);
        return userName1.size()>0?(User) userName1.remove(0):null;
    }

    public synchronized static UserDaoImpl getUserDaoImpl() {
        return userDaoImpl;
    }
}
