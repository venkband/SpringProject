package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Autowired
    NamedParameterJdbcTemplate jdbc;

    Map<Integer, String> users = new HashMap<Integer, String>();
    Map<Integer, User> userobjects = new HashMap<Integer, User>();

    @RequestMapping("/greeting")
    public String greet(@RequestParam(name = "username") String user) {

        return "hello " + user;
    }

    @RequestMapping("/addition")
    public int sum(@RequestParam int a, @RequestParam int b) {

        return a + b;
    }

    @RequestMapping(value = "/wwe", method = RequestMethod.GET)
    public String check() {
        return "wwe";
    }

    @RequestMapping(value = {"/facebook", "/fb"}, method = RequestMethod.GET)
    public String fb() {
        return "facebook";
    }

    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    public String adduser(@RequestParam int userid,
                          @RequestParam String username) {
        if (users.containsKey(userid)) {
            return "user already exists";
        } else {
            users.put(userid, username);
            return "user added";
        }
    }

    @RequestMapping(value = "/getuser", method = RequestMethod.GET)
    public String getuser(@RequestParam int userid) {
        if (users.containsKey(userid)) {
            return users.get(userid);
        } else {
            return "inalid user";
        }
    }

    @RequestMapping(value = "/getuserdetails/{userid}", method = RequestMethod.GET)
    public String getuserdetails(@PathVariable("userid") int userid) {
        if (users.containsKey(userid)) {
            return users.get(userid);
        } else {
            return "inalid user";
        }
    }

    // other way to get or post or put or delete
    @GetMapping(value = "/getuserdetail/{userid}")
    public String getuserdetail(@PathVariable("userid") int userid) {

        if (users.containsKey(userid)) {
            return users.get(userid);
        } else {
            return "inalid user";
        }
    }

    @PostMapping("/jsonuser")
    public String jsonuser(@RequestBody User a) {
        System.out.println("'/jsonuser' invoked:" + a.id + " , " + a.name);
        userobjects.put(a.id, a);
        return "json object posted successfully";
    }

    @GetMapping("/getjasonuser")
    public @ResponseBody
    User getjasonuser(@RequestParam int id) {
        System.out.println("'/getjasonuser' returning:"
                + userobjects.get(id).id + " , " + userobjects.get(id).name);
        return userobjects.get(id);
    }

    @PostMapping("/signup")
    public void adduser(@RequestBody Loginuser l) {
        String qry = "insert into users values(:val1,:val2,:val3,:val4,:val5)";

        MapSqlParameterSource input = new MapSqlParameterSource();
        input.addValue("val1", l.getUsername());
        input.addValue("val2", l.getPwd());
        input.addValue("val3", l.getFirstname());
        input.addValue("val4", l.getLastname());
        input.addValue("val5", l.getContactno());

        jdbc.update(qry, input);
    }

    @GetMapping("/login")
    public List<Loginuser> getuser(@RequestParam String username,
                                   @RequestParam String pwd) {
        String qry = "select * from users where username = :val1 and pwd = :val2";

        MapSqlParameterSource selectqryparams = new MapSqlParameterSource();
        selectqryparams.addValue("val1", username);
        selectqryparams.addValue("val2", pwd);

        List<Loginuser> queryresults = jdbc.query(qry, selectqryparams, new RowMapper<Loginuser>() {

            @Override
            public Loginuser mapRow(ResultSet rs, int rowNum) throws SQLException {
                Loginuser u = new Loginuser();
                u.setUsername(rs.getString("username"));
                u.setContactno(rs.getLong("contactno"));
                u.setFirstname(rs.getString("Firstname"));
                u.setLastname(rs.getString("lastname"));
                u.setPwd(rs.getString("pwd"));
                return u;
            }
        });
        return queryresults;
    }
}

class User {
    int id;
    String name;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

