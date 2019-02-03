package com.allandroidprojects.ecomsample.startup;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.Default_Notification;
import com.allandroidprojects.ecomsample.Login;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.fragments.ImageListFragment;
import com.allandroidprojects.ecomsample.miscellaneous.EmptyActivity;
import com.allandroidprojects.ecomsample.notification.NotificationCountSetClass;
import com.allandroidprojects.ecomsample.options.CartListActivity;
import com.allandroidprojects.ecomsample.options.SearchResultActivity;
import com.allandroidprojects.ecomsample.options.WishlistActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.Vector;

import android.content.Context;

import static org.apache.commons.lang3.ArrayUtils.toArray;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int notificationCountCart = 0;
    static ViewPager viewPager;
    static TabLayout tabLayout;
    FirebaseStorage storage;
    public StorageReference storageReference;
    public UploadTask uploadTask;
    public Context context;
    public String personemailfortoken;
    public String token;
    public static String personemail_txt;
    public  Context context2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseStorage storage = FirebaseStorage.getInstance();
         File dir=getFilesDir();


         Log.d("DIRECTORY","comeon:::"+dir);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (viewPager != null) {
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }


      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        Log.d("enteringlogin", "main1");

        final   FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Log.d("Entered", "Logingoing");
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
           try { Log.d("LOG","::");
                writeTousertxt("users.txt",personemail_txt,dir,this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Login i = new Login();
            try {
                personemailfortoken = i.alreadylogin(
                        MainActivity.this, mAuth.getCurrentUser());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // final FirebaseUser user = mAuth.getCurrentUser();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("MainActivity", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = Objects.requireNonNull(task.getResult()).getToken();
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("MainActivity", msg);
                        Log.d("", "opened");
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                        Log.d("Context", "Checking" + getApplicationContext() + "" + getBaseContext() + this + "" + context);
                        token();
                        TimeZone tz = TimeZone.getTimeZone("GMT+5:30");
                        Calendar c = Calendar.getInstance(tz);
                        String time = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", c.get(Calendar.MINUTE));
                        ////////////By mistake app opened code pending///////////
                        // default_open();


                    }
                });

        //  Default_Notification obj = new Default_Notification();
        //obj.activeHour("Yes");

        try{
            token();
        }catch (Exception e)
        {
            Log.d("TOKEN","NOT OPEN");
        }
        try {
            activeHour("Yes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void token() {

        readFiletokenid("token_id.csv",token,personemailfortoken,MainActivity.this);
        writeTotoken_id(token,"token_id.csv",personemailfortoken,MainActivity.this);

    }

   /* private void default_open(Context context) {

        Log.d("Default_noti","Not started");
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_cart);
        NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCountCart);
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Log.d("openedsearch", "search90");
            startActivity(new Intent(MainActivity.this, SearchResultActivity.class));
            return true;
        } else if (id == R.id.action_cart) {

           /* NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCount);
            invalidateOptionsMenu();*/
            startActivity(new Intent(MainActivity.this, CartListActivity.class));

           /* notificationCount=0;//clear notification count
            invalidateOptionsMenu();*/
            return true;
        } else {
            startActivity(new Intent(MainActivity.this, EmptyActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        ImageListFragment fragment = new ImageListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_1));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 2);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_2));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 3);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_3));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 4);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_4));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 5);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_5));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 6);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_6));
        viewPager.setAdapter(adapter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item1) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_item2) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_item3) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_item4) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_item5) {
            viewPager.setCurrentItem(4);
        } else if (id == R.id.nav_item6) {
            viewPager.setCurrentItem(5);
        } else if (id == R.id.my_wishlist) {
            startActivity(new Intent(MainActivity.this, WishlistActivity.class));
        } else if (id == R.id.my_cart) {
            startActivity(new Intent(MainActivity.this, CartListActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, EmptyActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    //////////////////////NOTIFICATION_ACTION_INFORMATION//////////////////////////////////

    public void writeToAnyFile(String str, String title, String message, String personname, String localFileName, String personemail, String time, Context context) throws IOException {
        Log.d("Received", "Notification");
       // File dir=getFilesDir();
        //  localFileName is with the file with extension which needs the content to be written on it.
        File file = new File(context.getApplicationInfo().dataDir, localFileName);
        Log.d("Context00", "msg" +context.getApplicationInfo().dataDir);

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
      /*  if (!file.exists() && !file.isDirectory()) {
            bufferedWriter.write("Message");
            bufferedWriter.write(",");
            bufferedWriter.write("Category");
            bufferedWriter.write(",");
            bufferedWriter.write("Time");
            bufferedWriter.write(",");
            bufferedWriter.write("Action");
            bufferedWriter.write("\n");
        }*/
        bufferedWriter.write(message);
        bufferedWriter.write(",");
        bufferedWriter.write(title);
        bufferedWriter.write(",");
        bufferedWriter.write(time);
        bufferedWriter.write(",");
        bufferedWriter.write(str);
        bufferedWriter.write("\n");

        bufferedWriter.close();
        Log.d("Received", "ReceiverYes9");
        //readFromAnyFile(localFileName, context);
        uploadAnyFile("users", personemail, localFileName, context);

    }

    public void readFromAnyFile(String sourceFileNameWExt, Context context) {
        File dir = context.getFilesDir();
        File file = new File(getFilesDir(), sourceFileNameWExt);
        Log.d("Context", "msg" + dir);
        String readData = "";
        //File f = new File()
        try {
            // Handle Csv file properly ASAP!!!.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
//                Log.d("status", line);
                readData += line;
            }
            bufferedReader.close();
            Log.d("status", readData);


        } catch (IOException e) {
            e.printStackTrace();
            Log.d("status", "error!");
            Log.d("status", file.getPath());
        }

    }

    public void uploadAnyFile(String firebaseFolder, String personemail, String sourceFileName, Context context1) {
        Log.d("Uploading", "Storagebucket");
        //File dir = context.getFilesDir();
        File file = new File(context1.getApplicationInfo().dataDir, sourceFileName);
        Log.d("Context01", "msg" +context1.getApplicationInfo().dataDir);
        /*if(file.exists())
            Log.d("status", "exists");
        else
            Log.d("status", "No!");*/
        Uri file2 = Uri.fromFile(file);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ecommercenotify.appspot.com");
        StorageReference storageRef = storage.getReference();

        StorageReference riv = storageRef.child(firebaseFolder + "/" + personemail + "/" + sourceFileName);
        UploadTask uploadTask = riv.putFile(file2);
        Log.d("Uploading1", "Storagebucket1");

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Succes", "Uploaded noti_action");
            }
        });
    }

    //////////////////////TOKEN_Id OF EACH USER///////////////////////

    public boolean readFiletokenid(String sourceFileNameWExt, String token, String personemailfortoken, Context context) {
        File dir = context.getFilesDir();
        File file = new File(dir, sourceFileNameWExt);
        String readData = "";
        //File f = new File()
        try {
            // Handle Csv file properly ASAP!!!.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = token;

            while ((bufferedReader.readLine()) != null) {
                if (bufferedReader.readLine() == line) {
                    return true;
                }
            }
            bufferedReader.close();
            Log.d("status", readData);


        } catch (IOException e) {
            e.printStackTrace();
            Log.d("status", "error!");
            Log.d("status", file.getPath());
        }

        return false;

    }


    public void writeTotoken_id(String token, String localFileName, String personemail, Context context1) {
        Log.d("Received", "ReceiverYes11");
        // Boolean check=true;
        //  localFileName is with the file with extension which needs the content to be written on it.
        File file = new File(context1.getFilesDir(), localFileName);
        if (!file.exists()) {
            BufferedWriter bufferedWriter = null;
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.write(token);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Received", "ReceiverYes12");
            uploadtoken_file("users", personemail, localFileName, context1);

        } else if (file.exists()) {

            boolean check = readFiletokenid(localFileName, token, personemail, context1);
            if (!check) {
                BufferedWriter bufferedWriter = null;
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter(file, true));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedWriter.write(token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedWriter.write(",");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedWriter.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("Received", "ReceiverYes12");
                uploadtoken_file("users", personemail, localFileName, context);
            }

        }

    }

    public void uploadtoken_file(String firebaseFolder, String personemail, String sourceFileName, Context context1) {
        Log.d("Uploading", "Storagebucket_token");
        File dir = getFilesDir();
        File file = new File(dir, sourceFileName);
        if (file.exists())
            Log.d("status", "exists");
        else
            Log.d("status", "No!");
        Uri file2 = Uri.fromFile(file);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ecommercenotify.appspot.com");
        StorageReference storageRef = storage.getReference();

        StorageReference riv = storageRef.child(firebaseFolder + "/" + personemail + "/" + sourceFileName);
        UploadTask uploadTask = riv.putFile(file2);
        Log.d("Uploading1", "Storagebucket1token");

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Succes", "Uploadedtoken");
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////

    void openactivehour() {
        Intent s = new Intent(MainActivity.this, Default_Notification.class);
        startActivity(s);
    }

    //////////////////////////////////  USERS.txt Single file //////////////////////////////////////////////////////////////////

    public void writeTousertxt(String localFileName, String personemail,File dir,Context context) throws IOException {
        Log.d("Received", "usertxt");
       //  localFileName is with the file with extension which needs the content to be written on it.//
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ecommercenotify.appspot.com");
        StorageReference storageRef = storage.getReference();
        final StorageReference firebaseFileRef = storageRef.child( localFileName );

        final File localFile = new File(dir,"users.txt");
        firebaseFileRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("SUCCESS ACTIVE",":");
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
                Log.d("status" ,"not downloaded");
            }
        });

     //  File file = new File(dir, localFileName);
        Log.d("Contextuser", "msg" + dir);
       // boolean check = readFromusertxtFile("users.txt", personemail,dir,context);
       // if (!check) {
            File file1 = new File(dir, localFileName);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file1, true));
            bufferedWriter.write(personemail);
            bufferedWriter.write("\n");
            bufferedWriter.close();
            Log.d("Received", "Writeuser");
            uploadusertxtFile(personemail, localFileName,dir,context);
       // } else {
            Log.d("already", "membered");

    //    }
    }

    private void uploadusertxtFile(String personemail, String localFileName,File dir, Context context) {

        Log.d("Uploading", "Storagebucket_token");
       //File dir =getFilesDir();
        File file = new File(dir, localFileName);
        if (file.exists())
            Log.d("status", "exists");
        else
            Log.d("status", "No!");
        Uri file2 = Uri.fromFile(file);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ecommercenotify.appspot.com");
        StorageReference storageRef = storage.getReference();

        StorageReference riv = storageRef.child(localFileName);
        UploadTask uploadTask = riv.putFile(file2);
        Log.d("Uploading1", "Storagebucketusertxt");

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Succes", "Uploadedusertxt");
            }
        });


    }

    public boolean readFromusertxtFile(String sourceFileNameWExt, String email,File dir,Context c) throws IOException {

        Log.d("Reader123", "inside read");

       // File dir = c.getFilesDir();
        File file = new File(dir, sourceFileNameWExt);
        Log.d("READ",":::"+dir);
        BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            Log.d("status", line);
            if (line.equals(email)) {
                Log.d("Reading", "Already saved");
                return true;
            } else {
                Log.d("Reading", "Not found");
            }

        }
        bufferedReader.close();
        return false;
    }



////////////////////////////////////////  ACTIVE_HOURS OF EACH USER  //////////////////////////////////////////////////////



    public void activeHour(String check) throws IOException { TimeZone tz = TimeZone.getTimeZone("GMT+5:30");
        Calendar c = Calendar.getInstance(tz);
        int time = Integer.parseInt(String.format("%02d", c.get(Calendar.HOUR_OF_DAY)));

        Date dl=Calendar.getInstance().getTime();
        SimpleDateFormat format=new SimpleDateFormat("dd");
        String formatedate=format.format(dl);
        int p=Integer.parseInt(formatedate);
        Log.d("Received", "default"+p);
        Log.d("PERSONINFO2","::::"+personemail_txt);
        //localFileName is with the file with extension which needs the content to be written on it.
        try {
            Log.d("TRY ","entered");
           /* URL website = new URL("https://storage.googleapis.com/ecommercenotify.appspot.com/users/" + personemail_txt + "/" + "active_hours.csv");
            Log.d("Downloaded",":file");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream("active_hours.csv");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            Log.d("READ",":::");*/
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://ecommercenotify.appspot.com");
            StorageReference storageRef = storage.getReference();
            final StorageReference firebaseFileRef = storageRef.child( "users" + "/" + personemail_txt +"/" + "active_hours.csv" );

            final File localFile = new File(getFilesDir(),"active_hours.csv");
            Log.d("Dir",".."+getFilesDir());
            firebaseFileRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("SUCCESS ACTIVE",":");
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    // ...
                    Log.d("status" ,"not downloaded");
                }
            });
            Log.d("Downloaded",":file");
           // File inputFile = new File("active_hours.csv");
            // Read existing file
            Vector<String> data;
            if(check.equals("No"))
            {
                data =changeCharAt(time,p,1);
                File file= new File(getFilesDir(),"active_hours.csv");
                file.delete();
                writeToactiveFile(data,"active_hours.csv");

            }else if (check.equals("Yes"))
            {    Log.d("YES","YES");
                data =changeCharAt(time,p,3);
                Log.d("DATA",":"+data);
                File file= new File(getFilesDir(),"active_hours.csv");
                file.delete();
                writeToactiveFile(data,"active_hours.csv");
            }



           /* CSVReader reader = new CSVReader(new FileReader(inputFile),',');
            List<String[]> csvBody = reader.readAll();
            // get CSV row column  and replace with by using row and column
            String []line;
            int count=0;
            int x;
            while ((line=reader.readNext()) != null) {
                if(line[24].equals(Integer.toString(p)))
                {
                    x= Integer.parseInt(line[24]);
                }
                else {
                }
                count++;
            }
            //   if(count<7 && x==p)
            // {

            //}
            // csvBody.get(p)[time] = ;
            reader.close();
            // Write to CSV file which is open
            CSVWriter writer = new CSVWriter(new FileWriter(inputFile), ',');
            writer.writeAll(csvBody);
            writer.flush();
            writer.close();*/
        } catch (Exception e) {
            Log.d("Catch","Catchenter"+check);
           // File dir = getFilesDir();
            //File file = new File(dir,"active_hours.csv");
            //FileWriter output = null;
            /*try {
                output = new FileWriter(file);
            } catch (IOException e1) {
                e1.printStackTrace();
            }*/
            Log.d("Catch","Catchenter1");
            String data = "00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,0"+p;
            String header = "00:00-01:00,01:00-02:00,02:00-03:00,03:00-04:00,04:00-05:00,05:00-06:00,06:00-07:00,07:00-08:00,08:00-09:00,09:00-10:00,10:00-11:00,11:00-12:00,12:00-13:00,13:00-14:00,14:00-15:00,15:00-16:00,16:00-17:00,17:00-18:00,18:00-19:00,19:00-20:00,20:00-21:00,21:00-22:00,22:00-23:00,23:00-00:00,date\n";
            Log.d("Catch","Catchenter22");
            Vector<String> intial_data= new Vector<String>(2);
            Log.d("Catch","Catchenter33");
            intial_data.add(header);
            Log.d("Catch","Catchenter44");
            intial_data.add(data);
            Log.d("Catch","Catchenter55");
            writeheader(header.toString(),data.toString(),"active_hours.csv");
          /*  CSVWriter writer1 = new CSVWriter(output);
            String[] header = {"00:00-01:00", "01:00-02:00", "02:00-03:00", "03:00-04:00", "04:00-05:00", "05:00-06:00", "06:00-07:00", "07:00-08:00", "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00", "21:00-22:00", "22:00-23:00", "23:00-00:00", "date"};
            writer1.writeNext(header);
            Log.d("CSv","header"+header);
            String[] data = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", Integer.toString(p)};
            if (check.equals("No")) {
                data[time] = Integer.toString(Integer.parseInt(data[time]) + 1);
                Log.d("CSV","Data"+data[time]);
                writer1.writeNext(data);
                upload_file("users", personemail_txt, "active_hours.csv");
            } else if (check.equals("Yes")) {
                Log.d("check",": "+check);
                data[time] = Integer.toString(Integer.parseInt(data[time]) + 3);
                writer1.writeNext(data);
                Log.d("Catch","CatchenterYes");
                upload_file("users", personemail_txt, "active_hours.csv");


            }*/
        }


        Log.d("Received", "Default2");
        //readFromAnyFile(localFileName, context);
        //uploadAnyFile("users", personemail, localFileName, context);

    }


   public  void personinfo(String email)
    {
        personemail_txt=email;
        Log.d("PERSONINFO","::"+personemail_txt);

    }


    public void upload_file(String firebaseFolder, String personemail, String sourceFileName) {
        Log.d("Uploading", "Storagebucket_activehours");
        File dir = getFilesDir();
        File file = new File(dir, sourceFileName);

        Uri file2 = Uri.fromFile(file);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ecommercenotify.appspot.com");
        StorageReference storageRef = storage.getReference();

        Log.d("Email::",""+personemail);
        StorageReference riv = storageRef.child(firebaseFolder + "/" + personemail + "/" + sourceFileName);
        UploadTask uploadTask = riv.putFile(file2);
        Log.d("Uploading1", "Storagebuckket upload");

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Succes", "Uploaded active_hours");
            }
        });
    }

    private  Vector<String> readFromactiveFile(String sourceFileNameWExt){
        File dir = getFilesDir();
      //  String dir = "D:\\";
        File file = new File( sourceFileNameWExt);
        Log.d("File",""+dir+sourceFileNameWExt);
        String readData = "";
        Vector<String> data = new Vector<String>(0);
        //File f = new File()
        try {
            // Handle Csv file properly ASAP!!!.
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/data/user/0/com.allandroidprojects.ecomsample/files/active_hours.csv") {
                @Override
                public int read(@NonNull char[] cbuf, int off, int len) throws IOException {
                    return 0;
                }

                @Override
                public void close() throws IOException {

                }
            });
            Log.d("File123","11");
            String line = null;
            while((line = bufferedReader.readLine() ) != null ){
//                Log.d("status", line);
                //readData += line;
                Log.d("CHECKING11",":"+line);
                data.addElement(line);
                Log.d("CHECKING",":");
                //System.out.println(line);

            }
            bufferedReader.close();

        } catch (IOException e){
            Log.d("CHECKING12",":");
            e.printStackTrace();
        }
        return data;
    }

    private  Vector<String> changeCharAt(int time_ ,int k ,int a){
        //read a line from active_hours.csv
        //change a value in this line
        //write back to the file
        Log.d("VECTOR","::");
        Vector<String> data = readFromactiveFile("active_hours.csv");
      // .toArray(data);
       Log.d("VECTOR22",":: "+ data.toString());
        //System.out.println(data);
        // k = 2;  // date*2
         //time_ = 4;

        Boolean True_ = true;
        try{  Log.d("VECTOR33","::");
            Boolean kuch = data.get(0).isEmpty();}
        catch(Exception e){
            True_ = false;

        }
        int no_of_rows=0;
        int x=0;
        try{
            for(x=0;x<8;x++)
            {
                data.get(x);
            }
            no_of_rows=x;
        }catch (Exception e){
            no_of_rows=x;
        }
        if (no_of_rows==8){
            Log.d("Row","8");
            for (int i=1;i<9;i++)
                {if (i==8)
                    {
                        data.remove(1);
                        StringBuilder new_row  = new StringBuilder("00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,"+k);
                        time_*=3;
                        int value_to_set=Integer.parseInt(new_row.substring(time_,time_+2)) +a;
                        if (value_to_set<10){
                            new_row.setCharAt(time_,'0');
                        }
                        else
                        {
                            new_row.setCharAt(time_,(Integer.toString(value_to_set).charAt(0)));
                        }
                       // new_row.setCharAt(time_+1,(Integer.toString(value_to_set).charAt(1)));
                        break;

                    }

                    if(Integer.parseInt(data.get(i).substring(73,75))==k)
                    {  StringBuilder row  = new StringBuilder(data.get(i));
                       time_*=3;

                       int value_to_set=Integer.parseInt(row.substring(time_,time_+2))+a;
                       if (value_to_set<10){
                           row.setCharAt(time_,'0');
                       }
                       else
                       {
                           row.setCharAt(time_,(Integer.toString(value_to_set).charAt(0)));
                       }
                      // row.setCharAt(time_+1,(Integer.toString(value_to_set).charAt(1)));
                       break;
                    }

                }


        }
        else{
            try { Log.d("Row","else");
                for (int i=1;i<8;i++){
                    Log.d("Row","forcheck"+Integer.parseInt(data.get(i).substring(73,74)));
                    if(Integer.parseInt(data.get(i).substring(73,75))==k)
                    {  Log.d("Row","Enterif"+i);
                        StringBuilder row  = new StringBuilder(data.get(i));
                        time_*=3;

                        int value_to_set=Integer.parseInt(row.substring(time_,time_+2))+a;
                        if (value_to_set<10){
                            Log.d("Row","nextif");
                            row.setCharAt(time_,'0');
                        }
                        else
                        {  Log.d("Row","nextelse");
                            row.setCharAt(time_,(Integer.toString(value_to_set).charAt(0)));
                        }
                       // row.setCharAt(time_+1,(Integer.toString(value_to_set).charAt(1)));
                        break;
                    }

                }
            }catch (Exception e){

                Log.d("Catch","active");
                StringBuilder new_row  = new StringBuilder("00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,"+k);
                time_*=3;
                int value_to_set=Integer.parseInt(new_row.substring(time_,time_+2)) +a;
                if (value_to_set<10){
                    new_row.setCharAt(time_,'0');
                }
                else
                {
                    new_row.setCharAt(time_,(Integer.toString(value_to_set).charAt(0)));
                }
              //  new_row.setCharAt(time_+1,(Integer.toString(value_to_set).charAt(1)));

            }

        }


       /* for(int i = 1;True_;){
            if(i==k){
                // we found the row
                // now time will tel at which index we have to edit in i
                StringBuilder row  = new StringBuilder(data.get(i));
               // System.out.println(row.charAt(time_));
                int value = Integer.parseInt(""+row.charAt(time_));
                try{
                    row.setCharAt(time_,(Integer.toString( value+ a).charAt(0)));
                    Log.d("ROW","::"+a);
                   // System.out.println("44444444444"+row.charAt(time_));
                }
                catch(Exception e){
                    row.setCharAt(time_,Integer.toString((value + a)% 10).charAt(0));
                    row.setCharAt(time_,Integer.toString((value + a)/ 10).charAt(0));
                }

                data.set(i, row.toString());

            }
          //  System.out.println(data.get(i));
            i++;
            try{Boolean kuch = data.get(i).isEmpty();}
            catch(Exception e){
                True_ = false;
            }
        }
       // StringBuilder myName = new StringBuilder("domanokz");
       // myName.setCharAt(4, 'x');

       // System.out.println(myName);*/
       Log.d("Data","return");
        return data;
    }

    private  void writeToactiveFile(Vector<String> data, String localFileName) throws IOException {
         Log.d("Entered","WRITING TO EDIT");
        //  localFileName is with the file with extension which needs the content to be written on it.
        File dir = getFilesDir();
        File file = new File( dir,localFileName);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        for(int i =0;i < 8; i++) {

            bufferedWriter.append(data.get(i)+"\n");
        }

        bufferedWriter.close();
        readFromactiveFile(localFileName);

        upload_file("users",personemail_txt,localFileName);

    }

    public void writeheader(String header,String first_row,String localFileName) throws IOException {
        Log.d("WRITEINITIAL","ACTIVE");
        File dir = getFilesDir();
        File file = new File(dir, localFileName);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(header);
        bufferedWriter.write(first_row);
        bufferedWriter.close();

        upload_file("users",personemail_txt,localFileName);
    }


}


