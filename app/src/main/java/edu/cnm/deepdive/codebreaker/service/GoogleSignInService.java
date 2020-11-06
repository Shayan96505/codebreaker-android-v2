package edu.cnm.deepdive.codebreaker.service;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import edu.cnm.deepdive.codebreaker.BuildConfig;
import io.reactivex.Single;

public class GoogleSignInService {

  private static Application context;

  private final GoogleSignInClient client;

  private GoogleSignInAccount account;

  //this constructor is used to help us get the info of the user from google
  private GoogleSignInService() {

    GoogleSignInOptions options = new GoogleSignInOptions.Builder()
        .requestEmail()
        .requestId()
        .requestProfile()
        .requestIdToken(BuildConfig.CLIENT_ID)
        .build();
// this is a bearer token, the back end takes this bearer token and passes it to google, and asks
    // google if the bearer token matches what it requests.

    client = GoogleSignIn.getClient(context, options);
  }

  public static void setContext(Application context) {
    GoogleSignInService.context = context;
  }

  public static GoogleSignInService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public GoogleSignInAccount getAccount() {
    return account;
  }

  //return a task to silently sign the user in if their token is expired!
  public Single<GoogleSignInAccount> refresh() {
    return Single.create((emitter) -> {
          client.silentSignIn()
              .addOnSuccessListener(this::setAccount)
              .addOnSuccessListener((account) -> {
                setAccount(account);
                emitter.onSuccess(account);
              })
              .addOnFailureListener(emitter::onError);
        }
    );
  }

  public void startSignin(Activity activity, int requestCode) {
    account = null;
    Intent intent = client.getSignInIntent();
    activity.startActivityForResult(intent, requestCode);
  }

  // we created this method to check for a completed sign-in.
  public Task<GoogleSignInAccount> completeSignIn(Intent data) {
    Task<GoogleSignInAccount> task = null;
    try {
      task = GoogleSignIn.getSignedInAccountFromIntent(data);
      setAccount(task.getResult(ApiException.class));
    } catch (ApiException e) {
      //exception will be pass automatically to onFailureListener
    }
    return task;
  }

  //regardless of whether the account signs out or not.... sign us out.
  public Task<Void> signOut() {
    return client.signOut()
        .addOnCompleteListener((ignored) -> setAccount(null));
  }

  private void setAccount(GoogleSignInAccount account) {
    this.account = account;
    if (account != null) {
      //noinspection ConstantConditions
      Log.d(getClass().getSimpleName(), account.getIdToken());
    }
  }

  private static class InstanceHolder {

    private static final GoogleSignInService INSTANCE = new GoogleSignInService();

  }

}
