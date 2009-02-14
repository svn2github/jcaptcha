<html>
<body>
<h2>Simple Captcha Servlet sample</h2>
<br/>
<br/>
<h4><%=request.getParameter("message")==null?"":request.getParameter("message")%></h4>

<br/>

<form action="submit.action" method="post">
     <img src="jcaptcha.jpg" /> <input type="text" name="jcaptcha" value="" />
     <input type="submit"/>
</form>
</body>
</html>
