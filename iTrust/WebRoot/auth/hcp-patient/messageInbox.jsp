<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>

<%@include file="/global.jsp" %>

<%
response.setHeader("Cache-Control", "no-cache, max-age=0, must-revalidate, no-store");
response.setDateHeader("Expires", 0);
pageTitle = "iTrust - View My Message ";
session.setAttribute("outbox",false);
session.setAttribute("isHCP",userRole.equals("hcp"));
%>

<%@include file="/header.jsp" %>

<div align=center>
	<h2>My Messages</h2>
	<%@include file="/auth/hcp-patient/mailbox.jsp" %>

</div>

<%@include file="/footer.jsp" %>
