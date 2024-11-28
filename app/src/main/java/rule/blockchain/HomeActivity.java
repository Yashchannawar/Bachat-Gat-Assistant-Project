package rule.blockchain;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rule.blockchain.adapter.HomeOffersAdapter;
import rule.blockchain.loan.AddLoanActivity;
import rule.blockchain.loan.AllLoansActivity;
import rule.blockchain.penalty.AddPenaltyActivity;
import rule.blockchain.penalty.AllPenaltyActivity;

public class HomeActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_CAPTURE = 2;
    private static final int PICK_IMAGE_REQUEST = 1;
    private CardView selectedImageView;
    TextView[] dots;
    ViewPager viewPager;
    LinearLayout layout;
    BottomNavigationView bottom_navigation_bar;
    List<String> drugNames = new ArrayList<>(); // Fetch drug names from your database
    private Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_detector);
        viewPager = findViewById(R.id.offer_slidder);


        findViewById(R.id.medicalRecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.userSession.removeUser();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        findViewById(R.id.addMapGhatMember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapGhatAndMemberActivity.class));
            }
        });


        findViewById(R.id.addMember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddMemberActivity.class));
            }
        });


        findViewById(R.id.addRecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddGhatGroupActivity.class));
            }
        });


        findViewById(R.id.allMembers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllMembersActivity.class);
                intent.putExtra("ghatKey", "pass");
                startActivity(intent);
            }
        });

        findViewById(R.id.allGhats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AllBachatGhatsctivity.class));
            }
        });

        findViewById(R.id.allMapGhat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AllBachatGhatsctivity.class));
            }
        });


        findViewById(R.id.addLoan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddLoanActivity.class));
            }
        });

        findViewById(R.id.allLoans).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AllLoansActivity.class));
            }
        });

        findViewById(R.id.addPenalty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddPenaltyActivity.class));
            }
        });

        findViewById(R.id.allPenalty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AllPenaltyActivity.class));
            }
        });

        layout = findViewById(R.id.dots_container);

        List imgs = new ArrayList<>();
        imgs.add("slide1");
        imgs.add("slide2");
        imgs.add("slide3");
        imgs.add("slide4");


        showOffersImages(imgs);
        bottom_navigation_bar = findViewById(R.id.bottom_navigation_bar);
        bottom_navigation_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    LoginActivity.userSession.removeUser();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                return false;
            }
        });

        // Check if the CAMERA permission is not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request the CAMERA permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CAPTURE);
        }


    }

    public void showOffersImages(List showImages) {

        dots = new TextView[showImages.size()];

        HomeOffersAdapter myAdapter = new HomeOffersAdapter(showImages, getApplicationContext(), null);
        viewPager.setAdapter(myAdapter);

        setIndicators();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectedDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        Handler handler = new Handler();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int i = viewPager.getCurrentItem();

                        if (i == showImages.size() - 1) {
                            i = 0;
                            viewPager.setCurrentItem(i, true);
                        } else {
                            i++;
                            viewPager.setCurrentItem(i, true);
                        }
                    }
                });
            }
        }, 3000, 3000);
    }


    private void selectedDots(int position) {

        for (int i = 0; i < dots.length; i++) {
            try {
                if (i == position) {
                    dots[i].setTextColor(getResources().getColor(R.color.darkGreen));
                } else {
                    dots[i].setTextColor(getResources().getColor(R.color.white));
                }
            } catch (Exception e) {
                break;
            }
        }
    }

    private void setIndicators() {

        for (int i = 0; i < dots.length; i++) {
            try {
                dots[i] = new TextView(getApplicationContext().getApplicationContext());
                dots[i].setText(Html.fromHtml("&#9679;"));
                dots[i].setTextSize(18);
                layout.addView(dots[i]);
            } catch (Exception e) {
                break;
            }
        }
    }


}
