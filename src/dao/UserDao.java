package dao;

import bean.User;

/**
 * Created by  waiter on 18-7-8  下午7:12.
 *
 * @author waiter
 */
public interface UserDao {
    /**
     * 通过用户名查找一个用户
     * @param userName
     * @return
     */
    User findByUserName(String userName);

}
