package com.adrosonic.adroscanner.modules.login

import android.accounts.AccountAuthenticatorActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.security.KeyChain
import android.webkit.WebSettings
import android.webkit.WebView
import com.adrosonic.adroscanner.R
import com.salesforce.androidsdk.accounts.UserAccount
import com.salesforce.androidsdk.auth.OAuth2
import com.salesforce.androidsdk.config.RuntimeConfig
import com.salesforce.androidsdk.rest.ClientManager
import com.salesforce.androidsdk.ui.OAuthWebviewHelper
import com.salesforce.androidsdk.ui.OAuthWebviewHelper.OAuthWebviewHelperEvents
import com.salesforce.androidsdk.ui.ServerPickerActivity
import com.salesforce.androidsdk.util.EventsObservable
import com.salesforce.androidsdk.util.SalesforceSDKLogger
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AccountAuthenticatorActivity(), OAuthWebviewHelperEvents {


    private lateinit var webViewHelper: OAuthWebviewHelper
    val loginOptions = ClientManager.LoginOptions.fromBundle(intent.extras)
    private var receiverRegistered: Boolean = false
    private lateinit var changeServerReceiver: ChangeServerReceiver

    companion object {
        val PICK_SERVER_REQUEST_CODE = 10
        private val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Setup the WebView
        sf_oauth_webview.settings.apply {
            useWideViewPort = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
            javaScriptEnabled = true
            allowFileAccessFromFileURLs = true
            javaScriptCanOpenWindowsAutomatically = true
            databaseEnabled = true
            domStorageEnabled = true
        }
        EventsObservable
                .get()
                .notifyEvent(
                        EventsObservable.EventType.AuthWebViewCreateComplete, sf_oauth_webview)
        webViewHelper = getOAuthWebViewHelper(
                this,
                loginOptions,
                sf_oauth_webview,
                savedInstanceState)

        //Let observers know
        EventsObservable
                .get()
                .notifyEvent(EventsObservable.EventType.LoginActivityCreateComplete, this)
        certAuthOrLogin()
        if (!receiverRegistered){
            changeServerReceiver = ChangeServerReceiver()
            val changeServerFilter = IntentFilter(ServerPickerActivity.CHANGE_SERVER_INTENT)
            registerReceiver(changeServerReceiver, changeServerFilter)
            receiverRegistered = true
        }


    }

    fun getOAuthWebViewHelper(
            calllback: OAuthWebviewHelperEvents,
            loginOptions: ClientManager.LoginOptions,
            webView: WebView,
            savedInstanceState: Bundle?): OAuthWebviewHelper{

        return OAuthWebviewHelper(this,
                    calllback,
                    loginOptions,
                    webView,
                    savedInstanceState)
    }

    fun certAuthOrLogin(){
        if (shouldUseCertBaseAuth()){
            val alias = RuntimeConfig.getRuntimeConfig(this).getString(RuntimeConfig.ConfigKey.ManagedAppCertAlias)
            SalesforceSDKLogger.d(TAG, "Cert based login flow being triggered with alias: $alias")
            KeyChain.choosePrivateKeyAlias(
                    this,
                    webViewHelper,
                    null,
                    null,
                    null,
                    0,
                    alias)
        }else{
            SalesforceSDKLogger.d(TAG, "User agent login flow being triggered")
            webViewHelper.loadLoginPage()
        }
    }

    fun shouldUseCertBaseAuth(): Boolean{
        return RuntimeConfig.getRuntimeConfig(this).getBoolean(RuntimeConfig.ConfigKey.RequireCertAuth)
    }

    override fun onAccountAuthenticatorResult(authResult: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadingLoginPage(loginUrl: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun finish(userAccount: UserAccount?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class ChangeServerReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null && intent.action != null) {
                val action = intent.action
                if (ServerPickerActivity.CHANGE_SERVER_INTENT == action) {
                    webViewHelper.loadLoginPage()
                }
            }
        }
    }

    inner class SPAuthCallback{

        fun receivedTokenResponse(tokenResponse: OAuth2.TokenEndpointResponse){
            webViewHelper
        }
    }
}
