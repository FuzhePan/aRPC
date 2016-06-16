import java.io.IOException;

/**
 * Created by FuzhePan on 2016/6/15.
 */
public class Provicder2 {
    public static void main(String[] args){

        ServiceMap.addService("HelloWorld",HelloWorldImpl.class);

        Starter starter = new Starter();
        try {
            starter.start(12702);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
