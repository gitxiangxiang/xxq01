package servlet;

import bean.User;
import service.StaffService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by  waiter on 18-7-10  下午3:47.
 *
 * @author waiter
 */
@WebServlet(name = "StaffDeleteServlet",urlPatterns = "/staff/list/delete")
public class StaffDeleteServlet extends HttpServlet {
    Logger logger=Logger.getLogger(this.getClass().getName());
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StaffService staffService = StaffService.getStaffService();
        String id = request.getParameter("id");
        User loginInfo = (User) request.getSession().getAttribute("loginInfo");

        if(id!=null){
            if (loginInfo.getId()!=Integer.parseInt(id)) {
                boolean b = staffService.dismissStaff(Integer.parseInt(id));
                if (b) {
                    request.setAttribute("msg", "成功辞退员工");

                    logger.info(loginInfo + "辞退了" + id + "员工");
                } else {
                    request.setAttribute("msg", "未知错误");
                }
            }
        }
        request.getRequestDispatcher("/staff/list").forward(request,response);
    }
}
