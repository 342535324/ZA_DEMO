<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>接口文档(不支持https)</title>
</head>
 
<body>
	<div>${requestScope.codeInfo}</div>
	<br/>
	<div>--------------------------</div>
	<br/>
	<div>${requestScope.str}</div>
	</body>
</html>
