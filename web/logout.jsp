<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><!DOCTYPE html>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1 >Hello !</h1>
<form action="${pageContext.request.contextPath}/do_logout" method="post">
    <input type="submit" value="Sign Out"/>
</form>
</body>
</html>