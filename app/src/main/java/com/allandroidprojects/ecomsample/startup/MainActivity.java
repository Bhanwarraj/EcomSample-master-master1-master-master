package com.allandroidprojects.ecomsample.startup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

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
   public  String token;
    private String personemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseStorage storage = FirebaseStorage.getInstance();


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

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();
        if (user == null) {
            Log.d("Entered","Logingoing");
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        } else{
            Login i = new Login();
            try {
                personemailfortoken=i.alreadylogin(
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
                        Log.d("Context","Checking"+getApplicationContext()+""+getBaseContext()+this +""+context);
                        TimeZone tz=TimeZone.getTimeZone("GMT+5:30");
                        Calendar c=Calendar.getInstance(tz);
                        String time=String.format("%02d",c.get(Calendar.HOUR_OF_DAY))+":"+String.format("%02d",c.get(Calendar.MINUTE));
                        ////////////By mistake app opened code pending///////////
                       // default_open();



                    }
                });

      //  Default_Notification obj = new Default_Notification();
        //obj.activeHour("Yes");
       // readFiletokenid("token_id.csv",token,personemailfortoken,MainActivity.this);
        //writeTotoken_id(token,"token_id.csv",personemailfortoken,MainActivity.this);
        activeHour("Yes");
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
        Log.d("Received", "ReceiverYes10");

        //  localFileName is with the file with extension which needs the content to be written on it.
        File file = new File(context.getFilesDir(), localFileName);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        if(!file.exists()&& !file.isDirectory()){
            bufferedWriter.write("Message");
            bufferedWriter.write(",");
            bufferedWriter.write("Category");
            bufferedWriter.write(",");
            bufferedWriter.write("Time");
            bufferedWriter.write(",");
            bufferedWriter.write("Action");
            bufferedWriter.write("\n");
        }
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
        readFromAnyFile(localFileName, context);
        uploadAnyFile("users", personemail, localFileName, context);

    }

    public void readFromAnyFile(String sourceFileNameWExt, Context context) {
        File dir = context.getFilesDir();
        File file = new File(dir, sourceFileNameWExt);
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
        File dir = context1.getFilesDir();
        File file = new File(dir, sourceFileName);
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
                Log.d("Succes", "Uploaded");
            }
        });
    }

    //////////////////////TOKEN_Id OF EACH USER///////////////////////

    public boolean readFiletokenid(String sourceFileNameWExt,String token,String personemailfortoken ,Context context) {
        File dir = context.getFilesDir();
        File file = new File(dir, sourceFileNameWExt);
        String readData = "";
        //File f = new File()
        try {
            // Handle Csv file properly ASAP!!!.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = token ;

                while (( bufferedReader.readLine())!= null) {
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


    public void writeTotoken_id(String token, String localFileName, String personemail, Context context1)  {
        Log.d("Received", "ReceiverYes11");
       // Boolean check=true;
        //  localFileName is with the file with extension which needs the content to be written on it.
        File file = new File(context1.getFilesDir(), localFileName);
        if (!file.exists())
        {
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
            uploadtoken_file("users", personemail, localFileName, context1);

        } else if(file.exists()){

          boolean check=readFiletokenid(localFileName,token,personemail,context1);
            if(!check)
            {
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
            uploadtoken_file("users", personemail, localFileName, context);}

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

    /////////////////////////////////////////////////

    void openactivehour()
    {
        Intent s=new Intent(MainActivity.this, Default_Notification.class);
        startActivity(s);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public void writeTousertxt(String localFileName, String personemail, Context context) throws IOException {
        Log.d("Received", "usertxt");
        //  localFileName is with the file with extension which needs the content to be written on it.//
        File file=new File(context. getFilesDir(),localFileName);
        Log.d("Context", "msg" + context.getFilesDir());
        boolean check = readFromusertxtFile(localFileName, personemail, context);
        if (!check) {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(personemail);
            bufferedWriter.write("\n");
            bufferedWriter.close();
            Log.d("Received", "Writeuser");
            uploadusertxtFile(personemail, localFileName, context);
        } else {
            Log.d("already", "membered");

        }
    }

    private void uploadusertxtFile(String personemail, String localFileName, Context context) {

        Log.d("Uploading", "Storagebucket_token");
        File dir = context.getFilesDir();
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
        Log.d("Uploading1", "Storagebucket1token");

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Succes", "Uploadedusertxt");
            }
        });


    }

    public boolean readFromusertxtFile(String sourceFileNameWExt, String email, Context context) {

        Log.d("Reader123", "inside read");

        File dir = context.getFilesDir();
        File file = new File(dir, sourceFileNameWExt);
         BufferedReader bufferedReader=null;
        try {
            // Handle Csv file properly ASAP!!!.
             bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                Log.d("status", line);
                if (line.equals(email)) {
                    Log.d("Reading","Already saved");
                    return true;
                }
                else {
                    Log.d("Reading","Not found");
                }


            }
            bufferedReader.close();
        }catch (IOException e) {
            e.printStackTrace();
            Log.d("status", "error!");
            Log.d("status", file.getPath());
        }

        return false;
    }

//////////////////////////////////////////////////////////////////////////////////////////////



    public void activeHour(String check)
    {TimeZone tz = TimeZone.getTimeZone("GMT+5:30");
        Calendar c = Calendar.getInstance(tz);
        int time = Integer.parseInt(String.format("%02d", c.get(Calendar.HOUR_OF_DAY)));

        int p = Calendar.DATE;
        Log.d("Received", "default");
        //localFileName is with the file with extension which needs the content to be written on it.
        try {
            URL website = new URL("https://storage.googleapis.com/ecommercenotify.appspot.com/users/" + personemail + "/" + "active_hours.csv");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream("active_hours.csv");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            File inputFile = new File("active_hours.csv");
            // Read existing file
            CSVReader reader = new CSVReader(new FileReader(inputFile),',');
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
            writer.close();
        } catch (Exception e) {
            Log.d("Catch","Catchenter");
            File dir = getFilesDir();
            File file = new File(dir,"active_hours.csv");
            FileWriter output = null;
            try {
                output = new FileWriter(file);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Log.d("Catch","Catchenter1");

            CSVWriter writer1 = new CSVWriter(output);
            String[] header = {"00:00-01:00", "01:00-02:00", "02:00-03:00", "03:00-04:00", "04:00-05:00", "05:00-06:00", "06:00-07:00", "07:00-08:00", "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00", "21:00-22:00", "22:00-23:00", "23:00-00:00", "date"};
            writer1.writeNext(header);
            String[] data = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", Integer.toString(p)};
            if (check.equals("No")) {
                data[time] = Integer.toString(Integer.parseInt(data[time]) + 1);
                writer1.writeNext(data);
                upload_file("users", personemail, "active_hours.csv", this);
            } else if (check.equals("Yes")) {
                data[time] = Integer.toString(Integer.parseInt(data[time]) + 3);
                writer1.writeNext(data);
                Log.d("Catch","Catchenter2");
                upload_file("users", personemail, "active_hours.csv", this);


            }
        }


        Log.d("Received", "Default2");
        //readFromAnyFile(localFileName, context);
        //uploadAnyFile("users", personemail, localFileName, context);

    }


   public  void personinfo(String email)
    {
        personemail=email;
    }


    public void upload_file(String firebaseFolder, String personemail, String sourceFileName, Context context1) {
        Log.d("Uploading", "Storagebucket_activehours");
        File dir = context1.getFilesDir();
        File file = new File(dir, sourceFileName);

        Uri file2 = Uri.fromFile(file);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ecommercenotify.appspot.com");
        StorageReference storageRef = storage.getReference();

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




}


