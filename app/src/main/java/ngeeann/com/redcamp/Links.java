package ngeeann.com.redcamp;

public class Links {
    //link to main activity stuff
    String camp_programme = "www.np.edu.sg";
    String course_finder = "https://www.np.edu.sg/Pages/diplomas.aspx";
    String campus_explorer = "https://www.np.edu.sg/pages/tour.aspx";
    String ask_red_camp = "https://www.np.edu.sg/redcamp/pages/askredcamp.aspx";

    //API
    String login = "http://bryanlowsk.com/RedCamp/API/login.php";
    String register = "http://bryanlowsk.com/RedCamp/API/addUsers.php";


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

    public Links() {

    }
}

