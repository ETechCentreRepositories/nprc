package ngeeann.com.redcamp;

public class Links {

    /**
     * links in the main activity
     */
    private String camp_programme = "www.np.edu.sg";
    private String course_finder = "https://www.np.edu.sg/pages/diplomas.aspx";
    private String campus_explorer = "https://www.np.edu.sg/pages/tour.aspx";
    private String ask_red_camp = "https://www.np.edu.sg/redcamp/pages/askredcamp.aspx";

    /**
     * APIs for the login and register
     */
    private String login = "http://ehostingcentre.com/redcampadmin/API/login.php";
    private String register = "http://ehostingcentre.com/redcampadmin/API/addUsers.php";

    /**
     * terms of use and privacy policy
     */
    private String tnc = "https://www.np.edu.sg/Pages/terms.aspx";
    private String pp = "https://www.np.edu.sg/Pages/privacy.aspx";

    /**
     * Forget Password
     */
    private String forgetpassword = "http://www.ehostingcentre.com/redcampadmin/API/forgetPassword.php";

    public String getCamp_programme() {
        return camp_programme;
    }

    public String getCourse_finder() {
        return course_finder;
    }

    public String getCampus_explorer() {
        return campus_explorer;
    }

    public String getAsk_red_camp() {
        return ask_red_camp;
    }

    public String getLogin() {
        return login;
    }

    public String getRegister() {
        return register;
    }

    public String getTnc() {
        return tnc;
    }

    public String getPp() {
        return pp;
    }

    public String getForgetpassword() {
        return forgetpassword;
    }

    public Links() {

    }

}

