package android.fasa.edu.br.webview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by pcego on 06/06/18.
 */

public class Util {

    protected static boolean isConnected(Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }

        return false;

    }

    protected static void warnDialog(Context context) {
        Toast message = Toast.makeText(context,
                R.string.alertMessage,Toast.LENGTH_LONG);
        message.setGravity(Gravity.CENTER,0,0);
        message.show();

    }

}
