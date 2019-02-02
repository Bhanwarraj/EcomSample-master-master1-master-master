package com.allandroidprojects.ecomsample;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.options.SearchResultActivity;
import com.allandroidprojects.ecomsample.startup.ActionReceiver;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.SaveInformation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//import com.google.firebase.quickstart.auth.R;

public class Login extends AppCompatActivity implements View.OnClickListener {


    // SignInButton button;
    FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    public DatabaseReference mDatabase;
    public static final String SHARED_PREFS = "sharedpref";
    public static final String PersonNm = "text";
    public static final String Personelmid = "text";

    private String getPersonNm;
    private String getPersonelmid;
    public Context context;

    /*
    private String name, email;

    private String photo;

    private Uri photoUri;
    private String idToken;

   // public  ;

*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Entered", "Login");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // button = (SignInButton) findViewById(R.id.Glogin);
        findViewById(R.id.Glogin).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1099185445947-pv8tcuqvd0msof2kn0hj8i2fb0b6081m.apps.googleusercontent.com")
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN),
                        new Scope(PeopleServiceScopes.USERINFO_PROFILE),
                        new Scope(PeopleServiceScopes.USER_EMAILS_READ),
                        new Scope(PeopleServiceScopes.USER_BIRTHDAY_READ),
                        new Scope(PeopleServiceScopes.CONTACTS_READONLY))
                .build();

        /*GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference();


    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

  /*  @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
           /* if (requestCode == RC_SIGN_IN) {

                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                if (result.isSuccess()) {

                    // Google Sign In was successful, save Token and a state then authenticate with Firebase

                    GoogleSignInAccount account = result.getSignInAccount();



                    idToken = account.getIdToken();



                    name = account.getDisplayName();

                    email = account.getEmail();

                    photoUri = account.getPhotoUrl();

                    photo = photoUri.toString();



                    // Save Data to SharedPreference

                    sharedPrefManager = new SharedPrefManager(mContext);

                    sharedPrefManager.saveIsLoggedIn(mContext, true);



                    sharedPrefManager.saveEmail(mContext, email);

                    sharedPrefManager.saveName(mContext, name);

                    sharedPrefManager.savePhoto(mContext, photo);



                    sharedPrefManager.saveToken(mContext, idToken);

                    //sharedPrefManager.saveIsLoggedIn(mContext, true);



                    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

                    firebaseAuthWithGoogle();

                } else {

                    // Google Sign In failed, update UI appropriately

                   // Log.e(TAG, "Login Unsuccessful. ");

                    Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT)

                            .show();

                }

            }*/
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                //  Toast.makeText(Login.this, "Toast1", Toast.LENGTH_SHORT).show();
                Log.d("hello", "Hiii");
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(Login.this, "Toast2", Toast.LENGTH_SHORT).show();
                assert account != null;
                firebaseAuthWithGoogle(account);
                //  Toast.makeText(Login.this, "Sign", Toast.LENGTH_SHORT).show();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(Login.this, "Sign failed", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("fb", "firebaseAuthWithGoogle:" + acct.getId());
        //Toast.makeText(Login.this, "firebaseAuthWithGoogle", Toast.LENGTH_SHORT).show();
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(Login.this, "Signin successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            try {
                                updateUI(user);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // If sign in fails, display a message to cthe user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(Login.this, "Auth failed.", Toast.LENGTH_SHORT).show();

                            try {
                                updateUI(null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        startActivity(new Intent(Login.this, MainActivity.class));

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }


    public void updateUI(FirebaseUser user) throws IOException {

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            Log.d("UpdateUI","DEAD");
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            String token_id = acct.getIdToken();
            Uri personPhoto = acct.getPhotoUrl();
            Toast.makeText(Login.this, "Welcome " + personName, Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PersonNm, personName);
            editor.putString(Personelmid, personId);

            Log.d("writed", "txt");
            SaveInformation saveInformation = new SaveInformation(personName, personEmail, personFamilyName, personId, Login.this);

            mDatabase.child(personId).setValue(saveInformation);

            MainActivity dn=new MainActivity();
            Log.d("ACtIVE",":"+personEmail);
            dn.personinfo(personEmail);

            //MainActivity i=new MainActivity();
           // i.writeTousertxt("users.txt", personEmail,context);

            Log.d("csv", "csvmsg90");

            ActionReceiver obj = new ActionReceiver();
            obj.getuserinfo(personId, personName, personEmail);
            Log.d("csv", "csvmsg");




        }
    }

    public String alreadylogin(Context c, FirebaseUser user) throws IOException {
        String personname = user.getDisplayName();
        String personId = user.getUid();
        String personemail = user.getEmail();
        //writeTousertxt("users.csv",personemail,c);

        MainActivity dn=new MainActivity();
        Log.d("ACtIVE in Already Login",":"+personemail);
        dn.personinfo(personemail);

        ActionReceiver obj = new ActionReceiver();
        obj.getuserinfo(personId, personname, personemail);

        SearchResultActivity obj2 = new SearchResultActivity();
        obj2.getuserinfo_search(personemail);

        Default_Notification dq=new Default_Notification();
        dq.personinfo(personemail);
        Log.d("csv", "csvmsg90");
        return personemail;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.Glogin) {
            signIn();
        }
    }


    public void setUp() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        // Go to the Google API Console, open your application's
        // credentials page, and copy the client ID and client secret.
        // Then paste them into the following code.
        String clientId = "1099185445947-r48uetc6o99sahg34anqaovrs50k8tq9.apps.googleusercontent.com";
        String clientSecret = "8XFfSCUG00g9m1S8ThFpY4-q";

        // Or your redirect URL for web based applications.
        String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
        String scope = "https://www.googleapis.com/auth/contacts.readonly";

        // Step 1: Authorize -->
        String authorizationUrl =
                new GoogleBrowserClientRequestUrl(clientId, redirectUrl, Arrays.asList(scope)).build();

        // Point or redirect your user to the authorizationUrl.
        System.out.println("Go to the following link in your browser:");
        System.out.println(authorizationUrl);

        // Read the authorization code from the standard input stream.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("What is the authorization code?");
        String code = in.readLine();
        // End of Step 1 <--

        // Step 2: Exchange -->
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        httpTransport, jsonFactory, clientId, clientSecret, code, redirectUrl)
                        .execute();
        // End of Step 2 <--

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(tokenResponse);

        PeopleService peopleService =
                new PeopleService.Builder(httpTransport, jsonFactory, credential).build();

        ListConnectionsResponse response = peopleService.people().connections().list("people/me")
                .setPersonFields("names,emailAddresses")
                .execute();
        List<Person> connections = response.getConnections();


        Person contactToCreate = new Person();
        List names = new ArrayList<>();
        names.add(new Name().setGivenName("John").setFamilyName("Doe"));
        contactToCreate.setNames(names);

        Person createdContact = peopleService.people().createContact(contactToCreate).execute();
    }



}




