package com.sayed.packingsolution.misc;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sayed.packingsolution.adapter.ViewIncAdapter;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

    private static final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private static final DatabaseReference settings_node = db.getReference("setting");
    private static final DatabaseReference completion_mail_node = settings_node.child("completion_mail");


    //private static String FROM_MAIL="tmsbyshahin@gmail.com",FROM_MAIL_PASS="xkmjqbuneqgwqpfa",MSG_BODY="TEST BODY",MSG_SUBJ="TEST SUBJ";

    private static String FROM_MAIL=null,FROM_MAIL_PASS=null,MSG_BODY,MSG_SUBJ;

    private static String MAIL_HOST,MAIL_PORT;

    public static String getMailHost() {
        return MAIL_HOST;
    }

    public static void setMailHost(String mailHost) {
        MAIL_HOST = mailHost;
    }

    public static String getMailPort() {
        return MAIL_PORT;
    }

    public static void setMailPort(String mailPort) {
        MAIL_PORT = mailPort;
    }

    public static String getFromMail() {
        return FROM_MAIL;
    }

    public static void setFromMail(String fromMail) {
        FROM_MAIL = fromMail;
    }

    public static String getFromMailPass() {
        return FROM_MAIL_PASS;
    }

    public static void setFromMailPass(String fromMailPass) {
        FROM_MAIL_PASS = fromMailPass;
    }

    public static String getMsgBody() {
        return MSG_BODY;
    }

    public static void setMsgBody(String msgBody) {
        MSG_BODY = msgBody;
    }

    public static String getMsgSubj() {
        return MSG_SUBJ;
    }

    public static void setMsgSubj(String msgSubj) {
        MSG_SUBJ = msgSubj;
    }



    private static void loadMailSettingFromDB(){

        System.out.println("Entry");

        completion_mail_node.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                setFromMail(dataSnapshot.child("from_mail").getValue().toString());
                setFromMailPass(dataSnapshot.child("from_mail_pass").getValue().toString());
                setMsgBody(dataSnapshot.child("msg_body").getValue().toString());
                setMsgSubj(dataSnapshot.child("msg_subj").getValue().toString());
                setMailHost(dataSnapshot.child("mail_host").getValue().toString());
                setMailPort(dataSnapshot.child("mail_port").getValue().toString());
                System.out.println("On get()"+dataSnapshot);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("On get() Failed");
            }
        });

        /*

        completion_mail_node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setFromMail(snapshot.child("from_mail").getValue().toString());
                setFromMailPass(snapshot.child("from_mail_pass").getValue().toString());
                setMsgBody(snapshot.child("msg_body").getValue().toString());
                setMsgSubj(snapshot.child("msg_subj").getValue().toString());
                setMailHost(snapshot.child("mail_host").getValue().toString());
                setMailPort(snapshot.child("mail_port").getValue().toString());
                System.out.println("On valueEvent"+snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */
    }



    public static void sendEmail(String recipient, Context context){

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Sending Email....");
        progressDialog.setCancelable(false);
        progressDialog.show();


            //loadMailSettingFromDB();

        completion_mail_node.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                setFromMail(dataSnapshot.child("from_mail").getValue().toString());
                setFromMailPass(dataSnapshot.child("from_mail_pass").getValue().toString());
                setMsgBody(dataSnapshot.child("msg_body").getValue().toString());
                setMsgSubj(dataSnapshot.child("msg_subj").getValue().toString());
                setMailHost(dataSnapshot.child("mail_host").getValue().toString());
                setMailPort(dataSnapshot.child("mail_port").getValue().toString());
                System.out.println("On get()"+dataSnapshot);



                Properties properties = new Properties();

                // Setup mail server
                properties.put("mail.smtp.host", MAIL_HOST);
                properties.put("mail.smtp.port", MAIL_PORT);
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.auth", "true");


                Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(FROM_MAIL, FROM_MAIL_PASS);

                    }

                });

                // Used to debug SMTP issues
                //session.setDebug(true);



                try {

                    MimeMessage message = new MimeMessage(session);


                    message.setFrom(new InternetAddress(FROM_MAIL));

                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));


                    message.setSubject(MSG_SUBJ);

                    message.setText(MSG_BODY);

                    Transport.send(message);

                    progressDialog.dismiss();
                    Toast.makeText(context,"Email has been sent",Toast.LENGTH_SHORT).show();



                }
                catch (Exception mex) {
                    progressDialog.dismiss();

                    Toast.makeText(context,"Failed to send Email!",Toast.LENGTH_LONG).show();


                }







            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("On get() Failed");
            }
        });




    }










}
