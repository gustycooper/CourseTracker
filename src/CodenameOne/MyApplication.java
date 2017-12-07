package zzwierko.coursetracker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;

public class MyApplication {
    private Form current;
    private Resources theme;
    private User user;
    private int half_width;

    private String formatDate(String date) {
        SimpleDateFormat data_format = new SimpleDateFormat(Config.DATA_TIME_FORMAT);
        SimpleDateFormat display_format = new SimpleDateFormat(Config.DISPLAY_TIME_FORMAT);

        try {
            return display_format.format(data_format.parse(date));
        }
        catch (ParseException e) {
            return "";
        }
    }

    private ArrayList<Map<String, String>> sendRequest(String url, String... args) {
        ConnectionRequest req = new ConnectionRequest();
        req.setPost(false);
        req.setUrl(url);
        for (int i = 0; i < args.length; i += 2) {
            req.addArgument(args[i], args[i+1]);
        }

        NetworkManager.getInstance().addToQueueAndWait(req);

        try (Reader rdr = new InputStreamReader(new ByteArrayInputStream(req.getResponseData()))) {
            Map<String, Object> data = new JSONParser().parseJSON(rdr);
            return (ArrayList<Map<String, String>>)data.get("root");
        }
        catch (IOException | RuntimeException e) {
            Log.e(e);
            return new ArrayList<>();
        }
    }

    private void showLoginScreen() {
        current = new AppForm();

        Image logo_image;
        try {
            logo_image = Image.createImage("/login_logo.png");
        }
        catch (IOException e) {
            Log.e(e);
            logo_image = Image.createImage(100, 100, Config.WHITE);
        }

        Label logo_label = new Label();
        logo_label.setIcon(logo_image);
        logo_label.getAllStyles().setBorder(Border.createGrooveBorder(2));
        logo_label.getAllStyles().setMargin(20, 0, half_width-55, 0);

        TextField username_input = new TextField("", "Username", 50, TextArea.ANY);
        username_input.getAllStyles().setMargin(4, 2, 2, 2);

        TextField password_input = new TextField("", "Password", 50, TextArea.PASSWORD);
        password_input.getAllStyles().setMargin(1, 2, 2, 2);

        Button login_button = new Button("Login");
        login_button.getAllStyles().setBorder(Border.createGrooveBorder(2));
        login_button.getAllStyles().setFgColor(Config.WHITE);
        login_button.getAllStyles().setMargin(10, 10, 14, 0);
        login_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String username = username_input.getText();
                if (username.equals("")) {
                    showErrorDialog("Username is empty.");
                    return;
                }

                String password = password_input.getText();
                if (password.equals("")) {
                    showErrorDialog("Password is empty.");
                    return;
                }

                ArrayList<Map<String, String>> user_data = sendRequest(Config.USERS_URL, "user", username, "pw", password);
                if (user_data.isEmpty()) {
                    showErrorDialog("Invalid Username or Password.");
                    return;
                }

                user = new User(user_data.get(0));

                showMainScreen();
            }
        });

        current.add(logo_label)
               .add(username_input)
               .add(password_input)
               .add(login_button);

        current.show();
    }

    private void showMainScreen() {
        current = new AppForm();

        ArrayList<Map<String, String>> courses = sendRequest(Config.COURSES_URL, "uid", user.getToken());
        for (Map<String, String> c : courses) {
            Container card = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            card.getStyle().setBgColor(Config.WHITE);
            card.getStyle().setBgTransparency(50);
            card.getAllStyles().setMargin(10, 0, 10, 10);

            Label title_label = new Label(c.get("cid"));
            title_label.getAllStyles().setBorder(Border.createLineBorder(2));
            title_label.getAllStyles().setFgColor(Config.WHITE);

            card.add(title_label);

            ArrayList<Map<String, String>> assignments = sendRequest(Config.ASSIGNMENTS_URL, "cid", c.get("cid"));
            for (Map<String, String> a : assignments) {
                Container assign = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                assign.getAllStyles().setFgColor(Config.WHITE);

                Label assign_name_label = new Label(a.get("name"));
                assign_name_label.getAllStyles().setFgColor(Config.WHITE);

                Label assign_due_label = new Label(formatDate(a.get("due")));
                assign_due_label.getAllStyles().setFgColor(Config.WHITE);

                Label divider_label = new Label(Config.DIVIDER);
                divider_label.getAllStyles().setFgColor(Config.WHITE);

                assign.add(assign_name_label);
                assign.add(assign_due_label);
                assign.add(divider_label);

                card.add(assign);
            }

            current.add(card);
        }

        current.show();
    }

    private void showErrorDialog(String msg) {
        Dialog.show("Error!", msg, "OK", null);
    }

    public void init(Object context) {
        theme = UIManager.initFirstTheme("/theme");
        half_width = Display.getInstance().getDisplayWidth() / 2;

        Display.getInstance().lockOrientation(true);
        Log.bindCrashProtection(true);
    }

    public void start() {
        if (current != null){
            current.show();
        }
        else {
            showLoginScreen();
        }
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }

    public void destroy() {
    }
}