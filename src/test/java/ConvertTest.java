import org.junit.jupiter.api.Test;

public class ConvertTest {
    @Test
    void T(){
        byte a = (byte)1;
        byte b = (byte)2;
        //1000 0010
        short i = (short) (a<<8 | b);
        System.out.println(i);

    }
}
