set CLASSPATH=%CLASSPATH%;../jcaptcha-core-1.0-beta2/jcaptcha-core-1.0-beta2.jar;
set CLASSPATH=%CLASSPATH%;jcaptcha-samples-1.0-beta2.jar
java com.octo.captcha.image.utils.ImageCaptchaToJPEG com.octo.captcha.image.gimpy.BasicGimpyEngine . 10
java com.octo.captcha.image.utils.SimpleImageCaptchaToJPEG
pause