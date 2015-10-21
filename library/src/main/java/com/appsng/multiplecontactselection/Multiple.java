package com.appsng.multiplecontactselection;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;


public class Multiple extends AppCompatActivity {

	static ListView listView;
	private ArrayList<Contact> list = new ArrayList<>();
	ArrayList<String> selected_phones = new ArrayList<String>();
    static final int DONE = Menu.FIRST;
    android.app.ActionBar acBar;
    
    Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        
        listView = (ListView)findViewById(R.id.contact_list);
        
        context = getApplicationContext();
        acBar = getActionBar();
        
        acBar.setTitle("Select Contact");
        acBar.setDisplayHomeAsUpEnabled(true);
        
        FetchContactonBackground();
	}
	
	private void FetchContactonBackground() {
		final Handler handlert = new Handler();
		Thread loginUserThread = new Thread(new Runnable() {
			public void run() {
				FetchContact();
					handlert.post(new Runnable() {
					public void run() {

						ContactAdapter objAdapter = new ContactAdapter(context,list,true);
						listView.setAdapter(objAdapter);	
						listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); 
						listView.setTextFilterEnabled(true);
						setClickEvents();
						
					}

				});
			}
		});
		loginUserThread.start();		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private void FetchContact() {
		 String sort = Phone.DISPLAY_NAME+" ASC";

		Cursor phones = context.getContentResolver().query(
				
				Phone.CONTENT_URI, null, null,
				null, sort);
		while (phones.moveToNext()) {

			String name = phones
					.getString(phones
							.getColumnIndex(Phone.DISPLAY_NAME));

			String phoneNumber = phones
					.getString(phones
							.getColumnIndex(Phone.NUMBER));
			
			String contactid = phones
					.getString(phones
							.getColumnIndex(Phone.CONTACT_ID));

			Contact objContact = new Contact();
			objContact.setName(capitallizeString(name));
			objContact.setPhoneNo(phoneNumber);
			objContact.setContactId(contactid);
			objContact.setSelected(false);
			list.add(objContact);

		}

		
		phones.close();


	}

	public static String capitallizeString(String text){
		return String.valueOf(text.charAt(0)).toUpperCase(Locale.getDefault()) + text.subSequence(1, text.length());
	}


	private void setClickEvents() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				 CheckBox checkbox = (CheckBox) view.getTag(R.id.check);
				 list.get(position).setSelected(view.isSelected()); // Set the value of checkbox to maintain its state.
				 if(checkbox.isChecked()){
					 checkbox.setChecked(false);
				 }else{
					 checkbox.setChecked(true);
				 }
				 showSelected();
				 //checkbox.setChecked(view.isSelected());
			}
			
		});
	}
	
	
   private void showSelected() {
	   int selected = 0;
       int len = listView.getCount();
    	SparseBooleanArray checked = listView.getCheckedItemPositions();
    	for (int i = 0; i < len; i++)
    	 if (checked.get(i)) {
    		 String g = list.get(i).getPhoneNo();
    		 g = g.replace("-","");
    		 g = g.replace(" ","");
    		 g = g.replace("(","");
    		 g = g.replace(")","");
    		 selected_phones.add(g);
    		 selected++;
    	 }
    	getActionBar().setSubtitle(Html.fromHtml("<font color='#FFFFFF'>"+selected+" selected</font>"));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0,DONE, Menu.NONE,"DONE")
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	public void onDestroy() {
		   super.onDestroy();
	}	
    
    @Override 
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {   
        case android.R.id.home:
        	finish();
        	return true;
        case DONE:
        	SendDataBack();
        	return true;
            default:
            return false;
          }
    }

    
	private void SendDataBack() {
		Intent returnIntent = new Intent();
		//Utilities.toast(context, "Selected: "+selected_phones);
		returnIntent.putExtra("result",selected_phones);
		setResult(RESULT_OK,returnIntent);
		finish();
	}
    
	
	private void returnEmptyData(){
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}

	
}