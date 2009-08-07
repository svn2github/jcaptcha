<html>
<body>
<h2>Simple Captcha Servlet sample</h2>
<br/>
<h4><%=request.getParameter("message")==null?"":request.getParameter("message")%></h4>
<br/>

<form action="submit.action" method="post">
     <a href="jcaptcha.wav">Download audio Captcha</a>
     <br/> 
     <input type="text" name="jcaptcha" value="" />
     <input type="submit"/>
</form>
</body>
</html>
