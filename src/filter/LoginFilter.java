package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by  waiter on 18-7-6.
 *
 * @author waiter
 */
@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {
    ServletContext servletContext;
    Logger logger=Logger.getLogger(this.getClass().getName());
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    servletContext=filterConfig.getServletContext();
    logger.info("LoginFilter start");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1. 获取请求资源，截取
        // /emp_sys/login.jsp
        String uri = request.getRequestURI();
        // 截取 【login.jsp或login】
        String requestPath = uri.substring(uri.lastIndexOf("/") + 1, uri.length());
        logger.info(requestPath);
        //2. 判断： 先放行一些资源：/login.jsp、/login
        if(uri.contains("static")){
            filterChain.doFilter(request, response);
        }else if ("do_login".equals(requestPath) || "login.jsp".equals(requestPath)||"register.jsp".equals(requestPath)||"register".equals(requestPath)) {
            // 放行
            filterChain.doFilter(request, response);
        } else {
            //3. 对其他资源进行拦截
            //3.1 先获取Session、获取session中的登陆用户(loginInfo)
            HttpSession session = request.getSession(false);
            // 判断
            if (session != null) {

                Object obj = session.getAttribute("loginInfo");

                //3.2如果获取的内容不为空，说明已经登陆，放行
                if (obj != null) {
                    // 放行
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    //3.3如果获取的内容为空，说明没有登陆； 跳转到登陆
                    uri = "/login.jsp";
                }

            } else {
                // 肯定没有登陆
                uri = "/login.jsp";
            }
            request.getRequestDispatcher(uri).forward(request, response);
        }
    }

    @Override
    public void destroy() {
    logger.info("LoginFilter stop");
    }
}
