<%@ page import="com.octo.captcha.image.ImageCaptcha"%>
<%
ImageCaptcha captcha = (ImageCaptcha)request.getSession().getAttribute(com.octo.captcha.j2ee.servlet.ImageCaptchaEngineInitializerServlet.JCAPTCHA);
String res = request.getParameter("response");
boolean human = false;

try{
    human= captcha.validateResponse(res).booleanValue();
}catch(Throwable e){
%>
<html>
<head></head>
<body><center>
<a href="index.html">Back home</a>
<H1>Gimpy test result</H1>
<br>
 <H2>You'll have to retry : this test is outdated.</H2>
 <BR/>
 </center>
</body>
</html>


 <%
    return;
}finally{
request.getSession().setAttribute(com.octo.captcha.j2ee.servlet.ImageCaptchaEngineInitializerServlet.JCAPTCHA,null);
captcha = null;
}
%>

<html>
<head></head>
<body><center>
<a href="index.html">Back home</a>
<H1>Gimpy test result</H1>
<br>

<%if(human){%>
 <H2>You seem to be human</H2>
 <BR/>
 <img src="images/human.jpg"/>



 <%}else{%>
<H2>You're like a robot for me</H2>
 <BR/>
 <img src="images/robot.jpg"/>




 <%}%>


</center>
</body>
</html>
