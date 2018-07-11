package servlet;

import bean.User;
import service.UserService;
import utils.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by  waiter on 18-7-6.
 *
 * @author waiter
 */
@WebServlet(name = "LoginServlet",urlPatterns = "/do_login")
public class LoginServlet extends HttpServlet {
    UserService userService=UserService.getUserService();
    Logger logger=Logger.getLogger(this.getClass().getName());


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        logger.info(request.getRemoteAddr()+"尝试使用用户"+username+"登录");
        User byUserName = userService.findByUserName(username);
        if(byUserName==null){
            request.setAttribute("msg","用户名或密码错误");
            logger.info(request.getRemoteAddr()+"尝试使用用户"+username+"登录失败");
            request.getRequestDispatcher("/login.jsp").forward(request,response);
        }else if (BCrypt.checkpw(request.getParameter("password"),byUserName.getPassWd())){
            logger.info(request.getRemoteAddr()+"尝试使用用户"+username+"登录成功");
        request.getSession().setAttribute("loginInfo",byUserName);
        response.sendRedirect("/index.jsp");
        }else {
            request.setAttribute("msg","用户名或密码错误");
            logger.info(request.getRemoteAddr()+"尝试使用用户"+username+"登录失败");
            request.getRequestDispatcher("/login.jsp").forward(request,response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request,response);
    }
}
