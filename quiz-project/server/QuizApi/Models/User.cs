namespace QuizApi.Models;
public
class User {
public
  int Id {
    get;
    set;
  }
public
  string Username {
    get;
    set;
  }
  = "";
public
  byte[] PasswordHash {
    get;
    set;
  }
  = System.Array.Empty<byte>();
public
  string FullName {
    get;
    set;
  }
  = "";
public
  int RoleId {
    get;
    set;
  }
public
  bool IsActive {
    get;
    set;
  }
  = true;
}
