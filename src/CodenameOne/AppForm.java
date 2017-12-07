package zzwierko.coursetracker;

import java.io.IOException;

import com.codename1.io.Log;
import com.codename1.ui.Form;
import com.codename1.ui.Image;

class AppForm extends Form {
    AppForm() {
        super();

        getTitleArea().setPreferredH(0);

        Image bg_image;
        try {
            bg_image = Image.createImage("/login_background.jpeg");
        }
        catch (IOException e) {
            Log.e(e);
            bg_image = Image.createImage(200, 200, Config.WHITE);
        }
        getAllStyles().setBgImage(bg_image);
    }
}