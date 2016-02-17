package com.jtnd.flinger;

import java.io.IOException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Activity2 extends Activity {

	
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       
       // Restore preferences
       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
       String IP_PORT = preferences.getString("IP_PORT", "");
       String Username = preferences.getString("Username", "");
       String Password = preferences.getString("Password", "");
        
       Intent intent = getIntent();
       Bundle extras = intent.getExtras();
       String action = intent.getAction();
       
       String[] split = null;
       String sliced = null;
       
       if(action == Intent.ACTION_SEND)
       {
    	   if(extras.containsKey(Intent.EXTRA_TEXT)) 
    		   split = extras.getString(Intent.EXTRA_TEXT).split("=");
       }
   	   else
	   {
		   split = intent.getData().toString().split("=");
	   }
       
       if(split[0].indexOf("vnd.youtube:") >= 0)
       {
    	   int index = split[0].indexOf("?");
    	   int tempindex = split[0].indexOf(":");
    	   
    	   sliced = split[0].substring(tempindex + 1, index);
       }
       else if(split[1].equals(null))
       {
    	   new AlertDialog.Builder(this).setTitle("Error").setMessage("Unsupported format!").setNeutralButton("Close", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) 
	           {
	                Activity2.this.finish();
	           }
	       }).show(); 
       }
       else
       {
    	   int index = split[1].indexOf("&");
    	   if(index >= 0)
    	   {
    		   sliced = split[1].substring(0, index);
    	   }
       }
       
	   HttpParams httpParameters = new BasicHttpParams();
	   
       HttpConnectionParams.setConnectionTimeout(httpParameters, 2000);
	   // Set the default socket timeout (SO_TIMEOUT) 
	   // in milliseconds which is the timeout for waiting for data.
	   HttpConnectionParams.setSoTimeout(httpParameters, 3000);
       
	   DefaultHttpClient client = new DefaultHttpClient(httpParameters);
	   
       client.getCredentialsProvider().setCredentials(
               new AuthScope(null, -1),
               new UsernamePasswordCredentials(Username, Password));
       
	   HttpGet request = new HttpGet("http://" + IP_PORT + "/xbmcCmds/xbmcHttp?command=ExecBuiltIn(PlayMedia(plugin://video/youtube/?path=/root%26action=play_video%26videoid=" + sliced + "))");
       
	   
	   try 
	   {
		   client.execute(request);
		   
		   this.finish();
	   } 
	   catch (ClientProtocolException e) 
	   {
		   new AlertDialog.Builder(this).setTitle("Error").setMessage("Client Exception").setNeutralButton("Close", null).show(); 
	   } 
	   catch (IOException e) 
	   {
		   AlertDialog.Builder error = new AlertDialog.Builder(this);
		   
		   error.setTitle("Error");
		   
		   if(e.toString().equals("java.net.SocketTimeoutException: Socket is not connected"))
			   error.setMessage("Connection couldn't be made.");
		   else if(e.toString().equals("java.net.SocketTimeoutException: The operation timed out"))
			   error.setMessage("Connection was made but server didn't respond."); 
		   else
			   error.setMessage("Unknown IOException occurred."); 
		   
		   error.setNeutralButton("Close",new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) 
	           {
	                Activity2.this.finish();
	           }
	       }).show();
	   }
   }
}
