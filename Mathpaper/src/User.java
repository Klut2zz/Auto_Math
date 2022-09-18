import java.util.Objects;

/**
 * @author 30347
 */
public class User {
  private String id;
  private String psw;
  private String type;
  private boolean convert;

  public User(String s, String s1, String s2) {
    this.id = s;
    this.psw = s1;
    this.type = s2;
  }

  public User() {
    this.id = "None";
    this.psw = "None";
    this.type = "None";
    this.convert = false;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPsw() {
    return psw;
  }

  public void setPsw(String psw) {
    this.psw = psw;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isConvert() {
    return convert;
  }

  public void setConvert(boolean convert) {
    this.convert = convert;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id.equals(user.id) && psw.equals(user.psw);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, psw);
  }
}
