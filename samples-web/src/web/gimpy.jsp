<%@ page import="com.octo.captcha.image.ImageCaptchaEngine,
                 java.awt.image.BufferedImage,
                 com.octo.captcha.image.gimpy.BasicGimpyEngine,
                 com.sun.imageio.plugins.jpeg.JPEG,
                 com.sun.image.codec.jpeg.JPEGCodec,
                 javax.imageio.ImageIO,
                 com.octo.captcha.image.ImageCaptcha,
                 com.octo.captcha.j2ee.servlet.ImageCaptchaEngineInitializerServlet"%>
<%
    ImageCaptcha captcha = ImageCaptchaEngineInitializerServlet.getEngine().getNextImageCaptcha();
    request.getSession().setAttribute(com.octo.captcha.j2ee.servlet.ImageCaptchaEngineInitializerServlet.JCAPTCHA,captcha);
%>

<html>
<head></head>
<body><center>
<a href="index.html">Back home</a>
<H1>Gimpy sample</H1>
<br>
<img src="servlet/captchatoimage"/>
<FORM ACTION="gimpyresult.jsp" METHOD="PUT">
<%=captcha.getQuestion()%><BR/>
<INPUT TYPE="TEXT" NAME="response"/><BR/>
<INPUT TYPE="SUBMIT"/>
</FORM>


</center>
</body>
</html>
