import com.fcannizzaro.oshare.Oshare;
import com.fcannizzaro.oshare.annotations.Callback;
import com.fcannizzaro.oshare.annotations.Share;
import com.fcannizzaro.oshare.interfaces.ReadyListener;
import com.fcannizzaro.oshare.util.Shared;

/**
 * Created by Francesco Cannizzaro (fcannizzaro)
 */
public class Main implements ReadyListener {

    @Share
    private Integer java = 8;

    @Share
    private void alert(String str) {
        System.out.println("Alert: " + str);
    }

    public Main() {

        // register shared methods
        Shared.register(this);

        // connect and run
        Oshare.init("http://localhost:3000", Remote.class, this);

    }

    @Callback
    void apiRun(String value, Integer number) {
        System.out.println(String.format("Callback \"%s\" after %d ms!", value, number));
    }

    @Override
    public void onReady() {
        System.out.println("// ---- Print values ---- //");
        System.out.println(Remote.obj.node);
        System.out.println(Remote.obj.number);
        System.out.println(Remote.obj.flag);
        System.out.println("// ---------------------- //");
    }

    @Override
    public void onConnected() {
        Remote.api.run(Oshare.Cb(this));
    }

}
