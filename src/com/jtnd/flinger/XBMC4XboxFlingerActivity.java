package com.jtnd.flinger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;


public class XBMC4XboxFlingerActivity extends Activity {
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main);
       
       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
       String IP_PORT = preferences.getString("IP_PORT", "");
       String Username = preferences.getString("Username", "");
       String Password = preferences.getString("Password", "");
       
       EditText text1 = (EditText) findViewById(R.id.editIP);
       EditText text2 = (EditText) findViewById(R.id.editUser);
       EditText text3 = (EditText) findViewById(R.id.editPass);
       
       text1.setText(IP_PORT);
       text2.setText(Username);
       text3.setText(Password);
   }
   
   public void buttonOnClick(View view)
   {
       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	   
	   EditText text1 = (EditText) findViewById(R.id.editIP);
       EditText text2 = (EditText) findViewById(R.id.editUser);
       EditText text3 = (EditText) findViewById(R.id.editPass);
	   
	   SharedPreferences.Editor editor = preferences.edit();
	   editor.putString("IP_PORT", text1.getText().toString());
	   editor.putString("Username", text2.getText().toString());
	   editor.putString("Password", text3.getText().toString());
	   
	   editor.commit();
	   
	   new AlertDialog.Builder(this).setTitle("Notice").setMessage("Settings saved!").setNeutralButton("Close", null).show(); 
   }
  
}