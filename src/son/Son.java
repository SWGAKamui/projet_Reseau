package son;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;


public class Son {

	AudioClip audioClip;
	URL urlclip ;

	public Son(String url)
	{
		try {
			urlclip= new URL("file:" + url);
			audioClip = Applet.newAudioClip(urlclip);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	public void play(){
		audioClip.play();
	}
	public void stop(){
		audioClip.stop();
	}
}