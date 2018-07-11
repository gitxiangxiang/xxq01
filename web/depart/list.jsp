<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="bean.Depart" %>
<%@ page import="utils.PageBean" %>
<!DOCTYPE html>
<!-- saved from url=(0052)http://getbootstrap.com/docs/4.0/examples/dashboard/ -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta name="description" content="">
		<meta name="author" content="">

		<title>Dashboard Template for Bootstrap</title>
		<!-- Bootstrap core CSS -->
        <link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="${pageContext.request.contextPath}/static/css/dashboard.css" rel="stylesheet">
		<style type="text/css">
			/* Chart.js */
			
			@-webkit-keyframes chartjs-render-animation {
				from {
					opacity: 0.99
				}
				to {
					opacity: 1
				}
			}
			
			@keyframes chartjs-render-animation {
				from {
					opacity: 0.99
				}
				to {
					opacity: 1
				}
			}
			
			.chartjs-render-monitor {
				-webkit-animation: chartjs-render-animation 0.001s;
				animation: chartjs-render-animation 0.001s;
			}
		</style>
	</head>

	<body>
		<!--引入抽取的topbar-->
        <jsp:include page="${pageContext.request.contextPath}/commons/bar.jsp">
            <jsp:param value="depart" name="activeUri"/>
            <jsp:param name="title" value="部门列表"/>
        </jsp:include>


		<div class="container-fluid">
			<div class="row">
				<!--引入侧边栏-->


				<main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
					<h2><a class="btn btn-sm btn-success" href="emp" >部门添加</a></h2>
					<div class="table-responsive">
						<table class="table table-striped table-sm">
							<thead>
								<tr>
									<th>部门号</th>
									<th>部门名</th>
									<th>经理工号</th>
								</tr>
							</thead>
							<tbody>
							<%
								PageBean pageBean = (PageBean) request.getAttribute("page");
								if(pageBean!=null){
                                    LinkedList<Depart> pageData = pageBean.getPageData();
                                    for(Depart l:pageData){
                                        out.println("<tr>");
                                        out.println("<td>"+l.getId()+"</td>");
                                        out.println("<td>"+l.getDepartname()+"</td>");
                                        out.println("<td>"+l.getStaffid()+"</td>");
                                        out.println("</tr>");
                                    }
                                }
							%>
							</tbody>
						</table>
                        当前${requestScope.page.currentPage }/${requestScope.page.totalPage }页     &nbsp;&nbsp;

                        <a href="${pageContext.request.contextPath }/depart/list?currentPage=1">首页</a>&nbsp;&nbsp;
                        <a href="${pageContext.request.contextPath }/depart/list?currentPage=${requestScope.page.currentPage>1?requestScope.page.currentPage-1:1}">上一页 </a>&nbsp;&nbsp;
                        <a href="${pageContext.request.contextPath }/depart/list?currentPage=${requestScope.page.currentPage<requestScope.page.totalPage?requestScope.page.currentPage+1:requestScope.page.totalPage}">下一页 </a>&nbsp;&nbsp;
                        <a href="${pageContext.request.contextPath }/depart/list?currentPage=${requestScope.page.totalPage}">末页</a>&nbsp;&nbsp;
					</div>
				</main>
				<form id="deleteEmpForm"  method="post">
					<input type="hidden" name="_method" value="delete"/>
				</form>
			</div>
		</div>

		<!-- Bootstrap core JavaScript
    ================================================== -->
		<!-- Placed at the end of the document so the pages load faster -->
        <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery-3.2.1.slim.min.js"></script>
        <script type="text/javascript"  src="${pageContext.request.contextPath}/static/js/popper.min.js"></script>
        <script type="text/javascript"  src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>

        <!-- Icons -->
        <script type="text/javascript"  src="${pageContext.request.contextPath}/static/js/feather.min.js"></script>

		<script>
			feather.replace()
		</script>
		<script>
			$(".deleteBtn").click(function(){
			    //删除当前员工的
			    $("#deleteEmpForm").attr("action",$(this).attr("del_uri")).submit();
			    return false;
            });
		</script>
	</body>
</html>