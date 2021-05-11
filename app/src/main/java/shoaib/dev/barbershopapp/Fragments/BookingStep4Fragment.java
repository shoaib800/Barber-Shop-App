package shoaib.dev.barbershopapp.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import shoaib.dev.barbershopapp.Common.Common;
import shoaib.dev.barbershopapp.Model.BookingInformation;
import shoaib.dev.barbershopapp.Model.MyNotification;
import shoaib.dev.barbershopapp.R;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    AlertDialog dialog;


    @BindView(R.id.txt_booking_barber_text)
    TextView txt_booking_barber_text;
    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;
    @BindView(R.id.txt_salon_address)
    TextView txt_salon_address;
    @BindView(R.id.txt_salon_name)
    TextView txt_salon_name;
    @BindView(R.id.txt_salon_open_hours)
    TextView txt_salon_open_hours;
    @BindView(R.id.txt_salon_phone)
    TextView txt_salon_phone;
    @BindView(R.id.txt_salon_website)
    TextView txt_salon_website;

    //-----------------------------getting data  for user information;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseUsers = database.getReference("Users");
    String id = firebaseAuth.getCurrentUser().getUid();
    DatabaseReference username = databaseUsers.child(id).child("name");
    String user_phonenumber,user_name;
    @OnClick(R.id.btn_confirm)
    void confirmBooking(){

        dialog.show();

        //Process Timestamp
        //We will use Timestamp to filter all booking with date is greater today
        //For only display all future booking
        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-"); // Split ex : 9:00 - 10:00
        // Get start time 9:00
        String[] startTimeConverter = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConverter[0].trim()); // we get 9
        int startMinInt = Integer.parseInt(startTimeConverter[1].trim());// we get 00

        Calendar bookingDateWithourHouse = Calendar.getInstance();
        bookingDateWithourHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());
        bookingDateWithourHouse.set(Calendar.HOUR_OF_DAY, startHourInt);
        bookingDateWithourHouse.set(Calendar.MINUTE,startMinInt);

        // Create timestamp object and apply to bookingInformation
        Timestamp timestamp = new Timestamp(bookingDateWithourHouse.getTime());

//        if (Common.currentSalon == null){
//            Log.d("----->","cueeewnr salalon issue");
//        }else if (Common.currentBarber == null){
//            Log.d("----->","cueeewnr babr issue");
//
//        }
//        else{
//            Log.d("...........",Common.city);
//            Log.d("...........",Common.currentSalon.getSalonId());
//            Log.d("...........",Common.currentBarber.getBarberId());
//            Log.d("...........",Common.currentBarber.getName());
//            Log.d("...........",Common.currentSalon.getAddress());
//            Log.d("...........",Common.currentSalon.getName());
//            Log.d("...........",String.valueOf(Common.currentTimeSlot));
            //Create booking information
        final BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setCityBook(Common.city);
        bookingInformation.setTimestamp(timestamp);
        bookingInformation.setDone(false); // Always FALSE, because we will use this field to filter for display on user
        bookingInformation.setBarberId(Common.currentBarber.getBarberId());
        bookingInformation.setBarberName(Common.currentBarber.getName());
        bookingInformation.setCustomerName(user_name);
        bookingInformation.setCustomerPhone(user_phonenumber);
        bookingInformation.setSalonId(Common.currentSalon.getSalonId());
        bookingInformation.setSalonAddress(Common.currentSalon.getAddress());
        bookingInformation.setSalonName(Common.currentSalon.getName());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(bookingDateWithourHouse.getTime())).toString());

        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        //Submit to barber document
        // /AllSalons/Jhelum/Branch/cWez0j0p1UqNrr4liN3D/Barber/hsOgnLrmtVOV2yUSUTs3
        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("AllSalons")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentSalon.getSalonId())
                .collection("Barber")
                .document(Common.currentBarber.getBarberId())
                .collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));

        // Write data
        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Here we can write function to check
                        // if already exist a booking , we will prevent new booking

                       addToUserBooking(bookingInformation);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();

                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        }

    private void addToUserBooking(BookingInformation bookingInformation) {
            // First create new collection
            final CollectionReference userBooking = FirebaseFirestore.getInstance()
                    .collection("User")
                    .document(firebaseAuth.getCurrentUser().getPhoneNumber())
                    .collection("Booking");
            // Check if exist document in this collection
            //Get current date
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);

            Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());

            userBooking
                    .whereGreaterThanOrEqualTo("timestamp",toDayTimeStamp)
                    .whereEqualTo("done", false)
                    .limit(1) //Only take 1
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.getResult().isEmpty())
                            {
                                //Set data
                                userBooking.document()
                                        .set(bookingInformation)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                MyNotification myNotification = new MyNotification();
                                                myNotification.setUid(UUID.randomUUID().toString());
                                                myNotification.setTitle("New Booking");
                                                myNotification.setContent("You have new appointment for Customer hair care with " + user_name);
                                                myNotification.setRead(false); //We will filter only notification with read = "false"
                                                myNotification.setServerTimestamp(FieldValue.serverTimestamp());

                                                //Submit Notification to "Notification" collection of barber
                                                FirebaseFirestore.getInstance()
                                                        .collection("AllSalons")
                                                        .document(Common.city)
                                                        .collection("Branch")
                                                        .document(Common.currentSalon.getSalonId())
                                                        .collection("Barber")
                                                        .document(Common.currentBarber.getBarberId())
                                                        .collection("Notifications")
                                                        .document(myNotification.getUid()) //Unique Key
                                                        .set(myNotification)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                if(dialog.isShowing())
                                                                    dialog.dismiss();

                                                                addToCalender(Common.bookingDate, Common.convertTimeSlotToString(Common.currentTimeSlot));

                                                                resetStaticData();
                                                                getActivity().finish(); //Close Activity
                                                                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();

//                                                                //First, Get Token base on Barber Id
//                                                                FirebaseFirestore.getInstance()
//                                                                        .collection("Tokens")
//                                                                        .whereEqualTo("userPhone", Common.currentBarber.getUsername())
//                                                                        .limit(1)
//                                                                        .get()
//                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                                            @Override
//                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                                                if(task.isSuccessful() && task.getResult().size() > 0)
//                                                                                {
//                                                                                    MyToken myToken = new MyToken();
//                                                                                    for(DocumentSnapshot tokenSnapshot : task.getResult())
//                                                                                        myToken = tokenSnapshot.toObject(MyToken.class);
//
//                                                                                    //Create Data to Send
//                                                                                    FCMSendData sendRequest = new FCMSendData();
//                                                                                    Map<String, String> dataSend = new HashMap<>();
//                                                                                    dataSend.put(Common.TITLE_KEY, "New Booking");
//                                                                                    dataSend.put(Common.CONTENT_KEY, "You have new booking from User " + Common.currentUser.getName());
//
//                                                                                    sendRequest.setTo(myToken.getToken());
//                                                                                    sendRequest.setData(dataSend);
//
//                                                                                    compositeDisposable.add( ifcmApi.sendNotification(sendRequest)
//                                                                                            .subscribeOn(Schedulers.io())
//                                                                                            .observeOn(AndroidSchedulers.mainThread())
//                                                                                            .subscribe(new Consumer<FCMResponse>() {
//                                                                                                @Override
//                                                                                                public void accept(FCMResponse fcmResponse) throws Exception {
//                                                                                                    if(dialog.isShowing())
//                                                                                                        dialog.dismiss();
//
//                                                                                                    addToCalendar(Common.bookingDate, Common.convertTimeSlotToString(Common.currentTimeSlot));
//
//                                                                                                    resetStaticData();
//                                                                                                    getActivity().finish(); //Close Activity
//                                                                                                    Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
//                                                                                                }
//                                                                                            }, new Consumer<Throwable>() {
//                                                                                                @Override
//                                                                                                public void accept(Throwable throwable) throws Exception {
//                                                                                                    Log.d("NOTIFICATION_ERROR", throwable.getMessage());
//
//                                                                                                    addToCalendar(Common.bookingDate, Common.convertTimeSlotToString(Common.currentTimeSlot));
//
//                                                                                                    resetStaticData();
//                                                                                                    getActivity().finish(); //Close Activity
//                                                                                                    Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
//
//                                                                                                }
//                                                                                            }));
//
//                                                                                }
//                                                                            }
//                                                                        });
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                if (dialog.isShowing())
                                                    dialog.dismiss();
                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            else
                            {
                                if (dialog.isShowing())
                                    dialog.dismiss();

                                resetStaticData();
                                getActivity().finish(); // Close Activity
                                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }

    private void addToCalender(Calendar bookingDate, String startDate) {

            String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
            String[] convertTime = startTime.split("-"); // Split ex : 9:00 - 10:00
            // Get start time 9:00
            String[] startTimeConverter = convertTime[0].split(":");
            int startHourInt = Integer.parseInt(startTimeConverter[0].trim()); // we get 9
            int startMinInt = Integer.parseInt(startTimeConverter[1].trim());// we get 00

            String[] endTimeConverter = convertTime[1].split(":");
            int endHourInt = Integer.parseInt(endTimeConverter[0].trim()); // we get 10
            int endMinInt = Integer.parseInt(endTimeConverter[1].trim());// we get 00

            Calendar startEvent = Calendar.getInstance();
            startEvent.setTimeInMillis(bookingDate.getTimeInMillis());
            startEvent.set(Calendar.HOUR_OF_DAY, startHourInt); //Set event start hr
            startEvent.set(Calendar.MINUTE, startMinInt); //Set event start min

            Calendar endEvent = Calendar.getInstance();
            endEvent.setTimeInMillis(bookingDate.getTimeInMillis());
            endEvent.set(Calendar.HOUR_OF_DAY, endHourInt); //Set event end hr
            endEvent.set(Calendar.MINUTE, endMinInt); //Set event end min

            //after we have starEvent and endEvent , convert it format string
            SimpleDateFormat calenderDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String startEventTime = calenderDateFormat.format(startEvent.getTime());
            String endEventTime = calenderDateFormat.format(endEvent.getTime());

            addToDeviceCalender(startEventTime,endEventTime, "Salon Booking",
                    new StringBuilder("Get services from ")
                            .append(startTime)
                            .append(" with ")
                            .append(Common.currentBarber.getName())
                            .append(" at ")
                            .append(Common.currentSalon.getName()).toString(),
                    new StringBuilder("Address: ").append(Common.currentSalon.getAddress()).toString());



    }

    private void addToDeviceCalender(String startEventTime, String endEventTime, String title, String description, String location) {
        SimpleDateFormat calenderDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try {
            Date start = calenderDateFormat.parse(startEventTime);
            Date end = calenderDateFormat.parse(endEventTime);

            ContentValues event = new ContentValues();

            //Put
            event.put(CalendarContract.Events.CALENDAR_ID,getCalender(getContext()));
            event.put(CalendarContract.Events.TITLE,title);
            event.put(CalendarContract.Events.DESCRIPTION,description);
            event.put(CalendarContract.Events.EVENT_LOCATION,location);

            //Time
            event.put(CalendarContract.Events.DTSTART,start.getTime());
            event.put(CalendarContract.Events.DTEND,end.getTime());
            event.put(CalendarContract.Events.ALL_DAY,0);
            event.put(CalendarContract.Events.HAS_ALARM,1);

            String timeZone = TimeZone.getDefault().getID();
            event.put(CalendarContract.Events.EVENT_TIMEZONE,timeZone);

            Uri calendars;
            if (Build.VERSION.SDK_INT >= 0)

                calendars= Uri.parse("content://com.android.calendar/events");
            else
                calendars= Uri.parse("content://calendar/events");

            Uri uri_save = getActivity().getContentResolver().insert(calendars,event);
            //Save to cache
            Paper.init(getActivity());
            Paper.book().write(Common.EVENT_URI_CACHE,uri_save.toString());


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private String getCalender(Context context) {
        //Set default calendar id of Gmail calendar
        String gmailIdCalendar = "";
        String projection[] = {"_id","calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = context.getContentResolver();
        //select all calendar
        Cursor managedCursor = contentResolver.query(calendars,projection,null,null,null);
        if (managedCursor.moveToFirst())
        {
            String calName;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            do {
                calName = managedCursor.getString(nameCol);
                if (calName.contains("@gmail.com"))
                {
                    gmailIdCalendar = managedCursor.getString(idCol);
                    break;//exist as soon as have id
                }
            }while (managedCursor.moveToNext());
            managedCursor.close();
        }

        return gmailIdCalendar;
    }


    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentSalon = null;
        Common.currentBarber = null;
        Common.bookingDate.add(Calendar.DATE, 0); // current Date added
    }

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        txt_booking_barber_text.setText(Common.currentBarber.getName());
        txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.bookingDate.getTime())));

        txt_salon_address.setText(Common.currentSalon.getAddress());
        txt_salon_website.setText(Common.currentSalon.getWebsite());
        txt_salon_name.setText(Common.currentSalon.getName());
        txt_salon_open_hours.setText(Common.currentSalon.getOpenHours());
    }


    @Override
    public void onStart() {
        super.onStart();
        username.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue().toString();
                user_name=username;
                user_phonenumber=firebaseAuth.getCurrentUser().getPhoneNumber();
                Log.d("----->",user_phonenumber+"----"+user_name);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance()
    {
        if (instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply format for date display
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false)
                .build();
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_four, container, false);
        unbinder = ButterKnife.bind(this, itemView);

        return itemView;

    }
}
