package com.octo.captcha.module.servlet.image.sample;

import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author mag
 * @Date 14 févr. 2009
 */
public class SubmitActionServlet extends HttpServlet{

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
      String userCaptchaResponse = request.getParameter("jcaptcha");
      boolean captchaPassed = SimpleImageCaptchaServlet.validateResponse(request, userCaptchaResponse);
      if(captchaPassed){
            response.sendRedirect("index.jsp?message=captcha%20passed");
        }else{
            response.sendRedirect("index.jsp?message=captcha%20failed");
        }

    }
}
