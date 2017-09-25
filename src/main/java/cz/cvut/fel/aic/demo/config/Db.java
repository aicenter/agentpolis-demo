package cz.cvut.fel.aic.demo.config;

import java.lang.String;
import java.util.HashMap;

public class Db {
  public String password;

  public String dbName;

  public String user;

  public Db(HashMap db) {
    this.password = (String) db.get("password");
    this.dbName = (String) db.get("db_name");
    this.user = (String) db.get("user");
  }
}
