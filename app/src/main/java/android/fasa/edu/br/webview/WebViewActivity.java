package android.fasa.edu.br.webview;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewActivity extends AppCompatActivity {

    //Para acessar outra url substitua aqui
    private static final String URL_SITE = "https://livearq.com.br/application/prelogin/";
    private WebView webView;
    private ProgressBar progress;

    // Variável para tratar efeito de reload por gesto
    // deslizar para baixo
    protected SwipeRefreshLayout swipeRefresh;
    //Variável para configuração da webview
    private WebSettings settings;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_web);

        //Recuperando a barra de progresso do arquivo de layout xml
        progress = (ProgressBar) findViewById(R.id.progress);
        webView = (WebView) findViewById(R.id.webView);
        webViewConfiguration();

        // Verifica conexão internet
        if (!Util.isConnected(this)) {
            Util.warnDialog(this);
        }

        //Verificando o estado atual da webview
        //caso seja diferente de null a webview ira exibir
        //a última tela vista pelo usuário
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        }
        //Caso a instancia ainda não tenha sido executada
        //a URL_SITE é carregada
        else {
            webView.loadUrl(URL_SITE);
        }
        setWebViewClient(webView);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
        //Cores utilizadas na barra de progresso
        //a mesma irá oscilar a exibição de cada cor definida aqui
        swipeRefresh.setColorSchemeResources(
                R.color.lightSkyBlue,
                R.color.colorPrimary
        );

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //Método para fazer reload da tela
            //após o usuário arrastar a tela para baixo
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

    }

    private void setWebViewClient(WebView webview) {

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView webview, String url, Bitmap favicon) {
                super.onPageStarted(webview, url, favicon);
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView webview, String url) {
                super.onPageFinished(webview, url);
                progress.setVisibility(View.INVISIBLE);
                swipeRefresh.setRefreshing(false);
            }
        });


    }

    //Método utilizado para salvar o estado da instância em execução
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Salva a activity
        super.onSaveInstanceState(outState);
        //Salva o estado atual da webview
        webView.saveState(outState);
    }

    //Método utilizado para restaurar a última instância executada
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //Restaura a Activity
        super.onRestoreInstanceState(savedInstanceState);
        //Restaura o ultimo estado da webview
        webView.restoreState(savedInstanceState);
    }

    private void webViewConfiguration(){
        //Configurações da webview
        settings = webView.getSettings();
        //habilitando execução código javascript
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance()
                    .setAcceptThirdPartyCookies(webView, true);
        }else {
            CookieManager.getInstance().setAcceptCookie(true);
        }


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder message = new AlertDialog.Builder(this);
        message.setTitle(R.string.titleWarnMessage);
        message.setIcon(R.mipmap.ic_launcher);
        message.setMessage(R.string.warnMessage)
                .setCancelable(false).setPositiveButton(R.string.btnPositive,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.btnNegative,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        message.show();
    }
}
