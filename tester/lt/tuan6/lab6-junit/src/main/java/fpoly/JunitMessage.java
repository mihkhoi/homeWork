package fpoly;

public
class JunitMessage {
  private
    String message;

  public
    JunitMessage(String message) {
        this.message = message;
    }

  public
    void printMessage() {
        System.out.println(message);
        int divide = 10 / 0; // chia cho 0 -> ArithmeticException
    }

  public
    String printHiMessage() {
        message = "Hi " + message;
        System.out.println(message);
        return message;
    }
}
