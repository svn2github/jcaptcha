package com.octo.captcha.sound.gimpy;


import com.octo.captcha.sound.SoundCaptcha;
import com.octo.captcha.component.sound.utils.*;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class GimpySound extends SoundCaptcha {

    private String response;

    public GimpySound(String thequestion,Sound thechallenge,String theresponse) {
        super(thequestion,thechallenge);
        this.response = theresponse;
    }

    public Boolean validateResponse(Object theresponse) {
        if((theresponse != null)&&(theresponse instanceof String)) {
            return this.validateResponse((String)theresponse);
        }
        else{
            return Boolean.FALSE;
        } 
    }
    
    public Boolean validateResponse(String theresponse) {
        return Boolean.valueOf(theresponse.equals(this.response));
    }

    public void writeObject() {
    }
    
    public void readObject() {
    }

	public Boolean hasGetChalengeBeenCalled() {
		return null;
	}

}
