package lab5.bai4;

public
class PaymentCalculator {

  public
    enum Type { MALE,
                FEMALE,
                CHILD }

    public static int
    calc(Type type, int age) {
        if (age < 0 || age > 145)
            throw new IllegalArgumentException("Age phải trong 0..145");

        switch (type) {
        case MALE:
            if (age < 18)
                throw new IllegalArgumentException("MALE phải >= 18");
            if (age <= 35)
                return 100;
            if (age <= 50)
                return 120;
            return 140;

        case FEMALE:
            if (age < 18)
                throw new IllegalArgumentException("FEMALE phải >= 18");
            if (age <= 35)
                return 80;
            if (age <= 50)
                return 110;
            return 140;

        case CHILD:
            if (age <= 17)
                return 50;
            throw new IllegalArgumentException("CHILD phải 0..17");

        default:
            throw new IllegalArgumentException("Type không hợp lệ");
        }
    }
}
