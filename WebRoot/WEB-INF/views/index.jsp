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
<script src="<%=basePath%>js/jquery/jquery-3.4.1.min.js"></script>
<base href="<%=basePath%>">
<title>调试工具(不支持https)</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript">
	function addParameter(self) {
		var id = Math.floor(Math.random() * 1000000);
		$(self).parent().parent().parent().prepend("<tr id='" + id + "'><td><input type='text' value='请输入参数名' onfocus='clean(this)'onchange='setInputName(this)' /></td> <td><input type='text' value='请输入参数值'onfocus='clean(this)'   /></td><td><input type='button' value='-' onclick='$(\"#" + id + "\").remove()'   /></td></tr>");
	}

	function showModel(index) {
		hideModel();
		$(".model").eq(index).show();
	}

	function hideModel() {
		$(".model").hide();
	}

	function setInputName(self) {
		var td = $(self).parent();
		var next_td = td.next();
		var input = next_td.children().get(0);
		input.name = self.value;
	}

	function clean(self) {
		var $self = $(self);
		if ($self.val() == "请输入参数名" || $self.val() == "请输入参数值") {
			$(self).val("")
			$(self).unbind("focus");
		}
	}
</script>
</head>

<body onload="showModel(0)">
	<div>
		<label>接口调试器(默认使用测试账号test) </label>
	</div>

	<div>
		接口：<select onchange="showModel(this.value)">
			<c:forEach items="${requestScope.list}" var="item" varStatus="v">
				<option value="${v.index}">${item.name}</option>
			</c:forEach>
		</select>
	</div>
	<div>----------------------------------------------</div>
	<div>
		<c:forEach items="${requestScope.list}" var="interfaceNotesEntity"
			varStatus="v">
			<div class="model" id="model_${v.index}">
				<div>
					请求：<input id="name" disabled="disabled"
						value="${interfaceNotesEntity.name}" />
				</div>
				<div>
					URL：<input id="url" style="width: 100%;"
						value="<%=basePath%>${interfaceNotesEntity.url}" />
				</div>
				<div style="display: none;">
					类型：<input id="type" value="${interfaceNotesEntity.type}" />
				</div>
				<div>----------------------------------------</div>
				<form id="form_${v.index}" method="post"
					enctype="multipart/form-data"
					action="<%=basePath%>${interfaceNotesEntity.url}">
					<table>
						<tr>
							<td></td>
							<td><input type="button" value="点击新增参数+"
								onclick="addParameter(this)" /></td>
						</tr>


						<tr>
							<td><input type="text" value="key" class="parameterName"
								disabled="disabled" /></td>

							<td><input type="text"
								value="SbKNNbc2x6LGjTHq2ZPvV4ghWMOEuqzDKG"
								name="key" class="parameterValue" /></td>

						</tr>
						<tr>
							<td>类型:${interfaceNotesParameter.type}</td>
							<td>备注:${interfaceNotesParameter.describe}</td>

						</tr>

						<c:forEach items="${interfaceNotesEntity.interfaceNotesParameter}"
							var="interfaceNotesParameter">

							<tr>
								<td><input type="text"
									value="${interfaceNotesParameter.name}" class="parameterName"
									disabled="disabled" /></td>

								<c:if
									test="${interfaceNotesParameter.typeValue!=1700 && interfaceNotesParameter.typeValue!=1800}">
									<td><input type="text"
										value="${interfaceNotesParameter.value}"
										name="${interfaceNotesParameter.name}" onfocus="clean(this)"
										class="parameterValue" /></td>
								</c:if>
								<c:if test="${interfaceNotesParameter.typeValue==1700}">
									<td><input type="file"
										name="${interfaceNotesParameter.name}" class="parameterValue" /></td>
								</c:if>
								<c:if test="${interfaceNotesParameter.typeValue==1800}">
									<td><input type="file"
										name="${interfaceNotesParameter.name}" class="parameterValue" /></td>
								</c:if>
								
								<td><input type="button" value="删除参数" onClick="var tr = this.parentNode.parentNode;tr.parentNode.removeChild(tr.nextElementSibling);tr.parentNode.removeChild(tr);"></td>
							</tr>
							<tr>
								<td>类型:${interfaceNotesParameter.type}</td>
								<td>备注:${interfaceNotesParameter.describe}</td>

							</tr>
						</c:forEach>
						<tr>
							<td><input type="submit" value="提交"></td>
						</tr>
					</table>
				</form>
			</div>
		</c:forEach>
	</div>
</body>
</html>
